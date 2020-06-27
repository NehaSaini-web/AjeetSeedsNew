package com.example.ajeetseeds.globalconfirmation;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.ajeetseeds.R;

public class GlobalConfirmation {
    private static boolean Active;
    public GlobalConfirmation(){
        this.Active=false;
    }
    public boolean isActive() {
        return Active;
    }
    public void AlertDisplay(final Activity activity, String title, String alertMessage) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.Theme_AppCompat_Dialog_Alert);
            builder.setCancelable(true);
            builder.setTitle(title +Active);
            builder.setMessage(alertMessage);
            AlertDialog.Builder ok = builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(activity, "Ok", Toast.LENGTH_SHORT).show();
                    Active=false;
                }

            });

            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Toast.makeText(activity, "Dismiss", Toast.LENGTH_SHORT).show();
                    Active=false;
                }
            });

            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(activity, "Cancel", Toast.LENGTH_SHORT).show();
                    Active=false;
                }
            });

            builder.show();
        Active=true;
    }
}
