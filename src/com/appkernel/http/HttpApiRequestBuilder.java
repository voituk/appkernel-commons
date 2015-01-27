package com.appkernel.http;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

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


    private String url;
    private String method;

    private MediaType mediaType;

    private StringBuilder content;

    public HttpApiRequestBuilder(String apiPath, Object... args) {
        super();
        url = String.format(apiPath, args);
        method = "GET";
    }

    public HttpApiRequestBuilder post() {
        method = "POST";
        mediaType = URL_ENCODED_MEDIA_TYPE;
        return this;
    }

    public HttpApiRequestBuilder put() {
        method = "PUT";
        mediaType = URL_ENCODED_MEDIA_TYPE;
        return this;
    }


    public HttpApiRequestBuilder add(String name, Object value) {
        if (content == null) { // Lazy init
            content = new StringBuilder();
        }
        if (content.length() > 0) {
            content.append('&');
        }
        try {
            content.append(URLEncoder.encode(name, "UTF-8"))
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
            builder.post(RequestBody.create(mediaType, content.toString()));

        } else if ("GET".equals(method)) {
            if (content != null && content.length()>0) {
                newUrl += (newUrl.indexOf('?') > -1 ? "&" : "?") + content;
            }
        }

        return  builder.url(newUrl).build();
    }

}
