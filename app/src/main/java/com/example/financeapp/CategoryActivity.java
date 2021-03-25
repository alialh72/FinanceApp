package com.example.financeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategoryActivity extends AppCompatActivity {
    private View decorView;
    private ExpandableListView categoryExpandableList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

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
        categoryExpandableList = findViewById(R.id.expandable_list);
    }

    private int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN;
    }

    private void ExpandableAdapter(){
        ListMultimap<String, String> MainSubCategories = ArrayListMultimap.create();

        for(categoriesEnum.SubCategory sub : categoriesEnum.SubCategory.values()){
            for (categoriesEnum.MainCategories main : categoriesEnum.MainCategories.values()){
                if (sub.getDisplayableType() == main.getDisplayableType()){
                    MainSubCategories.put(main.getDisplayableType(), sub.getLabel());
                }
            }
        }

        List<String> enumParent = new ArrayList<>();
        HashMap<String, List<String>> enumChild = new HashMap<>();
        for(categoriesEnum.MainCategories main : categoriesEnum.MainCategories.values()){
            List<String> subs = MainSubCategories.get(main.getDisplayableType());
            enumChild.put(main.getDisplayableType(), subs);
            enumParent.add(main.getDisplayableType());
        }
        ExpandableListAdapter adapter = new ExpandableListAdapter(enumParent, enumChild, this);
        categoryExpandableList.setAdapter(adapter);
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