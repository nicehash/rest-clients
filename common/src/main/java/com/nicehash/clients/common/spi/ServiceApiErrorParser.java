package com.nicehash.clients.common.spi;

import retrofit2.Response;


public interface ServiceApiErrorParser {
    ServiceApiError parse(Response response);
}
