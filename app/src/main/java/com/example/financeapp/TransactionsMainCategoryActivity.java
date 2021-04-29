package com.example.financeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.financeapp.Enums.categoriesEnum;
import com.example.financeapp.ViewAdapters.MainCategoryRecyclerAdapter;

import java.util.ArrayList;

public class TransactionsMainCategoryActivity extends AppCompatActivity {
    private View decorView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_main_category);

        hideStatusBars();
        getSupportActionBar().hide();
        findViews();
        ExpandableAdapter();
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

    private void findViews(){
        recyclerView = findViewById(R.id.recyclerView);
    }

    private int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN;
    }

    private void ExpandableAdapter(){
        ArrayList<String> enumParent = new ArrayList<>();
        for(categoriesEnum.MainCategories main : categoriesEnum.MainCategories.values()){
            enumParent.add(main.getDisplayableType());
        }
        MainCategoryRecyclerAdapter adapter = new MainCategoryRecyclerAdapter(enumParent,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    public void GoBack(View view){
        finish();
    }

    public void setCategoryText(String category){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("Category", category);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}