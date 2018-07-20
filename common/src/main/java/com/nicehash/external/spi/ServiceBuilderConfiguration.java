package com.nicehash.external.spi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ales Justin
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceBuilderConfiguration {
    /**
     * Get client url.
     *
     * @return client url
     */
    String url() default "${client.url:http://localhost:8080}";

    /**
     * Get service builder class impl.
     *
     * @return service builder class impl
     */
    Class<? extends ServiceBuilder> builder() default NoopServiceBuilder.class;
}
