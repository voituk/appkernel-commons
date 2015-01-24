package com.appkernel.http;

import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.appkernel.commons.BuildConfig;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * HTTP API Client
 */
public class HttpApiClient {


    // TODO: Make these IO-timeouts configurable
    private static final int HTTP_CONNECTION_TIMEOUT   = 45000; // Socket connection timeout
    private static final int HTTP_SOCKET_WRITE_TIMEOUT = 60000; // Socket write timeout
    private static final int HTTP_SOCKET_READ_TIMEOUT  = 60000; // Socket read timeout

    private static final String TAG = HttpApiClient.class.getName();


    private final OkHttpClient ok;
    private final HttpApiResponseParser parser;


    public HttpApiClient(HttpApiResponseParser parser, boolean debug) {

        if (parser == null) {
            throw new IllegalArgumentException("HttpApiResponseParser can not be null");
        }

        this.parser = parser;



        ok = new OkHttpClient();

        if (debug) {
            ok.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();

                    long t1 = System.nanoTime();
                    //Log.d(TAG, String.format("[%d] %s %s\n%s", request.hashCode(), request.method(), request.url(), request.headers()));
                    Log.d(TAG, String.format("[%d] %s %s", request.hashCode(), request.method(), request.url()));

                    Response response = chain.proceed(request);

                    long t2 = System.nanoTime();
                    //Log.d(TAG, String.format("[%d] duration=%.1fms\n%s", response.request().hashCode(), (t2 - t1) / 1e6d, response.headers() ));
                    Log.d(TAG, String.format("[%d] duration=%.1fms", response.request().hashCode(), (t2 - t1) / 1e6d ));

                    return response;
                }
            });
        }

        ok.setConnectTimeout(HTTP_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
        ok.setReadTimeout(HTTP_SOCKET_READ_TIMEOUT, TimeUnit.MILLISECONDS);
        ok.setWriteTimeout(HTTP_SOCKET_WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
    }


    public
    @NonNull
    <T> T call(final Request request, final Class<T> clazz) {
        try {

            //Log.d(TAG, String.format("[%d] %s %s", request.hashCode(), request.method(), request.url()));
            final Response response = ok.newCall(request).execute();

            final ResponseBody body = response.body();

            if (body == null)
                throw new HttpApiException(HttpApiException.ERR_NO_RESPONSE, "No response from server");

            final T result = parser.parse(body.byteStream(), clazz);
            if (result == null)
                throw new HttpApiException(HttpApiException.ERR_PARSE_RESPONSE, "Can not read server response");


            //Log.d(TAG, String.format("[%d] Done", request.hashCode()));
            return result;

        } catch (UnknownHostException e) {
            e.printStackTrace();
            return parser.parse(new HttpApiException(HttpApiException.ERR_CONNECTION, "Can't connect to application server. Please check your connection settings and try again."), clazz);

        } catch (SocketException e) {
            e.printStackTrace();
            return parser.parse(
                    new HttpApiException(
                            e.getMessage().indexOf("The operation timed out") != -1
                                    ? HttpApiException.ERR_SOCKETTIMEOUT
                                    : HttpApiException.ERR_CONNECTION,
                            e.getMessage()
                    )
                    , clazz);

        } catch (IOException e) {
            e.printStackTrace();
            return parser.parse(new HttpApiException(HttpApiException.ERR_CONNECTION, e.getMessage()), clazz);

        } catch (HttpApiException e) {
            e.printStackTrace();
            return parser.parse(e, clazz);

        } catch (Throwable e) {
            e.printStackTrace();
            return parser.parse(new HttpApiException(HttpApiException.ERR_UNKNOWN, e.getMessage()), clazz);
        }
    }


}
