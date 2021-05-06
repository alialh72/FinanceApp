package com.example.financeapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financeapp.Objects.Definition;
import com.example.financeapp.ViewAdapters.DefinitionGroupAdapter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.example.financeapp.MainActivity.EducationInfo;

public class GlossaryActivity extends AppCompatActivity {

    private static final String TAG = "DefinitionActivity";
    private RecyclerView definitionRecyclerView;

    private TextView topbar;

    View decorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glossary);

        hideStatusBars();
        getSupportActionBar().hide();
        findViews();
        recyclerViewInit(EducationInfo.returnDefinitions());

        topbar.setText("Glossary");
    }

    private void findViews(){
        definitionRecyclerView = findViewById(R.id.definitionGroupRecycler);
        topbar = findViewById(R.id.pagename);
    }

    private void recyclerViewInit(ArrayList<Definition> definitions) {
        ArrayList<Definition> mainArray = new ArrayList<>(definitions);
        if (mainArray.size() > 0) {
            ArrayList<String> definitionGroupList = new ArrayList<>();
            ListMultimap<String, Definition> childHashMap = ArrayListMultimap.create();

            for (Definition d : mainArray){
                String word = d.getWord();
                String firstletter = word.substring(0, 1);
                definitionGroupList.add(firstletter);
                childHashMap.put(firstletter, d);
            }

            //removes duplicates
            Set<String> set = new HashSet<>(definitionGroupList);
            definitionGroupList.clear();
            definitionGroupList.addAll(set);

            loadRecyclerViews(definitionGroupList, childHashMap);
        }

    }

    private void loadRecyclerViews(ArrayList<String> definitionGroupList, ListMultimap<String, Definition> childHashMap){
        Log.d(TAG, "initRecyclerView: init recyclerview locals");

        definitionRecyclerView.setNestedScrollingEnabled(false); //stops the recyclerview from scrolling
        DefinitionGroupAdapter groupAdapter = new DefinitionGroupAdapter(definitionGroupList, childHashMap, this);
        definitionRecyclerView.setAdapter(groupAdapter);
        definitionRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
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