package com.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.bioelectronic.sdk.config.Configuration;
import top.bioelectronic.sdk.config.DefaultConfiguration;
import top.bioelectronic.sdk.robot.contact.SNMemberPermission;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration(prefix = "demo")
public class MyConfiguration implements DefaultConfiguration {

    private boolean enable = false;
    private long target = 0L;
    private boolean atBot = false;
    private boolean atTarget = false;
    private String noMatcher = null;
    private String noPermission = null;
    private String prefix = "";
    private SNMemberPermission maxPermission = SNMemberPermission.OWNER;
    private SNMemberPermission minPermission = SNMemberPermission.MEMBER;

    @Override
    public DefaultConfiguration createDefaultConfigurationObject() {
        return new MyConfiguration();
    }

    public void setMaxPermission(String maxPermission) {
        try {
            this.minPermission = SNMemberPermission.valueOf(maxPermission);
        }catch (IllegalArgumentException e){
            this.minPermission = SNMemberPermission.OWNER;
        }
    }

    public void setMinPermission(String minPermission) {
        try {
            this.minPermission = SNMemberPermission.valueOf(minPermission);
        }catch (IllegalArgumentException e){
            this.minPermission = SNMemberPermission.MEMBER;
        }
    }

    public void setPrefix(String prefix) {
        if (prefix != null){
            this.prefix = prefix;
        }else{
            this.prefix = "";
        }
    }
}