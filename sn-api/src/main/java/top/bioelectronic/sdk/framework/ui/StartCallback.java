package top.bioelectronic.sdk.framework.ui;

import top.bioelectronic.sdk.framework.exception.BeanException;
import top.bioelectronic.sdk.framework.Context;

public interface StartCallback {

    /**
     * 扫描完但还未自动装载
     * @param context
     * @throws BeanException
     */
    void preLoad(Context context) throws BeanException;

    /**
     * 装载并且加载完成
     * @param context
     * @throws BeanException
     */
    void postLoad(Context context) throws BeanException;

}
