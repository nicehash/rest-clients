package com.nicehash.clients.common;

import com.nicehash.clients.common.spi.ServiceApiError;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ales Justin
 */
public class ClientException extends RuntimeException {
    private static final String NULL = "_NULL_";

    private int code;
    private ServiceApiError error;
    private String errorBody;

    private ClientException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public ClientException(String message, Throwable cause) {
        this(message, cause, -1);
    }

    public ClientException(ServiceApiError apiError) {
        this(apiError.getMessage(), null, apiError.getCode());
        this.error = apiError;
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();
        try {
            return String.format("%s [%s]", msg, getErrorBody());
        } catch (IOException e) {
            return msg;
        }
    }

    /**
     * Get short / original message.
     *
     * @return short message, w/o error body
     */
    public String getShortMessage() {
        return super.getMessage();
    }

    /**
     * Return http status code or -1 if it cannot be gathered.
     *
     * @return status code or -1
     */
    public int getStatusCode() {
        return code;
    }

    /**
     * Get error body as stream. Can be null.
     *
     * @return error body as stream, can be null
     */
    public InputStream getErrorBodyStream() {
        return (error != null ? error.getBodyAsStream() : null);
    }

    /**
     * Get error body as String. Can be null.
     * If error body is large, prefer {@link #getErrorBodyStream()}.
     *
     * @return error body as String, can be null
     */
    public String getErrorBody() throws IOException {
        if (error == null || NULL.equals(errorBody)) {
            return null;
        }
        if (errorBody == null) {
            String body = error.getBodyAsString();
            errorBody = (body != null) ? body : NULL;
            return getErrorBody();
        } else {
            return errorBody;
        }
    }
}
