package com.slimenano.framework.access;

import com.slimenano.framework.event.impl.plugin.PluginLoadFailEvent;
import com.slimenano.framework.event.impl.plugin.PluginUnloadedEvent;
import com.slimenano.sdk.access.Access;
import com.slimenano.sdk.access.AccessManager;
import com.slimenano.sdk.event.annotations.CacheIndex;
import com.slimenano.sdk.event.annotations.EventListener;
import com.slimenano.sdk.event.annotations.Subscribe;
import com.slimenano.sdk.framework.SystemInstance;
import com.slimenano.sdk.framework.annotations.Mount;
import com.slimenano.sdk.framework.ui.GUI_CONST;
import com.slimenano.sdk.framework.ui.IGUIBridge;
import com.slimenano.sdk.plugin.PluginInformation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

@SystemInstance
@EventListener
@CacheIndex(version = 1)
public class AccessManagerImpl implements AccessManager {

    /**
     * 保存所有插件的权限信息
     */
    private final ConcurrentHashMap<PluginInformation, HashSet<Access>> accessMap = new ConcurrentHashMap<>();
    @Mount
    private IGUIBridge bridge;

    @Override
    public boolean hasAccess(PluginInformation information, Access access) {
        if (accessMap.containsKey(information)) {
            HashSet<Access> hashSet = accessMap.get(information);
            return hashSet.stream().anyMatch(access1 -> access1.isAccess(access));
        }
        return false;
    }

    @Override
    public boolean canAccess(PluginInformation information, Access[] accesses) {
        if (accessMap.containsKey(information)) {
            HashSet<Access> hashSet = accessMap.get(information);
            return Arrays.stream(accesses).allMatch(access -> hashSet.stream().anyMatch(access1 -> access1.isAccess(access)));
        }
        return false;
    }

    /**
     * 申请一个权限
     *
     * @return
     */
    @Override
    public boolean useAccess(PluginInformation information, Access access) {
        if (!accessMap.containsKey(information)) return false;
        HashSet<Access> set = accessMap.get(information);
        if (set.contains(access)) return true;
        if (bridge.confirm("动态权限申请",
                "插件：" + information.getName() + "\n" +
                        "类路径：" + information.getPath() + "\n" +
                        "正在尝试申请以下权限：" + Access.toString(new Access[]{access}), GUI_CONST.YES_NO)) {
            set.add(access);
            accessMap.replace(information, new HashSet<>(Arrays.asList(Access.simplify(set.toArray(new Access[0])))));
            return true;
        }
        return false;
    }

    public void registerPlugin(PluginInformation information){
        Access[] simplify = Access.simplify(information.getAccesses());
        accessMap.put(information, new HashSet<>(Arrays.asList(simplify)));
    }

    @Subscribe
    public void onPluginUnLoadedEvent(PluginUnloadedEvent event) {
        accessMap.remove(event.getPayload().getInformation());
    }

    @Subscribe
    public void onPluginLoadFailEvent(PluginLoadFailEvent event) {
        if (event.getPayload() != null)
            accessMap.remove(event.getPayload());
    }

}