/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.vincemann.springrapid.log.nickvl;

/**
 * Method descriptor.
 */
final class MethodDescriptor {
    private final InvocationDescriptor invocationDescriptor;
    private volatile ArgumentDescriptor argumentDescriptor;
    private volatile ExceptionDescriptor exceptionDescriptor;

    public MethodDescriptor(InvocationDescriptor invocationDescriptor) {
        this.invocationDescriptor = invocationDescriptor;
    }

    public InvocationDescriptor getInvocationDescriptor() {
        return invocationDescriptor;
    }

    public ArgumentDescriptor getArgumentDescriptor() {
        return argumentDescriptor;
    }

    public void setArgumentDescriptor(ArgumentDescriptor argumentDescriptor) {
        this.argumentDescriptor = argumentDescriptor;
    }

    public ExceptionDescriptor getExceptionDescriptor() {
        return exceptionDescriptor;
    }

    public void setExceptionDescriptor(ExceptionDescriptor exceptionDescriptor) {
        this.exceptionDescriptor = exceptionDescriptor;
    }
}