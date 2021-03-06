package com.github.vincemann.springlemon.auth.slicing.test;

import com.github.vincemann.springlemon.auth.config.LemonAuthExceptionHandlerAutoConfiguration;
import com.github.vincemann.springlemon.auth.config.LemonCommonsWebAutoConfiguration;
import com.github.vincemann.springlemon.auth.config.LemonAuthenticationAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ImportAutoConfiguration({
        LemonAuthExceptionHandlerAutoConfiguration.class,
        LemonCommonsWebAutoConfiguration.class,
        LemonAuthenticationAutoConfiguration.class
})
@ImportLemonAuthGeneralConfig
public @interface ImportLemonAuthWebConfig {
}
