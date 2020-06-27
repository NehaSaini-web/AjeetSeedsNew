package com.example.ajeetseeds.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ajeetseeds.MainActivity;
import com.example.ajeetseeds.appSetting.AppSetting;

public class NetworkUtil extends BroadcastReceiver {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static boolean getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        if (conn == NetworkUtil.TYPE_WIFI) {
            return true;       //for wifi
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            return true;       //for mobiledata
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            return false;     // for not connected
        }
        return false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean speedtest = getConnectivityStatusString(context);
        if (speedtest) {
            Intent i = new Intent("checkInterNetBackground");
            i.putExtra("message", true);
            context.sendBroadcast(i);
        } else {
            Intent i = new Intent("checkInterNetBackground");
            i.putExtra("message", false);
            context.sendBroadcast(i);
        }
    }
}