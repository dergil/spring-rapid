/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.vincemann.springrapid.log.nickvl.service;

import com.github.vincemann.springrapid.log.nickvl.annotation.LogDebug;

/**
 * Implements {@link BazService}.
 */
@LogDebug
public class AuxBazService extends AbstractBazService {

    @Override
    public void inImpl(String xFirst, String xSecond) {
        // nothing to do
    }
}