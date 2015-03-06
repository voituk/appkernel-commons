package com.appkernel.http;

import java.io.InputStream;

/**
 *
 */
public interface HttpApiResponseParser {

    public <T> T parse(InputStream in, Class<T> clazz);

    public <T> T parse(CharSequence str, Class<T> clazz);

    public <T> T parse(HttpApiException e, Class<T> clazz);
}
