package com.example.ajeetseeds.globalconfirmation;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.ajeetseeds.R;
import com.victor.loading.newton.NewtonCradleLoading;

public class LoadingDialog {
    private AlertDialog alertDialog;
    private boolean loadingstate = false;
    private NewtonCradleLoading newtonCradleLoading;
    public TextView lodingText;
    public void showLoadingDialog(Activity activity, String... loadingmessage) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.loading_dialog, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(null);
        loadingstate = true;
        newtonCradleLoading = dialogView.findViewById(R.id.newton_cradle_loading);
        lodingText = dialogView.findViewById(R.id.lodingText);
        newtonCradleLoading.start();
        if (loadingmessage != null && loadingmessage.length > 0 && loadingmessage[0] != "")
            lodingText.setText(loadingmessage[0]);
        else
            lodingText.setText("Loading...");
//        newtonCradleLoading.setLoadingColor(R.color.colorPrimary);
    }
    public void dismissLoading() {
        try {
            newtonCradleLoading.stop();
            alertDialog.dismiss();
            loadingstate = false;
        }catch (Exception e){}
    }

    public boolean getLoadingState() {
        return loadingstate;
    }

    public void updateDialogProgress(String progressText) {
        if (getLoadingState() == true) {
            lodingText.setText(progressText);
        }
    }
}
