package com.example.ajeetseeds.backup;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.ajeetseeds.SplashScreen;
import com.example.ajeetseeds.appSetting.AppSetting;

public class ApplicationExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Activity activity;

    public ApplicationExceptionHandler(Activity a) {
        activity = a;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Intent intent = new Intent(activity, SplashScreen.class);
        intent.putExtra("crash", true);
        intent.putExtra("message", ex.getMessage());
        intent.putExtra("getlineno", ex.getStackTrace()[0].getLineNumber());
        intent.putExtra("getfragment", ex.getStackTrace()[0].getClassName());
        intent.putExtra("getmethod", ex.getStackTrace()[0].getMethodName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(AppSetting.getInstance().getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) AppSetting.getInstance().getBaseContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        activity.finish();
        System.exit(2);
    }
}