package com.github.vincemann.springlemon.auth.handler;

import com.github.vincemann.springlemon.auth.service.UserService;
import com.github.vincemann.springlemon.auth.service.token.HttpTokenService;
import com.github.vincemann.springrapid.acl.proxy.Unsecured;
import com.github.vincemann.springrapid.core.RapidCoreProperties;
import com.github.vincemann.springrapid.core.security.RapidSecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authentication success handler for sending the response
 * to the client after successful authentication.
 *
 * Adds token to response.
 * 
 * @author Sanjay Patel
 * @modifiedBy vincemann
 */
@Slf4j
public class LemonAuthenticationSuccessHandler
	extends SimpleUrlAuthenticationSuccessHandler {
	

    private UserService<?, ?> unsecuredUserService;
    private HttpTokenService httpTokenService;
	private RapidCoreProperties properties;
	
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {

        // Instead of handle(request, response, authentication),
		// the statements below are introduced
    	response.setStatus(HttpServletResponse.SC_OK);
    	response.setContentType(properties.controller.mediaType);
		String token = unsecuredUserService.createNewAuthToken();
		httpTokenService.appendToken(token,response);

//    	// write current-user data to the response
//    	response.getOutputStream().print(
//    			objectMapper.writeValueAsString(currentUser));

    	// as done in the base class
    	clearAuthenticationAttributes(request);
        
        log.debug("Authentication succeeded for user: " + RapidSecurityContext.getName());
    }

    @Autowired
	public void injectProperties(RapidCoreProperties properties) {
		this.properties = properties;
	}

	@Autowired
	@Unsecured
	public void injectUnsecuredUserService(UserService<?, ?> userService) {
		this.unsecuredUserService = userService;
	}
	
	@Autowired
	public void injectHttpTokenService(HttpTokenService httpTokenService) {
		this.httpTokenService = httpTokenService;
	}
}
