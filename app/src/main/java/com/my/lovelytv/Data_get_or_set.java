package com.my.lovelytv;

import java.util.ArrayList;

public class Data_get_or_set {
    private String id,name,image,url,description,mytype;
    public Data_get_or_set() {
    }

    public Data_get_or_set(String id,String name,String image,String url,String description,String mytype,
                                     ArrayList<String> genre) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.url = url;
        this.description = description;
        this.mytype = mytype;
    }

    public String id() {
        return id;
    }

    public void id(String id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public void name(String name) {
        this.name = name;
    }

    public String image() {
        return image;
    }

    public void image(String image) {
        this.image = image;
    }

    public String url() {
        return url;
    }

    public void url(String url) {
        this.url = url;
    }

    public String description() {
        return description;
    }

    public void description(String description) {
        this.description = description;
    }

    public String mytype() {
        return mytype;
    }

    public void mytype(String mytype) {
        this.mytype = mytype;
    }
}
