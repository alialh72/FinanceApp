package com.example.financeapp.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.financeapp.Enums.articlesCategoryEnum;

public class Article implements Parcelable {

    private String title, author,description,para1, para2, para3, category, image, videoToken;

    public Article(String title, String author, String description, String para1, String para2, String para3, articlesCategoryEnum category, String image, String videoToken){
        this.title = title;
        this.author = author;
        this.description = description;
        this.para1 = para1;
        this.para2 = para2;
        this.para3 = para3;
        this.category = category.getType();
        this.image = image;
        this.videoToken = videoToken;

    }

    protected Article(Parcel in){
        title = in.readString();
        author = in.readString();
        description = in.readString();
        para1 = in.readString();
        para2 = in.readString();
        para3 = in.readString();
        category = in.readString();
        image = in.readString();
        videoToken = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public String getDescription(){
        return description;
    }

    public String getPara1(){
        return para1;
    }

    public String getPara2(){
        return para2;
    }

    public String getPara3(){
        return para3;
    }

    public String getCategory(){
        return category;
    }

    public String getImage(){
        return image;
    }

    public String getVideoToken(){ return videoToken; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(description);
        dest.writeString(para1);
        dest.writeString(para2);
        dest.writeString(para3);
        dest.writeString(category);
        dest.writeString(image);
        dest.writeString(videoToken);
    }
}
