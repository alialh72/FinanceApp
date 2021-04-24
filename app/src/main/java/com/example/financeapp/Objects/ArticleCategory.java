package com.example.financeapp.Objects;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.financeapp.Enums.articlesCategoryEnum;
import com.example.financeapp.Enums.categoriesEnum;
import com.example.financeapp.InfoManager.educationInfo;
import com.example.financeapp.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.example.financeapp.MainActivity.EducationInfo;

public class ArticleCategory implements Parcelable{

    private articlesCategoryEnum cat;
    private ArrayList<Article> articles = new ArrayList<>();
    private int color;

    public ArticleCategory(articlesCategoryEnum cat, int color){
        this.cat = cat;
        this.color = color;
        setArticles();
    }

    protected ArticleCategory(Parcel in){
        cat = (articlesCategoryEnum) in.readSerializable();
        articles = in.readArrayList(Article.class.getClassLoader());
        color = in.readInt();
    }

    public static final Parcelable.Creator<ArticleCategory> CREATOR = new Parcelable.Creator<ArticleCategory>() {
        @Override
        public ArticleCategory createFromParcel(Parcel in) {
            return new ArticleCategory(in);
        }

        @Override
        public ArticleCategory[] newArray(int size) {
            return new ArticleCategory[size];
        }
    };

    private void setArticles(){
        for(Article a : EducationInfo.returnArticles()){
            if(a.getCategory().equals(cat.getType())){
                articles.add(a);
            }
        }
    }

    public ArrayList<Article> getArticles(){
        return articles;
    }

    public articlesCategoryEnum getCategory(){
        return cat;
    }

    public int getColor(){
        return color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(cat);
        Log.d(TAG, "writeToParcel: here");
        dest.writeList(articles);
        dest.writeInt(color);
    }

}
