package com.example.administrator.demo05;

import android.app.Application;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Created by Administrator on 2017/11/16 0016.
 */

public class APP extends Application {
    private static APP app;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        ZXingLibrary.initDisplayOpinion(this);
    }
    public static APP getApp() {
        return app;
    }
}
