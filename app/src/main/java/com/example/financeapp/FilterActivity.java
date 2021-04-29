package com.example.financeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class FilterActivity extends AppCompatActivity {

    private View decorView;
    private ConstraintLayout selectCategory;

    private TextView topBarText, categorySelected;

    private ScrollView scrollView;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private boolean isSelected = false;
    private boolean isSelectedFilter = false;
    private boolean isSelectedCategory = false;

    private Context context = this;

    private String selectedFilter = "";
    private String selectedCategory = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        hideStatusBars();
        getSupportActionBar().hide();
        findViews();

        topBarText.setText("Filters");

        selectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TransactionsMainCategoryActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    selectedCategory = data.getStringExtra("Category");
                    categorySelected.setText("Category Selected: "+selectedCategory);
                    isSelected = true;
                    isSelectedCategory = true;
                }
                break;
            }

        }
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
        topBarText = findViewById(R.id.pagename);
        selectCategory = findViewById(R.id.selectCategory);
        categorySelected = findViewById(R.id.categorySelected);
    }

    private int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN;
    }


    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);

        isSelected = true;
        isSelectedFilter = true;
        selectedFilter = (String) radioButton.getText();
        Toast.makeText(this, "Selected Filter: " + radioButton.getText(), Toast.LENGTH_SHORT).show();
    }

    public void GoBack(View view){
        if(isSelected == true){
            Intent resultIntent = new Intent();
            if(isSelectedCategory == true){
                resultIntent.putExtra("Category", selectedCategory);
            }

            if(isSelectedFilter == true){
                resultIntent.putExtra("Filter", selectedFilter);
            }

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
        else{
            finish();
        }

    }

}