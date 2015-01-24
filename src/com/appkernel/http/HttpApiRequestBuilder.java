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

    public static final MediaType URL_ENCODED_MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private static String apiRoot;
    private static String userAgent;
    private static String acceptLanguage;


    // TODO: yeah, kinda bad design, but dunno what to do with it right now
    synchronized public static void init(String apiRoot, String userAgent) {
        HttpApiRequestBuilder.apiRoot = apiRoot;
        HttpApiRequestBuilder.userAgent = userAgent;


        String lang = Locale.getDefault().getLanguage();
        if (lang == null || lang.length() == 0)
            lang = "en";

        if (lang.equals("en")) {
            HttpApiRequestBuilder.acceptLanguage = "en;q=0.5";
        } else {
            HttpApiRequestBuilder.acceptLanguage = lang + ", en;q=0.5";
        }
    }


    private String url;
    private String method;

    private MediaType mediaType;

    private StringBuilder content;

    public HttpApiRequestBuilder(String apiPath, Object... args) {
        super();
        url = apiRoot + String.format(apiPath, args);
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

        String newUrl = url;

        final Request.Builder builder = new Request.Builder()
                .header("Accept", "application/json");

        synchronized (HttpApiRequestBuilder.class) {
            builder
                .header("User-Agent", userAgent)
                .header("Accept-Language", acceptLanguage);
        }

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
