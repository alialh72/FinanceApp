package com.example.financeapp.InfoManager;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.financeapp.MainActivity;
import com.example.financeapp.Objects.Article;
import com.example.financeapp.Objects.ArticleCategory;
import com.example.financeapp.Objects.Definition;
import com.example.financeapp.Enums.articlesCategoryEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class educationInfo {

    private static ArrayList<Definition> definitions = new ArrayList<>();
    private static ArrayList<Article> articles = new ArrayList<>();
    private static ArrayList<ArticleCategory> articleCategories = new ArrayList<>();

    public educationInfo(){
    }

    public ArrayList<Definition> returnDefinitions(){
        return definitions;
    }

    public ArrayList<Article> returnArticles(){
        return articles;
    }

    public ArrayList<ArticleCategory> returnArticleCategories(){
        return articleCategories;
    }


    public void readDefinitionsText(Context context){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("definitions.txt")));

            String line = "";

            try {
                while ((line = reader.readLine()) != null) {
                    // Split the line into different tokens (using the comma as a separator).
                    String[] splitLine = line.split("\\*");

                    definitions.add(new Definition(splitLine[0], splitLine[1]));

                }
            } catch (IOException e1) {
                Log.d(TAG, "Error" + line, e1);
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readArticlesText(Context context){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("articles.txt")));

            String line = "";
            try {
                while ((line = reader.readLine()) != null) {
                    // Split the line into different tokens (using the comma as a separator).
                    String[] splitLine = line.split("\\*");

                    if(splitLine.length == 9){
                        //creates a new Article object and adds it to the list
                        articles.add(new Article(splitLine[0], splitLine[1],splitLine[2], splitLine[3], splitLine[4], splitLine[5], articlesCategoryEnum.LOOKUP.get(splitLine[6]), splitLine[7], splitLine[8]));
                    }

                }
            } catch (IOException e1) {
                Log.d(TAG, "Error" + line, e1);
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "readArticlesText: articles: "+articles);
        setArticleCategories();
    }

    public ArrayList<Article> getArticlesByCategory(articlesCategoryEnum category){
        //ArticleCategory contains all articles matching its category
        ArticleCategory aCat = getArticleCategoryByEnum(category); //gets the ArticleCategory object from the enum
        return aCat.getArticles(); //returns the articles
    }

    public ArticleCategory getArticleCategoryByEnum(articlesCategoryEnum category){
        for(ArticleCategory a : articleCategories){
            if(a.getCategory().getType().equals(category.getType())){
                return a;
            }
        }
        return null;
    }

    private void setArticleCategories() {
        articleCategories.add(new ArticleCategory(articlesCategoryEnum.TAXES, Color.parseColor("#FFDA83")));
        articleCategories.add(new ArticleCategory(articlesCategoryEnum.FINANCE, Color.parseColor("#FF8993")));
        articleCategories.add(new ArticleCategory(articlesCategoryEnum.SAVINGS, Color.parseColor("#D195FD")));
        articleCategories.add(new ArticleCategory(articlesCategoryEnum.INVESTING, Color.parseColor("#55D8FE")));
        articleCategories.add(new ArticleCategory(articlesCategoryEnum.ACCOUNTS, Color.parseColor("#FE89E2")));
        articleCategories.add(new ArticleCategory(articlesCategoryEnum.CRYPTO, Color.parseColor("#FFB575")));
    }




}
