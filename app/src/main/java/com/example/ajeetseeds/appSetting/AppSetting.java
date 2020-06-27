package com.example.ajeetseeds.appSetting;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.ajeetseeds.Network.NetworkUtil;

import java.util.Timer;
import java.util.TimerTask;

public class AppSetting extends Application implements LifecycleObserver {
    public static AppSetting instance;
    Timer bindlivedataofpage;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);  //todo Observe Application State Start or Stop
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static AppSetting getInstance() {
        return instance;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        Log.e("MyApp", "App in background");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
        bindlivedataofpage = new Timer();
        bindlivedataofpage.schedule(new TimerTask() {
            @Override
            public void run() {
                new UserAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }, 0, 4000);
        Log.e("MyApp", "App in foreground");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onAppDestroy() {
        Log.e("MyApp", "App Going To Resume.");
    }

    private class UserAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... test) {
            boolean Status = NetworkUtil.getConnectivityStatusString(getApplicationContext());
            return Status;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Intent i = new Intent("checkInterNetBackground");
            i.putExtra("message", result);
            getApplicationContext().sendBroadcast(i);
        }
    }

}
