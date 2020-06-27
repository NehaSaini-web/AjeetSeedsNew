package com.example.ajeetseeds.Http_Hanler;

public class HttpHandlerModel {
    private boolean connectStatus;
    private String jsonResponse;
    private String localFileUrl;

    public HttpHandlerModel() {
        this.setConnectStatus(false);
        this.setJsonResponse("");
    }
    public boolean isConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(boolean connectStatus) {
        this.connectStatus = connectStatus;
    }

    public String getJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(String jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    public String getLocalFileUrl() {
        return localFileUrl;
    }

    public void setLocalFileUrl(String localFileUrl) {
        this.localFileUrl = localFileUrl;
    }
}
