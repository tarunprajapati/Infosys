package com.infosys.base;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;


import com.infosys.BuildConfig;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Tarun on 7/07/2018.
 */

public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
        }
    }
}
