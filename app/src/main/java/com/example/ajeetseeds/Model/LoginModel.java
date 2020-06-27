package com.example.ajeetseeds.Model;
import java.util.ArrayList;
public class LoginModel {
    public String name;
    public String email;
    public String password;
    public String roleId;
    public String approver_id;
    public String user_type;
    public boolean condition;
    public String message;
    public ArrayList<MenuModel> menu;
    public String printerIP;
    public String printerPort;

    public class MenuModel {
        public String id;
        public String title;
        public String translate;
        public String type;
        public String icon;
        public ArrayList<MenuSubModel> children;
    }

    public class MenuSubModel {
        public String id;
        public String title;
        public String type;
        public String url;
    }
}
