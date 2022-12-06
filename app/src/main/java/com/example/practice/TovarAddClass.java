package com.example.practice;

import android.text.TextUtils;
import android.widget.Toast;

public class TovarAddClass {
    public String id,nazvanie,description,fullprice,imgTovar;

    public TovarAddClass() {

    }

    public TovarAddClass(String id, String nazvanie, String description, String fullprice,String imgTovar) {
        this.id = id;
        this.nazvanie = nazvanie;
        this.description = description;
        this.fullprice = fullprice;
        this.imgTovar = imgTovar;
    }
}
