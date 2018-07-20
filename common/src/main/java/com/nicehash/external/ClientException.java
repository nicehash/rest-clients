package com.nicehash.external;

import java.io.IOException;
import java.io.InputStream;

import com.nicehash.external.spi.ServiceApiError;

/**
 * @author Ales Justin
 */
public class ClientException extends RuntimeException {
    private int code;
    private ServiceApiError error;

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
        return (error != null ? error.getBodyAsString() : null);
    }
}
