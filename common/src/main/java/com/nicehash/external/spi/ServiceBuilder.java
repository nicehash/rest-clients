package com.nicehash.external.spi;

import com.nicehash.utils.options.OptionMap;
import okhttp3.Call;

/**
 * Customize client's REST service.
 *
 * @author Ales Justin
 */
public interface ServiceBuilder {
    Call.Factory buildCallFactory(OptionMap optionMap);

    ServiceApiErrorParser parser(OptionMap optionMap);
}
