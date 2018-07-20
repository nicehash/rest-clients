package com.nicehash.external;

import com.nicehash.utils.options.OptionMap;

/**
 * @author Ales Justin
 */
public class Clients {
    public static ClientFactory factory() {
        return factory(OptionMap.EMPTY);
    }

    public static ClientFactory factory(OptionMap options) {
        return new ClientFactoryImpl(options);
    }
}
