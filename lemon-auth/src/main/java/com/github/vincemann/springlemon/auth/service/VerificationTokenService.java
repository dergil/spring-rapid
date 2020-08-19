package com.github.vincemann.springlemon.auth.service;

import com.github.vincemann.springrapid.core.slicing.components.ServiceComponent;

/**
 * Used for dealing with tokens generated for verification purposes, usually send via email to the client.
 */
@ServiceComponent
public interface VerificationTokenService extends LemonTokenService {
	String VERIFY_AUDIENCE = "verify";
	String FORGOT_PASSWORD_AUDIENCE = "forgot-password";
	String CHANGE_EMAIL_AUDIENCE = "change-email";
}