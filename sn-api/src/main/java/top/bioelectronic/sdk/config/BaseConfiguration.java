package top.bioelectronic.sdk.config;

import top.bioelectronic.sdk.framework.Context;
import top.bioelectronic.sdk.framework.annotations.Instance;
import top.bioelectronic.sdk.framework.annotations.Mount;
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
