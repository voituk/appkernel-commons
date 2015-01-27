package com.appkernel.http;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * TODO: This class can not send JSON as a body, right now. Implement it.
 */
public class HttpApiRequestBuilder {

    private static final MediaType URL_ENCODED_MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");


    public static interface RequestBuilderFactory {
        public Request.Builder newBuilder();
    }

    private static final RequestBuilderFactory DEFAULT_BUILDER_FACTORY = new RequestBuilderFactory() {
        @Override
        public Request.Builder newBuilder() {
            return new Request.Builder();
        }
    };


    private static RequestBuilderFactory requestFactory = DEFAULT_BUILDER_FACTORY;


    //
    public static void setRequestBuilderFactory(final RequestBuilderFactory factory) {
        synchronized (HttpApiRequestBuilder.class) {
            requestFactory = factory;
        }
    }

    // Non-static part

    private static int URLENCODED  = 0;
    private static int JSON        = 1;


    private String url;
    private String method;

    private int mediaTypeCode;
    //private MediaType mediaType;

    private StringBuilder urlEncodedContent;
    private CharSequence jsonEncodedContent;

    public HttpApiRequestBuilder(String apiPath, Object... args) {
        super();
        url = String.format(apiPath, args);
        method = "GET";
        mediaTypeCode = URLENCODED;
    }

    public HttpApiRequestBuilder post() {
        method = "POST";
        return this;
    }

    public HttpApiRequestBuilder put() {
        method = "PUT";
        return this;
    }

    public HttpApiRequestBuilder json(CharSequence json) {
        jsonEncodedContent = json;
        mediaTypeCode = JSON;
        return this;
    }


    public HttpApiRequestBuilder add(String name, Object value) {
        if (urlEncodedContent == null) { // Lazy init
            urlEncodedContent = new StringBuilder();
        }
        if (urlEncodedContent.length() > 0) {
            urlEncodedContent.append('&');
        }
        try {
            urlEncodedContent.append(URLEncoder.encode(name, "UTF-8"))
                    .append('=')
                    .append(URLEncoder.encode(value==null ? "" : String.valueOf(value), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
        return this;
    }


    public Request build() {
        final RequestBuilderFactory factory;

        synchronized (HttpApiRequestBuilder.class) {
            factory = requestFactory;
        }

        final Request.Builder builder = factory.newBuilder();


        String newUrl = url;

        if ("POST".equals(method) || "PUT".equals(method)) {

            if (mediaTypeCode == JSON) {
                builder.post(RequestBody.create(JSON_MEDIA_TYPE, jsonEncodedContent.toString()));
                newUrl = appendToUrl(newUrl, urlEncodedContent);

            } else if (mediaTypeCode == URLENCODED) {
                builder.post(RequestBody.create(URL_ENCODED_MEDIA_TYPE, urlEncodedContent.toString()));
            }


        } else if ("GET".equals(method)) {
            newUrl = appendToUrl(newUrl, urlEncodedContent);
            if (mediaTypeCode == JSON && !isEmpty(jsonEncodedContent))
                throw new IllegalArgumentException("There should be no JSON body for GET request");
        }

        return  builder.url(newUrl).build();
    }


    private static boolean isEmpty(final CharSequence s) {
        return s == null || s.length()==0;
    }

    private static String appendToUrl(String url, CharSequence value) {
        if (isEmpty(value))
            return url;
        return url + ( url.indexOf('?') > -1 ? "&" : "?" ) + value;
    }
}
