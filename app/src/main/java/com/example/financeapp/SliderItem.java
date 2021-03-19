package com.example.financeapp;

public class SliderItem {

    private String slidetext;
    private String description;

    SliderItem(String t, String z){
        description = z;
        slidetext = t;
    }

    public String getText(){
        return slidetext;
    }

    public String getDesc(){
        return description;
    }

}
