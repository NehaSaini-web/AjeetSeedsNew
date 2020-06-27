package com.example.ajeetseeds.Model;

import org.json.JSONArray;
import org.json.JSONObject;

public class AsyModelArray {
    private String postingUrl;
    private JSONArray postingJson;
    private String flagOfAction;
    public AsyModelArray(String postingUrl, JSONArray postingJson, String flagOfAction){
        this.postingUrl=postingUrl;
        this.postingJson=postingJson;
        this.flagOfAction=flagOfAction;
    }

    public String getPostingUrl() {
        return postingUrl;
    }

    public JSONArray getPostingJson() {
        return postingJson;
    }

    public String getFlagOfAction() {
        return flagOfAction;
    }
}
