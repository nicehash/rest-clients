package com.nicehash.clients.common.spi;

import retrofit2.Response;

/**
 * @author Ales Justin
 */
public interface ServiceApiErrorParser {
    ServiceApiError parse(Response response);
}
