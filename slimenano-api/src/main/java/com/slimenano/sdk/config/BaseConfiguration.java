package com.slimenano.sdk.config;

import com.slimenano.sdk.framework.Context;
import com.slimenano.sdk.framework.annotations.Instance;
import com.slimenano.sdk.framework.annotations.Mount;
import lombok.SneakyThrows;

@Instance
public abstract class BaseConfiguration {

    @Mount
    private Context context;

    @SneakyThrows
    public void save(){
        context.storeConfiguration();
    }

}
