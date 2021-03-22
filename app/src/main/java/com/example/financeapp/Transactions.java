package com.example.financeapp;

public class Transactions {

    private String date;
    private categories category;
    private String merchant;
    private categories type;
    private double value;

    Transactions(String d, categories t, categories c, String m, double v){
        date = d;
        type = t;
        category = c;
        merchant = m;
        value = v;
    }

    public String getDate(){
        return date;
    }

    public String getCategory(){
        return category.toString();
    }

    public String getMerchant(){
        return merchant;
    }

    public String getType(){
        return type.toString();
    }

    public double getValue(){
        return value;
    }



}
