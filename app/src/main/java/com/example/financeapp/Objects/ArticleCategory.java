package com.example.financeapp.Objects;

import com.example.financeapp.Enums.articlesCategoryEnum;

public class ArticleCategory {

    private articlesCategoryEnum cat;
    private int color;

    public ArticleCategory(articlesCategoryEnum cat, int color){
        this.cat = cat;
        this.color = color;
    }

    public articlesCategoryEnum getCategory(){
        return cat;
    }

    public int getColor(){
        return color;
    }

}
