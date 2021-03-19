package com.example.financeapp;

public class Transactions {

    private String date;
    private categories category;
    private String seller;
    private categories type;
    private float value;

    Transactions(String d, categories t, categories c, String s, float v){
        date = d;
        type = t;
        category = c;
        seller = s;
        value = v;
    }

    public String getDate(){
        return date;
    }

    public String getCategory(){
        return category.toString();
    }

    public String getSeller(){
        return seller;
    }

    public String getType(){
        return type.toString();
    }

    public float getValue(){
        return value;
    }





}
