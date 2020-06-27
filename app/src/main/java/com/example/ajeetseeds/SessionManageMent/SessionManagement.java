package com.example.ajeetseeds.SessionManageMent;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sp;

    public SessionManagement(Context context) {
        sp = context.getSharedPreferences("ajeetseeds", Context.MODE_PRIVATE);
    }

    //todo set sessison
    public void setUserName(String userName) {
        setSharedPreferences("userName", userName);
    }

    public void setUserEmail(String userEmail) {
        setSharedPreferences("userEmail", userEmail);
    }

    public void setRoleId(String roleId) {
        setSharedPreferences("roleId", roleId);
    }

    public void setApprover_id(String approver_id) {
        setSharedPreferences("approver_id", approver_id);
    }

    public void setMenu(String menu) {
        setSharedPreferences("menu", menu);
    }

    public void setPrinterIpAdddress(String ipaddress) {
        setSharedPreferences("printerIpAdddress", ipaddress);
    }

    public void setPrinterPort(String port) {
        setSharedPreferences("printerPort", port);
    }

    public String getPrinterIpAdddress() {
        return getDataFromSharedPreferences("printerIpAdddress");
    }

    public String getPrinterPort() {
        return getDataFromSharedPreferences("printerPort");
    }

    //todo scaneddata
    public String getScanConsolidationBarcodeResponse() {
        return getDataFromSharedPreferences("scanConsolidationBarcodeResponse");
    }

    public String getTraySortSession() {
        return getDataFromSharedPreferences("traySort");
    }

    //todo scanData of Tray sorting
    public void setTraySortSession(String menu) {
        setSharedPreferences("traySort", menu);
    }

    //todo set barcodescanSlot
    public void setScanConsolidationBarcodeResponse(String responseBarcodescan) {
        setSharedPreferences("scanConsolidationBarcodeResponse", responseBarcodescan);
    }

    public String getUser_type() {
        return getDataFromSharedPreferences("user_type");
    }
    //todo set sessison

    public void setUser_type(String user_type) {
        setSharedPreferences("user_type", user_type);
    }
    public void setModelOfTravel(String ModelOfTravel) {
        setSharedPreferences("ModelOfTravel", ModelOfTravel);
    }
    public String getUserName() {
        return getDataFromSharedPreferences("userName");
    }

    public String getUserEmail() {
        return getDataFromSharedPreferences("userEmail");
    }

    public String getRoleId() {
        return getDataFromSharedPreferences("roleId");
    }

    public String getApprover_id() {
        return getDataFromSharedPreferences("approver_id");
    }


    public String getMenu() {
        return getDataFromSharedPreferences("menu");
    }
    public String getModelOfTravel() {
        return getDataFromSharedPreferences("ModelOfTravel");
    }


    private String getDataFromSharedPreferences(String Key) {
        try {
            return sp.getString(Key, null);
        } catch (Exception e) {
            return "";
        }
    }

    private void setSharedPreferences(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void ClearSession() {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
