package com.example.ajeetseeds;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ajeetseeds.backup.AndroidExceptionHandel;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashScreen extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    Animation animTogether;
    private TextView tv_company, tv_poweredby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_splash_screen);
        //todo rotate image
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.tv_welcome);
        linearLayout.clearAnimation();
        linearLayout.startAnimation(animation);
        animation.reset();

        animation = AnimationUtils.loadAnimation(this, R.anim.translate_pop_up);
        tv_company = (TextView) findViewById(R.id.tv_company);
        tv_poweredby = (TextView) findViewById(R.id.tv_poweredby);
        ImageView company_splash_logo = (ImageView) findViewById(R.id.company_splash_logo);
        company_splash_logo.clearAnimation();
        company_splash_logo.startAnimation(animation);
        tv_company.startAnimation(animation);
        tv_poweredby.startAnimation(animation);
        // todo end rotate
        if (getIntent().getBooleanExtra("crash", false)) {
//            Toast.makeText(this, getIntent().getExtras().get("message").toString(), Toast.LENGTH_LONG).show();
            new AndroidExceptionHandel(getIntent().getExtras().get("message").toString(), "Application_crash",
                    getIntent().getIntExtra("getlineno", 0), getIntent().getExtras().get("getfragment").toString(), getIntent().getExtras().get("getmethod").toString());
            LoadAnotherActivity();
        } else {
            LoadAnotherActivity();
        }
    }

    void LoadAnotherActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkPermission()) {
//                    Toast.makeText(LogoPopupActivity.this, "Permission already granted.", Toast.LENGTH_LONG).show();
                    gotoLogin();
                } else {
//                    Toast.makeText(LogoPopupActivity.this, "Please request permission.", Toast.LENGTH_LONG).show();
                    requestPermission();
                }
            }
        }, 2000);
    }

    void gotoLogin() {
        Intent loginIntent = new Intent(SplashScreen.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);

    }

    //todo when request permision hit
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean readAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (readAccepted && writeAccepted && cameraAccepted) {
//                        Toast.makeText(LogoPopupActivity.this, "Permission Granted, Now you can access location data and camera.", Toast.LENGTH_LONG).show();
                        gotoLogin();
                    } else {
//                        Toast.makeText(LogoPopupActivity.this, "Permission Denied, You cannot access location data and camera.", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }
                break;
        }
    }

    //todo show message yes or no after disallow
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashScreen.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
}
