package com.example.ajeetseeds.Http_Hanler;

import android.os.Environment;

import androidx.annotation.Nullable;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class GlobalPostingMethod {

    // TODO: 11-Sep-19 genrate create url...
    public URL createUrl(String stringUrl) throws MalformedURLException {
        try {
            stringUrl = stringUrl.replaceAll("\\s", "%20");
            return new URL(stringUrl);
        } catch (MalformedURLException e) {
            throw new MalformedURLException("Error with creating URL..");
        }
    }


    // TODO: 11-Sep-19 GetHit For Api...
    public HttpHandlerModel getHttpRequest(URL url) {
        // If the URL is null, then return early.
        if (url == null)
            return setReturnMessage(false, "URL is null Check the URL pass on it.");
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // TODO: 11-Sep-19   If the request was successful (response code 200),  then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200)
                return setReturnMessage(true, readFromStream(urlConnection.getInputStream()));
            else
                return setReturnMessage(false, "Error response code: " + urlConnection.getResponseCode());

        } catch (IOException e) {
            return setReturnMessage(false, "Problem retrieving the user JSON results." + e.getMessage());
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }

    // TODO: 11-Sep-19 PostHit When Api Hit
    public HttpHandlerModel postHttpRequest(URL url, JSONObject postedjason) {
        // If the URL is null, then return early.
        if (url == null)
            return setReturnMessage(false, "URL is null Check the URL pass on it.");
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);// indicates POST method
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();
            DataOutputStream printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.writeBytes(postedjason.toString());
            printout.flush();
            printout.close();
            // TODO: 11-Sep-19   If the request was successful (response code 200),     // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200)
                return setReturnMessage(true, readFromStream(urlConnection.getInputStream()));
            else
                return setReturnMessage(false, "Error response code: " + urlConnection.getResponseCode());

        } catch (IOException e) {
            return setReturnMessage(false, "Problem retrieving the user JSON results." + e.getMessage());
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();

        }
    }

    public HttpHandlerModel postArrayHttpRequest(URL url, JSONArray postedjason) {
        // If the URL is null, then return early.
        if (url == null)
            return setReturnMessage(false, "URL is null Check the URL pass on it.");
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);// indicates POST method
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();
            DataOutputStream printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.writeBytes(postedjason.toString());
            printout.flush();
            printout.close();
            // TODO: 11-Sep-19   If the request was successful (response code 200),     // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200)
                return setReturnMessage(true, readFromStream(urlConnection.getInputStream()));
            else
                return setReturnMessage(false, "Error response code: " + urlConnection.getResponseCode());

        } catch (IOException e) {
            return setReturnMessage(false, "Problem retrieving the user JSON results." + e.getMessage());
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();

        }
    }

    public HttpHandlerModel postHttpAndGetFileRequest(URL url, JSONObject postedjason) {
        // If the URL is null, then return early.
        if (url == null)
            return setReturnMessage(false, "URL is null Check the URL pass on it.");
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);// indicates POST method
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();
            DataOutputStream printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.writeBytes(postedjason.toString());
            printout.flush();
            printout.close();
            // TODO: 11-Sep-19   If the request was successful (response code 200),     // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                String contentType = urlConnection.getContentType();
                if (contentType.equalsIgnoreCase("application/pdf")) {
                    String filename = urlConnection.getHeaderField("filename");
                    if (filename != null) {
                        String FileUrl = Environment.getExternalStorageDirectory() + "/PristineZivame/";
                        File fileDirectory = new File(FileUrl);
                        if (!fileDirectory.exists()) {
                            fileDirectory.mkdirs();
                        }
                        File outputFile = new File(fileDirectory, filename);
                        if (outputFile.exists()) {
                            outputFile.delete();
                        }

                        FileOutputStream fos = new FileOutputStream(outputFile);
                        InputStream inputstream = urlConnection.getInputStream();
                        byte[] buffer = new byte[1024];
                        int len1 = 0;
                        while ((len1 = inputstream.read(buffer)) != -1) {
                            fos.write(buffer, 0, len1);
                        }
                        fos.close();
                        inputstream.close();
                        return setReturnMessage(true, "File Found", FileUrl + filename);
                    } else {
                        return setReturnMessage(false, "File Not Found On Server");
                    }
                }
                return setReturnMessage(true, readFromStream(urlConnection.getInputStream()));
            } else
                return setReturnMessage(false, "Error response code: " + urlConnection.getResponseCode());
        } catch (Exception e) {
            return setReturnMessage(false, "Problem retrieving the user JSON results." + e.getMessage());
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();

        }
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            StringWriter writer = new StringWriter();
            IOUtils.copy(inputStream, writer, Charset.forName("UTF-8"));
            String theString = writer.toString();
            return theString;
        }
        return null;
    }

    public HttpHandlerModel setReturnMessage(boolean connectStatus, String passdata, @Nullable String... localstorageUrl) {
        HttpHandlerModel returnmodel = new HttpHandlerModel();
        returnmodel.setConnectStatus(connectStatus);
        returnmodel.setJsonResponse(passdata);
        returnmodel.setLocalFileUrl(localstorageUrl.length > 0 ? localstorageUrl[0] : null);
        return returnmodel;
    }


}
