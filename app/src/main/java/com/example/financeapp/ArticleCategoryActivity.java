package com.example.financeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.financeapp.Objects.ArticleCategory;
import com.example.financeapp.ViewAdapters.miniArticleRecyclerAdapter;

import org.w3c.dom.Text;

import static com.example.financeapp.MainActivity.EducationInfo;

public class ArticleCategoryActivity extends AppCompatActivity {

    private static final String TAG = "ArticleCategoryActivity";
    private View decorView;

    private TextView categoryTitle;
    private RecyclerView articleRecycler;

    private ConstraintLayout backgroundConstraint;

    private ArticleCategory articleCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_category);

        Log.d(TAG, "onCreate: inside article category");

        Intent intent = getIntent();
        articleCategory = intent.getParcelableExtra("ARTICLECATEGORY");

        hideStatusBars();
        getSupportActionBar().hide();

        findViews();
        setText();
        setBackground();
        setupRecyclerView();
    }

    private void findViews(){
        categoryTitle = findViewById(R.id.categoryTitle);
        articleRecycler = findViewById(R.id.articlesRecycler);
        backgroundConstraint = findViewById(R.id.background);
    }

    private void hideStatusBars(){
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
    }

    private int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN;
    }

    private void setBackground(){
        GradientDrawable gd1 = new GradientDrawable(
                GradientDrawable.Orientation.BR_TL,
                new int[] {articleCategory.getColor(), articleCategory.getColor()});
        gd1.setCornerRadius(0f);

        backgroundConstraint.setBackgroundDrawable(gd1);
    }

    private void setText(){
        categoryTitle.setText(articleCategory.getCategory().getType());
    }

    private void setupRecyclerView(){
        articleRecycler.setNestedScrollingEnabled(false); //stops the recyclerview from scrolling
        articleRecycler.setAdapter(new miniArticleRecyclerAdapter(articleCategory.getArticles(),this, true));
        articleRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    public void GoBack(View view){
        finish();
    }

}