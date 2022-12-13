package com.example.practice;

import android.text.TextUtils;
import android.widget.Toast;

public class TovarAddClass {
    public String id,nazvanie,description,fullprice,imgTovar, warranty,Category;

    public TovarAddClass() {

    }

    public TovarAddClass(String id, String nazvanie, String description, String fullprice,String imgTovar,String warranty,String Category) {
        this.id = id;
        this.nazvanie = nazvanie;
        this.description = description;
        this.fullprice = fullprice;
        this.imgTovar = imgTovar;
        this.warranty = warranty;
        this.Category = Category;
    }


    public TovarAddClass(String id, String nazvanie, String adress, String fullPrice, String dateTime, String status) {
    }
}
