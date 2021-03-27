package com.example.financeapp;

public class Transactions {

    private String date;
    private String MainCategory;
    private categoriesEnum.MainCategories type;
    private String merchant;
    private categoriesEnum.SubCategories SubCategory;
    private String value;

    Transactions(String d, categoriesEnum.MainCategories t, categoriesEnum.SubCategories sc, String m, String v){
        date = d;
        type = t;
        SubCategory = sc;
        MainCategory = sc.getDisplayableType();

        merchant = m;
        value = v;
    }

    public String getDate(){
        return date;
    }

    public String getMainCategory(){
        return SubCategory.getDisplayableType();
    }

    public String getSubCategoryLabel(){ return SubCategory.getLabel(); }

    public categoriesEnum.SubCategories getSubCategory(){ return SubCategory; }

    public String getMerchant(){
        return merchant;
    }

    public String getType(){
        return type.getDisplayableType();
    }

    public String getValue(){ return value; }



}
