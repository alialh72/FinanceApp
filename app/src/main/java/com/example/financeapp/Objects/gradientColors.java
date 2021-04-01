package com.example.financeapp.Objects;

import android.graphics.Color;

public class gradientColors {

    private int hex1, hex2;

    public gradientColors(String hex1, String hex2){
        this.hex1 = Color.parseColor(hex1);
        this.hex2 = Color.parseColor(hex2);
    }

    public int getColor1(){
        return hex1;
    }
    public int getColor2() {return hex2;}



}
