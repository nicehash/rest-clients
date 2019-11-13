package com.nicehash.clients.common.spi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientImplementation {
    /**
     * Exact client implementation.
     * Should have public OptionMap constructor.
     *
     * @return client implementation class
     */
    Class<?> value() default Void.class;
}
