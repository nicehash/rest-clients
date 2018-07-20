package com.nicehash.external.spi;

import retrofit2.Response;

/**
 * @author Ales Justin
 */
public interface ServiceApiErrorParser {
    ServiceApiError parse(Response response);
}
