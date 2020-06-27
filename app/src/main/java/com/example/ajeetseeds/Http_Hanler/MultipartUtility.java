package com.example.ajeetseeds.Http_Hanler;

import androidx.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.UUID;

public class MultipartUtility {
    private HttpURLConnection urlConnection;
    private final String LINE_FEED = "\r\n";
    private String boundary;
    private DataOutputStream printout;

    public MultipartUtility(String requestURL) throws Exception {
        boundary = UUID.randomUUID().toString(); //todo if boundry not pass
        URL url = new URL(requestURL);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setUseCaches(false);
        urlConnection.setDoOutput(true);// indicates POST method
        urlConnection.setDoInput(true);
        urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        urlConnection.connect();
        printout = new DataOutputStream(urlConnection.getOutputStream());
    }
    public void addFormFieldJSONArray(String name, JSONArray value) throws Exception {
        printout.writeBytes("--" + boundary + LINE_FEED);
        printout.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + LINE_FEED + LINE_FEED);
        printout.writeBytes(value + LINE_FEED);
    }
    public void addFormField(String name, String value) throws Exception {
        printout.writeBytes("--" + boundary + LINE_FEED);
        printout.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + LINE_FEED + LINE_FEED);
        printout.writeBytes(value + LINE_FEED);
    }

    public void addFilePart(String fieldName, File uploadFile) throws Exception {
        printout.writeBytes("--" + boundary + LINE_FEED);
        printout.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + uploadFile.getName() + "\"" + LINE_FEED + LINE_FEED);
        printout.write(FileUtils.readFileToByteArray(uploadFile));
        printout.writeBytes(LINE_FEED);
    }

    public HttpHandlerModel finish() {
        try {
            printout.writeBytes("--" + boundary + "--" + LINE_FEED);
            printout.flush();
            printout.close();
            if (urlConnection.getResponseCode() == 200)
                return setReturnMessage(true, readFromStream(urlConnection.getInputStream()));
            else
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

    public static HttpHandlerModel setReturnMessage(boolean connectStatus, String passdata, @Nullable String... localstorageUrl) {
        HttpHandlerModel returnmodel = new HttpHandlerModel();
        returnmodel.setConnectStatus(connectStatus);
        returnmodel.setJsonResponse(passdata);
        returnmodel.setLocalFileUrl(localstorageUrl.length > 0 ? localstorageUrl[0] : null);
        return returnmodel;
    }


}
