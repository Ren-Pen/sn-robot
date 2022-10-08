package com.demo;

import com.slimenano.sdk.robot.messages.content.SNText;
import lombok.extern.slf4j.Slf4j;
import com.slimenano.sdk.config.ConfigLocation;
import com.slimenano.sdk.core.Robot;
import com.slimenano.sdk.framework.annotations.Mount;
import com.slimenano.sdk.logger.Marker;
import com.slimenano.sdk.plugin.BasePlugin;
import com.slimenano.sdk.plugin.Plugin;

import java.util.HashMap;

@Plugin
@ConfigLocation(location = "config")
@Slf4j
@Marker("示例插件")
public class DemoPlugin extends BasePlugin {

    @Mount
    Robot robot;

    /**
     * CLI界面桥扩展方法，指令 demo 可接受参数 --debug | -d
     * @param args
     * @return 指令执行反馈
     */
    public boolean demo(HashMap<String, String> args){

        if (args.containsKey("debug")){
            log.info("调试参数！");
        }else if (args.containsKey("demo")){
            log.info("运行了！ROBOT:" + robot.getCoreType());
        }else{
            log.info("无参！");
        }

        return true;
    }

    public boolean friend(HashMap<String, String> args){
        long target = 0L;
        String msg = args.get("msg");
        try{
            target = Long.parseLong(args.get("target"));
        }catch (Exception e){
            log.error("错误的输入！", e);
            return false;
        }

        robot.getFriend(target).sendMessage(robot, new SNText(msg).toChain());
        return true;
    }

}
