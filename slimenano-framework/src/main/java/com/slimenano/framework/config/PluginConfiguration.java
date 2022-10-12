package com.slimenano.framework.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.slimenano.sdk.config.BaseConfiguration;
import com.slimenano.sdk.config.Configuration;
import com.slimenano.sdk.config.DefaultConfiguration;
import com.slimenano.nscan.framework.SystemInstance;

import java.util.HashMap;

/**
 * 插件配置项
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SystemInstance
@Configuration(prefix = "plugin")
public class PluginConfiguration extends BaseConfiguration implements DefaultConfiguration {

    // 是否在启动时自动加载插件
    private boolean autoLoad = false;
    private boolean simplyJarFile = true;

    private HashMap<String, PL> trust = new HashMap<>();


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PL{
        private String filename;
        private String path;
        private String hash;
    }

}
