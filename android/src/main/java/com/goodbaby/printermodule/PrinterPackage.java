package com.goodbaby.printermodule;


import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.List;

/**
 * Created by limingguang on 2018/8/22.
 */

public class PrinterPackage implements ReactPackage {

    public PrinterPackage(){

    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        return Arrays.<NativeModule>asList(
           new PrinterModule(reactContext)
        );
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Arrays.asList();
    }
}
