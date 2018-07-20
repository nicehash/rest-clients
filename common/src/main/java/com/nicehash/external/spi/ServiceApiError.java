package com.nicehash.external.spi;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ales Justin
 */
public interface ServiceApiError {
    int getCode();
    String getMessage();

    InputStream getBodyAsStream(); // can be null

    String getBodyAsString() throws IOException; // can be null

    RuntimeException toException();
}
