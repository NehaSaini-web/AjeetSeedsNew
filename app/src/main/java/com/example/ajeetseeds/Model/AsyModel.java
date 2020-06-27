package com.example.ajeetseeds.Model;

import org.json.JSONObject;

public class AsyModel {
    private String postingUrl;
    private JSONObject postingJson;
    private String flagOfAction;
    public AsyModel(String postingUrl,JSONObject postingJson,String flagOfAction){
        this.postingUrl=postingUrl;
        this.postingJson=postingJson;
        this.flagOfAction=flagOfAction;
    }

    public String getPostingUrl() {
        return postingUrl;
    }

    public JSONObject getPostingJson() {
        return postingJson;
    }

    public String getFlagOfAction() {
        return flagOfAction;
    }
}
