package com.nicehash.clients.common.spi;

import com.nicehash.clients.util.options.Option;
import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import java.util.concurrent.Executor;


public class Options {

    private Options() {
    }

    /**
     * Base URL
     */
    public static final Option<String> BASE_URL = Option.simple(Options.class, "BASE_URL", String.class);

    /**
     * WebSockets Base URL
     */
    public static final Option<String> WS_BASE_URL = Option.simple(Options.class, "WS_BASE_URL", String.class);

    /**
     * Http client
     */
    public static final Option<OkHttpClient> HTTP_CLIENT = Option.simple(Options.class, "HTTP_CLIENT", OkHttpClient.class);

    /**
     * Call factory
     */
    public static final Option<Call.Factory> CALL_FACTORY = Option.simple(Options.class, "CALL_FACTORY", Call.Factory.class);

    /**
     * Read timeout
     */
    public static final Option<Long> READ_TIMEOUT = Option.simple(Options.class, "READ_TIMEOUT", Long.class);

    /**
     * Write timeout
     */
    public static final Option<Long> WRITE_TIMEOUT = Option.simple(Options.class, "WRITE_TIMEOUT", Long.class);

    /**
     * Auth interceptor.
     */
    public static final Option<Interceptor> AUTH_INTERCEPTOR = Option.simple(Options.class, "AUTH_INTERCEPTOR", Interceptor.class);

    /**
     * Dispatcher
     */
    public static final Option<Dispatcher> DISPATCHER = Option.simple(Options.class, "DISPATCHER", Dispatcher.class);

    /**
     * Web socket client
     */
    public static final Option<OkHttpClient> WEBSOCKET_CLIENT = Option.simple(Options.class, "WEBSOCKET_CLIENT", OkHttpClient.class);

    /**
     * Error response parser
     */
    public static final Option<ServiceApiErrorParser> ERROR_PARSER = Option.simple(Options.class, "ERROR_PARSER", ServiceApiErrorParser.class);

    /**
     * Async executor
     */
    public static final Option<Executor> EXECUTOR = Option.simple(Options.class, "EXECUTOR", Executor.class);

    /**
     * Property replacer
     */
    public static final Option<PropertyReplacer> REPLACER = Option.simple(Options.class, "REPLACER", PropertyReplacer.class);

    /**
     * OAuth token
     */
    public static final Option<String> TOKEN = Option.simple(Options.class, "TOKEN", String.class, true);

    /**
     * Auth email
     */
    public static final Option<String> EMAIL = Option.simple(Options.class, "EMAIL", String.class, true);

    /**
     * Key
     */
    public static final Option<String> KEY = Option.simple(Options.class, "KEY", String.class, true);

    /**
     * Secret
     */
    public static final Option<String> SECRET = Option.simple(Options.class, "SECRET", String.class, true);

    /**
     * Secret Out
     */
    public static final Option<String> SECRET_OUT = Option.simple(Options.class, "SECRET_OUT", String.class, true);

    /**
     * Name
     */
    public static final Option<String> NAME = Option.simple(Options.class, "NAME", String.class, true);

    /**
     * Username
     */
    public static final Option<String> USERNAME = Option.simple(Options.class, "USERNAME", String.class, true);

    /**
     * Password
     */
    public static final Option<String> PASSWORD = Option.simple(Options.class, "PASSWORD", String.class, true);
}
