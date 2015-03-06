package com.appkernel.http;

/**
 *
 */
public class HttpApiException extends Exception {

    // Server-side errors - codes should be the same as on server
    public static final int ERR_CODE_OK      = 0;

    // Server-side errors - codes should be the same as on server
    public static final int ERR_COMMON_ERROR   = 100;
    public static final int ERR_INVALID_AUTH   = 401;
    public static final int ERR_FORBIDDEN      = 403;

    // Local errors - not server side API (code values can be changed)
    public static final int ERR_CONNECTION     = 600;
    public static final int ERR_NO_RESPONSE    = 601;
    public static final int ERR_SOCKETTIMEOUT  = 602;
    public static final int ERR_INVALID_JSON   = 650;
    public static final int ERR_PARSE_RESPONSE = 698; // Used in HTTP-level
    public static final int ERR_UNKNOWN        = 699;

    private final int code;


    public HttpApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    
}
