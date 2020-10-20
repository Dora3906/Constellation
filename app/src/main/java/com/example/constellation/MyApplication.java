package com.example.constellation;

import android.app.Activity;

import org.litepal.LitePalApplication;

import java.util.LinkedList;
import java.util.List;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class MyApplication extends LitePalApplication {

    public static List<Activity> activityList = new LinkedList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        SQLiteStudioService.instance().start(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SQLiteStudioService.instance().stop();
    }

    public static void exit() {
        for (Activity activity : activityList){
            activity.finish();
        }
        System.exit(0);
    }
}
