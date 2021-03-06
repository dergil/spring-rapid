package com.github.vincemann.springlemon.auth.service;

import com.github.vincemann.springlemon.auth.config.UserServiceAutoConfiguration;
import com.github.vincemann.springrapid.acl.proxy.SecurityExtensionServiceProxy;
import com.github.vincemann.springrapid.acl.proxy.AclManaging;
import com.github.vincemann.springrapid.acl.proxy.Secured;
import com.github.vincemann.springrapid.core.proxy.ServiceExtensionProxy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.AopTestUtils;

import java.lang.reflect.Proxy;

/**
 * Extend this class to further configure PreConfigured {@link UserService} Proxies.
 * Used primarily to add more plugins.
 *
 * @see UserServiceAutoConfiguration
 */
public abstract class UserServiceProxyConfigurer  implements InitializingBean {

    private UserService acl;
    private UserService secured;

    @Autowired
    @AclManaging
    public void setAcl(UserService acl) {
        this.acl=acl;
    }

    @Autowired
    @Secured
    public void setSecured(UserService secured) {
        this.secured = secured;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        configureAclManaging((ServiceExtensionProxy) Proxy.getInvocationHandler(AopTestUtils.getUltimateTargetObject(acl)));
        configureSecured((SecurityExtensionServiceProxy) Proxy.getInvocationHandler(AopTestUtils.getUltimateTargetObject(secured)));
    }

    public void configureAclManaging(ServiceExtensionProxy proxy){}
    public void configureSecured(SecurityExtensionServiceProxy proxy){}
}
