package com.example.financeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.financeapp.Enums.categoriesEnum;
import com.example.financeapp.ViewAdapters.ExpandableListAdapter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private View decorView;
    private ExpandableListView categoryExpandableList;

    private TextView topBarText;

    private ScrollView scrollView;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private Boolean categorySelected = false;
    private String selectedFilter = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        hideStatusBars();
        getSupportActionBar().hide();
        findViews();
        ExpandableAdapter();

        topBarText.setText("Filters");
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
        radioGroup = findViewById(R.id.radioGroup);
        scrollView = findViewById(R.id.scrollView);
        categoryExpandableList = findViewById(R.id.expandable_list);
        topBarText = findViewById(R.id.pagename);
    }

    private int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN;
    }

    private void ExpandableAdapter(){
        ListMultimap<String, String> MainSubCategories = ArrayListMultimap.create();

        for(categoriesEnum.SubCategories sub : categoriesEnum.SubCategories.values()){
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
        ExpandableListAdapter adapter = new ExpandableListAdapter(enumParent, enumChild,"filter", this);
        categoryExpandableList.setAdapter(adapter);
    }


    public void selectedCategory(String category){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("Category", category);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);

        categorySelected = true;
        selectedFilter = (String) radioButton.getText();
        Toast.makeText(this, "Selected Filter: " + radioButton.getText(), Toast.LENGTH_SHORT).show();
    }

    public void GoBack(View view){
        if(categorySelected == true){
            Intent resultIntent = new Intent();
            resultIntent.putExtra("Filter", selectedFilter);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
        else{
            finish();
        }

    }

}