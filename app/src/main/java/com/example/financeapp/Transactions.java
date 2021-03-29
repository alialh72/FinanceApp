package com.example.financeapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.concurrent.ThreadLocalRandom;

import static android.content.ContentValues.TAG;

public class Transactions implements Parcelable {

    private String date;
    private String MainCategory;
    private String typeLabel;
    private String merchant;
    private String SubCategoryLabel;
    private categoriesEnum.SubCategories SubCategory;
    private String value;
    private String transactionId;

    Transactions(String d, categoriesEnum.MainCategories t, categoriesEnum.SubCategories sc, String m, String v, String id){
        date = d;
        typeLabel = t.getDisplayableType();
        SubCategory = sc;
        MainCategory = sc.getDisplayableType();
        SubCategoryLabel = sc.getLabel();
        merchant = m;
        value = v;

        if (!id.equals("")){
            transactionId = id;
        }
        else{
            transactionId = String.valueOf(ThreadLocalRandom.current().nextInt(1000000));
        }

    }

    protected Transactions(Parcel in) {
        date = in.readString();
        transactionId = in.readString();
        SubCategory = (categoriesEnum.SubCategories) in.readSerializable();
        typeLabel = in.readString();
        MainCategory = in.readString();
        SubCategoryLabel = in.readString();
        merchant = in.readString();
        value = in.readString();
    }

    public static final Creator<Transactions> CREATOR = new Creator<Transactions>() {
        @Override
        public Transactions createFromParcel(Parcel in) {
            return new Transactions(in);
        }

        @Override
        public Transactions[] newArray(int size) {
            return new Transactions[size];
        }
    };

    public String getDate(){
        return date;
    }

    public String getMainCategory(){
        return SubCategory.getDisplayableType();
    }

    public String getSubCategoryLabel(){ return SubCategoryLabel; }

    public categoriesEnum.SubCategories getSubCategory(){ return SubCategory; }

    public String getMerchant(){
        return merchant;
    }

    public String getType(){
        Log.d(TAG, "getType: type: "+typeLabel);
        return typeLabel;
    }

    public String getValue(){ return value; }

    public String getId() {return transactionId;}

    public void setCategory(categoriesEnum.SubCategories subCategory){
        MainCategory = subCategory.getDisplayableType();
        SubCategory = subCategory;
        SubCategoryLabel = subCategory.getLabel();
    }

    public void setMerchant(String merchant){
        this.merchant = merchant;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setValue(String value){
        this.value = value;

        if (value.contains("-")){
            typeLabel = "Expense";
        }
        else{
            typeLabel = "Income";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(transactionId);
        dest.writeSerializable(SubCategory);
        dest.writeString(typeLabel);
        dest.writeString(MainCategory);
        dest.writeString(SubCategoryLabel);
        dest.writeString(merchant);
        dest.writeString(value);
    }
}
