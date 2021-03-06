package com.github.vincemann.springrapid.core.controller.idFetchingStrategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LongUrlParamIdFetchingStrategyTest {

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void fetchId() throws IdFetchingException {
        //given
        Long testId = 42L;
        LongUrlParamIdFetchingStrategy longUrlParamIdFetchingStrategy = new LongUrlParamIdFetchingStrategy();
        when(httpServletRequest.getParameter("id"))
                .thenReturn(String.valueOf(testId));

        //when
        Long fetchedId = longUrlParamIdFetchingStrategy.fetchId(httpServletRequest);
        //then
        Assertions.assertEquals(testId,fetchedId);
    }
}