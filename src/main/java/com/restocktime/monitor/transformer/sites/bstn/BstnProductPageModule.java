package com.restocktime.monitor.transformer.sites.bstn;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.restocktime.monitor.monitors.parse.important.bstn.parse.BSTNParseProductAbstractResponse;

public class BstnProductPageModule extends AbstractModule {

    @Override
    public void configure(){

    }

    @Singleton
    @Provides
    public BSTNParseProductAbstractResponse bstnParseProductAbstractResponse(){
        return null;
    }
}
