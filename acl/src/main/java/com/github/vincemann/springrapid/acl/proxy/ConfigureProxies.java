package com.github.vincemann.springrapid.acl.proxy;

import com.github.vincemann.springrapid.core.proxy.ServiceExtensionProxyBuilder;

import java.lang.annotation.*;


/**
 * Annotate your {@link com.github.vincemann.springrapid.core.service.AbstractCrudService} with this annotation to
 * induce the dynamic creation of Proxies for a {@link com.github.vincemann.springrapid.core.service.AbstractCrudService}.
 * This is an alternative solution for {@link SecurityServiceExtensionProxyBuilder}
 * and {@link ServiceExtensionProxyBuilder}.
 *
 * The {@link CrudServiceProxyBeanComposer} will detect the configuration and create the proxies at runtime.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ConfigureProxies {
    Proxy[] value() default {};
    SecurityProxy[] securityProxies() default {};
}
