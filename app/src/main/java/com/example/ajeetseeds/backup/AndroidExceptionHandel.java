package com.example.ajeetseeds.backup;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ajeetseeds.Http_Hanler.GlobalPostingMethod;
import com.example.ajeetseeds.Http_Hanler.HttpHandlerModel;
import com.example.ajeetseeds.Model.AsyModel;
import com.example.ajeetseeds.Model.StaticDataForApp;

import org.json.JSONObject;

import java.net.URL;

public class AndroidExceptionHandel {
    public AndroidExceptionHandel(String Exception, String ExceptionType, int lineNo, String fragment, String method) {
        try {
            JSONObject expostedjason = new JSONObject();
            expostedjason.put("Exception", Exception);
            expostedjason.put("ExceptionType", ExceptionType);
            expostedjason.put("lineNo", lineNo);
            expostedjason.put("fragment", fragment);
            expostedjason.put("method", method);
            new ExceptionHitToServer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new AsyModel(StaticDataForApp.androidExceptionLog_UIException, expostedjason, "Exception"));
        } catch (Exception e) {
        }
    }

    public class ExceptionHitToServer extends AsyncTask<AsyModel, String, HttpHandlerModel> {
        @Override
        protected HttpHandlerModel doInBackground(AsyModel... asyModels) {
            GlobalPostingMethod hit = new GlobalPostingMethod();
            try {
                URL url = hit.createUrl(asyModels[0].getPostingUrl());
                return hit.postHttpRequest(url, asyModels[0].getPostingJson());
            } catch (Exception e) {
                return hit.setReturnMessage(false, "Not Send");
            }
        }

        @Override
        protected void onPostExecute(HttpHandlerModel httpHandlerModel) {
            super.onPostExecute(httpHandlerModel);
            if (!httpHandlerModel.isConnectStatus()) {
                Log.e("NotSend", httpHandlerModel.getJsonResponse());
            } else {
                Log.e("Send", httpHandlerModel.getJsonResponse());
            }
        }
    }
}
