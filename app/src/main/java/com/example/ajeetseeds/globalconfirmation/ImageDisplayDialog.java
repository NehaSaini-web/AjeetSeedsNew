package com.example.ajeetseeds.globalconfirmation;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.ajeetseeds.R;

public class ImageDisplayDialog {
    public void imageDisplayDialog(Activity activity, String filename) {
        LayoutInflater inflater = activity.getLayoutInflater();
        final View view = inflater.inflate(R.layout.popup_photo_full, null);
        try {
            Dialog dialog = new Dialog(activity, android.R.style.Theme_DeviceDefault_DialogWhenLarge_NoActionBar);
            dialog.setContentView(view);
            //for zoom
            ImageView image = view.findViewById(R.id.image);
            final ScaleGestureDetector mScaleGestureDetector = new ScaleGestureDetector(activity, new ScaleListener(image));
            image.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mScaleGestureDetector.onTouchEvent(motionEvent);
                    return true;
                }
            });
            //end
            Glide.with(activity.getApplicationContext())
                    .load(filename).addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    ProgressBar loading = view.findViewById(R.id.loading);
                    loading.setVisibility(View.GONE);
                    return false;
                }
            }).into(image);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            dialog.show();
            ImageButton closebuttton = view.findViewById(R.id.ib_close);
            closebuttton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
        }
    }
}
