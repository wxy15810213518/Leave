package com.xhly.leave;

import android.app.Application;

import org.xutils.x;

/**
 * Created by 新火燎塬 on 2016/7/2. 以及  on 9:39!^-^
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
