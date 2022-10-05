package com.slimenano.sdk.framework;

public interface InitializationBean {

    void onLoad() throws Exception;

    void onDestroy() throws Exception;

}
