package com.example.ajeetseeds.globalconfirmation;

import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    private float mScaleFactor = 1.0f;
    private ImageView mImageView;

    public ScaleListener(ImageView mImageView) {
        this.mImageView = mImageView;
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        mScaleFactor *= scaleGestureDetector.getScaleFactor();
        mScaleFactor = Math.max(0.1f,
                Math.min(mScaleFactor, 10.0f));
        mImageView.setScaleX(mScaleFactor);
        mImageView.setScaleY(mScaleFactor);
        return true;
    }
}