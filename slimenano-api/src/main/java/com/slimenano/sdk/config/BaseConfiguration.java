package com.slimenano.sdk.config;

import com.slimenano.sdk.framework.Context;
import com.slimenano.sdk.framework.annotations.Instance;
import com.slimenano.sdk.framework.annotations.Mount;
import com.slimenano.sdk.logger.Marker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Instance
@Marker("配置项")
@Slf4j
public abstract class BaseConfiguration {

    @Mount
    private Context context;


    public void save(){
        log.debug("{} 配置项准备保存", this.getClass());
        try {
            context.storeConfiguration();
            log.debug("{} 配置项保存成功", this.getClass());
        } catch (IOException e) {
            log.error("{} 配置项保存时出现异常", this.getClass(), e);
        }
    }

}
