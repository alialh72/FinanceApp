package com.example.financeapp.Slider;

public class SliderItem1 {

    private String slidetext;
    private String description;

    public SliderItem1(String t, String z){
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
