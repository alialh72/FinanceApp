package com.example.financeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.financeapp.Objects.Article;

public class ArticleActivity extends AppCompatActivity {

    private View decorView;
    private WebView webView;

    private TextView topBarText, titleTextView, descriptionTextView, paragraphTextView1, paragraphTextView2, paragraphTextView3;
    private ImageView articleImageView;

    private ImageView arrow;

    private Article article;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Intent intent = getIntent();
        article = intent.getParcelableExtra("ARTICLE");

        hideStatusBars();
        getSupportActionBar().hide();


        findViews();
        setInfo();

    }


    private void findViews(){
        topBarText = findViewById(R.id.pagename);
        titleTextView = findViewById(R.id.title);
        descriptionTextView = findViewById(R.id.description);
        paragraphTextView1 = findViewById(R.id.paragraph1);
        paragraphTextView2 = findViewById(R.id.paragraph2);
        paragraphTextView3 = findViewById(R.id.paragraph3);

        articleImageView = findViewById(R.id.image);

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

    private void setInfo(){
        topBarText.setText("");

        titleTextView.setText(article.getTitle());
        descriptionTextView.setText(article.getDescription());
        paragraphTextView1.setText(article.getPara1());
        paragraphTextView2.setText(article.getPara2());
        paragraphTextView3.setText(article.getPara3());

        Glide.with(this)
                .asBitmap()
                .load(article.getImage())
                .into(articleImageView);

    }

    public void GoBack(View view){
        finish();
    }

}