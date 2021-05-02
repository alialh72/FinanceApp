package com.example.financeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.financeapp.Objects.Definition;
import com.example.financeapp.Objects.Transactions;
import com.example.financeapp.ViewAdapters.TransactionGroupAdapter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.content.ContentValues.TAG;

public class DefinitionActivity extends AppCompatActivity {

    private static final String TAG = "DefinitionActivity";
    View decorView;

    private TextView title, description;
    private TextView topbar;

    private Definition definition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definition);

        Intent intent = getIntent();
        definition = intent.getParcelableExtra("DEFINITION");

        hideStatusBars();
        getSupportActionBar().hide();

        findViews();
        setDefinitionText();

        topbar.setText("");
    }

    private void findViews(){
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        topbar = findViewById(R.id.pagename);
    }

    private void setDefinitionText(){
        title.setText(definition.getWord());
        description.setText(definition.getDescription());
    }



    public void GoBack(View view){
        finish();
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
}