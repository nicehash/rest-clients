package com.nicehash.clients.common.spi;

import com.nicehash.clients.util.options.OptionMap;
import okhttp3.Call;

/** Customize client's REST service. */
public interface ServiceBuilder {
  Call.Factory buildCallFactory(OptionMap optionMap);

  ServiceApiErrorParser parser(OptionMap optionMap);
}
