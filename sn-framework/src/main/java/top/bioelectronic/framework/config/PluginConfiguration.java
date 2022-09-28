package top.bioelectronic.framework.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.bioelectronic.sdk.config.BaseConfiguration;
import top.bioelectronic.sdk.config.Configuration;
import top.bioelectronic.sdk.config.DefaultConfiguration;
import top.bioelectronic.sdk.framework.SystemInstance;

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

    // 已加载的插件信息
    private HashMap<String , PL> plugins = new HashMap<>();

    @Override
    public DefaultConfiguration createDefaultConfigurationObject() {
        return new PluginConfiguration();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PL{
        // 插件md5
        private String md5;
        // 插件jar文件名
        private String file;
        // 插件是否启用（底层还是动态装载和动态卸载）
        private boolean enable;
    }

}
