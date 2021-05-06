package com.example.financeapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.financeapp.Objects.Article;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class ArticleActivity extends YouTubeBaseActivity {

    private View decorView;
    private YouTubePlayerView youTubePlayerView;

    private TextView topBarText, titleTextView, authorTextView, descriptionTextView, paragraphTextView1, paragraphTextView2, paragraphTextView3;
    private ImageView articleImageView, arrow;

    private Article article;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Intent intent = getIntent();
        article = intent.getParcelableExtra("ARTICLE");

        hideStatusBars();


        findViews();
        setInfo();

        arrow.setColorFilter(Color.WHITE);

        //setup webview
        YouTubePlayer.OnInitializedListener onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(article.getVideoToken());
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        youTubePlayerView.initialize(PlayerConfig.API_KEY, onInitializedListener);


    }


    private void findViews(){
        topBarText = findViewById(R.id.pagename);
        titleTextView = findViewById(R.id.title);
        authorTextView = findViewById(R.id.author);
        descriptionTextView = findViewById(R.id.description);
        paragraphTextView1 = findViewById(R.id.paragraph1);
        paragraphTextView2 = findViewById(R.id.paragraph2);
        paragraphTextView3 = findViewById(R.id.paragraph3);
        youTubePlayerView = findViewById(R.id.webView);
        articleImageView = findViewById(R.id.image);
        arrow = findViewById(R.id.arrow);

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
        authorTextView.setText("By "+article.getAuthor());
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}