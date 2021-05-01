package com.example.financeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.financeapp.Objects.Article;
import com.example.financeapp.Objects.Definition;
import com.example.financeapp.ViewAdapters.SearchAdapter;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.financeapp.MainActivity.EducationInfo;

public class EducationSearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";
    View decorView;
    private RecyclerView searchRecycler;
    private EditText searchBox;

    private SearchAdapter LocalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_search);


        hideStatusBars();
        getSupportActionBar().hide();

        findViews();
        initRecyclerView();

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        //updates recyclerview everytime a character is typed into the edittext
        searchBox.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Toast.makeText(EducationSearchActivity.this, "pee", Toast.LENGTH_SHORT).show();

                    return true;
                }
                return false;
            }
        });

    }

    private void filter(String text){
        Log.d(TAG, "filter: text: " + text);
        ArrayList<Object> filteredList = new ArrayList<Object>();

        Log.d(TAG, "filter: educationarticles: "+EducationInfo.returnArticles().size());

        //filters through articles
        for (Article a : EducationInfo.returnArticles()){
            if(a.getTitle().toLowerCase().contains(text.toLowerCase()) || a.getCategory().toLowerCase().contains(text.toLowerCase())){
                Log.d(TAG, "filter: item: " + a.getTitle());
                filteredList.add(a);
            }
        }

        //filters through defintions
        for (Definition d : EducationInfo.returnDefinitions()){
            if (d.getWord().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(d);
            }
        }

        Log.d(TAG, "filter: FilteredList: " + filteredList.size());

        Set<Object> set = new HashSet<>(filteredList);
        filteredList.clear();
        filteredList.addAll(set);


        Log.d(TAG, "filter: duplicates removed: " + filteredList.size());

        LocalAdapter.filterList(filteredList);
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview locals");
        ArrayList<Object> empty = new ArrayList<Object>();

        LocalAdapter = new SearchAdapter(empty,this);
        Log.d(TAG, "initRecyclerView: adapter: "+LocalAdapter);
        searchRecycler.setAdapter(LocalAdapter);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }

    public void GoBack(View view){
        finish();
    }

    private void findViews(){
        searchRecycler = findViewById(R.id.searchRecycler);
        searchBox = findViewById(R.id.SearchBox);
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