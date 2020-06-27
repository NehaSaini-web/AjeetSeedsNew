package com.example.ajeetseeds;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ajeetseeds.Http_Hanler.GlobalPostingMethod;
import com.example.ajeetseeds.Http_Hanler.HttpHandlerModel;
import com.example.ajeetseeds.Model.AsyModel;
import com.example.ajeetseeds.Model.LoginModel;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.backup.AndroidExceptionHandel;
import com.example.ajeetseeds.backup.ApplicationExceptionHandler;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button cirLoginButton;
    TextInputEditText editTextEmail, editTextPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide title bar and top bar
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_login);
        //todo ectivity Exception
        Thread.setDefaultUncaughtExceptionHandler(new ApplicationExceptionHandler(this));
        //todo exception end

        cirLoginButton = findViewById(R.id.cirLoginButton);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        cirLoginButton.setOnClickListener(this);
        SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
        String userEmail = sessionManagement.getUserEmail();
        if (userEmail != null && !sessionManagement.getUserEmail().equalsIgnoreCase("")) {
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        } else {
            String logoutReason = getIntent().getStringExtra("logoutreason");
            if (logoutReason != null && !logoutReason.contentEquals("")) {
                Snackbar.make(cirLoginButton, logoutReason, Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                }).show();
            }
        }
    }

    LoadingDialog loadingDialog = new LoadingDialog();

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.cirLoginButton: {
                try {
                    if (editTextEmail.getText().toString().equalsIgnoreCase("")) {
                        editTextEmail.setError("Please Enter Email.");
                    } else if (editTextPassword.getText().toString().equalsIgnoreCase("")) {
                        editTextPassword.setError("Please Enter Password.");
                    } else if (!loadingDialog.getLoadingState()) {
                        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.press_btton);
                        cirLoginButton.startAnimation(myAnim);
                        loadingDialog.showLoadingDialog(LoginActivity.this);
                        JSONObject postedJson = new JSONObject();
                        postedJson.put("Email", editTextEmail.getText().toString());
                        postedJson.put("password", editTextPassword.getText().toString());
                        new LoginHitToServer().execute(new AsyModel(StaticDataForApp.login, postedJson, "loginhit"));
                    }
                } catch (Exception e) {
                    new AndroidExceptionHandel(e.getMessage(), "On_Login_Button_Click", e.getStackTrace()[0].getLineNumber()
                            , e.getStackTrace()[0].getClassName(), e.getStackTrace()[0].getMethodName());
                }
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private class LoginHitToServer extends AsyncTask<AsyModel, Void, HttpHandlerModel> {
        private GlobalPostingMethod hitObj = new GlobalPostingMethod();
        private String flagOfAction;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected HttpHandlerModel doInBackground(AsyModel... asyModels) {
            try {
                URL postingUrl = hitObj.createUrl(asyModels[0].getPostingUrl());
                flagOfAction = asyModels[0].getFlagOfAction();
                return hitObj.postHttpRequest(postingUrl, asyModels[0].getPostingJson());
            } catch (Exception e) {
                return hitObj.setReturnMessage(false, "Problem retrieving the user JSON results." + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(HttpHandlerModel result) {
            super.onPostExecute(result);
            bindResponse(result, flagOfAction);
        }
    }

    void bindResponse(HttpHandlerModel result, String flagOfAction) {
        try {
            if (result.isConnectStatus()) {
                if (flagOfAction.equalsIgnoreCase("loginhit")) {
                    try {
                        LoginModel loginModel = new Gson().fromJson(result.getJsonResponse(), LoginModel.class);
                        if (loginModel.condition) {
                            //todo SessionManagement Start
                            SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
                            sessionManagement.setUserName(loginModel.name);
                            sessionManagement.setUserEmail(loginModel.email);
                            sessionManagement.setRoleId(loginModel.roleId);
                            sessionManagement.setApprover_id(loginModel.approver_id);
                            sessionManagement.setUser_type(loginModel.user_type);
                            sessionManagement.setMenu(new Gson().toJson(loginModel.menu));
                            sessionManagement.setPrinterIpAdddress(loginModel.printerIP);
                            sessionManagement.setPrinterPort(loginModel.printerPort);
                            sessionManagement.setModelOfTravel("M1");
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                            //todo if previous user and current user is not math with sync table truncate all table
                            this.deleteDatabase(DatabaseHelper.DB_NAME);
                        } else {
                            Toast.makeText(this, loginModel.message, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        ArrayList<LoginModel> loginModel = new Gson().fromJson(result.getJsonResponse(), new TypeToken<ArrayList<LoginModel>>() {
                        }.getType());
                        if (loginModel.get(0).condition == false) {
                            if (loginModel.get(0).message.contains("Email is wrong")) {
                                editTextEmail.setError(loginModel.get(0).message);
                            } else if (loginModel.get(0).message.contains("Wrong Password")) {
                                editTextPassword.setError(loginModel.get(0).message);
                            } else {
                                Toast.makeText(this, loginModel.get(0).message, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, result.getJsonResponse(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            new AndroidExceptionHandel(e.getMessage(), "Login_Response_Bind", e.getStackTrace()[0].getLineNumber()
                    , e.getStackTrace()[0].getClassName(), e.getStackTrace()[0].getMethodName());
        } finally {
            loadingDialog.dismissLoading();
        }
    }
}
