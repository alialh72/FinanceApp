package com.example.financeapp.Objects;

import com.example.financeapp.Enums.articlesCategoryEnum;

public class Article {

    private String title, author,description,para1, para2, para3, category, image;

    public Article(String title, String author, String description, String para1, String para2, String para3, articlesCategoryEnum category, String image){
        this.title = title;
        this.author = author;
        this.description = description;
        this.para1 = para1;
        this.para2 = para2;
        this.para3 = para3;
        this.category = category.getType();
        this.image = image;

    }

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



}
