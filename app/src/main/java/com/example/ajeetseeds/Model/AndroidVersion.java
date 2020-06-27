package com.example.ajeetseeds.Model;

import java.util.ArrayList;

public class AndroidVersion {
    public int id;
    public String type;
    public float version;
    public String url;
    public String version_name;
    public ArrayList<AVL2Model> avl2;

    public class AVL2Model {
        public String change_log;
    }
}

