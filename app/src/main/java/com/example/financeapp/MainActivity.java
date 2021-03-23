package com.example.financeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    private BottomNavigationView bottomNav;
    private NavController navController;
    private View decorView;

    public static userInfo UserInfo = new userInfo(); //THE OG USERINFO OBJECT

    public static String month;
    public static String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public static ArrayList<Integer> colors = new ArrayList<>();


    public categories Categories; //categories enum

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideStatusBar();

        findViews();

        setupVars();



        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNav, navController);
        NavigationUI.setupActionBarWithNavController(this, navController);



        createSampleTransactions();
    }

    private void hideStatusBar(){

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

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

    private void findViews(){
        bottomNav = findViewById(R.id.bottomNavigationView);
    }


    private void createSampleTransactions(){
        UserInfo.updateBalance(32124.42); //Set starting balance

        UserInfo.addTransaction(Categories.FOOD, "McDonald's", -35.50);
        UserInfo.addTransaction(Categories.ENTERTAINMENT, "Movies", -14.95);
        UserInfo.addTransaction(Categories.INCOME, "Salary", 4200.00);
        UserInfo.addTransaction(Categories.BILLS, "BC Hydro", -250.75);
        UserInfo.addTransaction(Categories.PETS, "PetSmart", -55.50);
        UserInfo.addTransaction(Categories.HEALTH, "Planet Urmom", -40.52);
        UserInfo.addTransaction(Categories.INCOME, "Stimmy Check", 1400);
    }

    public void AddButton(View view){
        Log.d(TAG, "AddButton: Total Spending: " + MainActivity.UserInfo.getTotalSpending());
        Log.d(TAG, "AddButton: Account Balance: " + MainActivity.UserInfo.accountBalance);
    }

    public void tester(View view){
        MainActivity.UserInfo.addTransaction(Categories.TRAVEL, "Hawaii", -205.50);
    }

    public void setupVars(){
        //setup colors list
        if (colors.size() == 0){
            colors.add(Color.parseColor("#A3A0FB"));
            colors.add(Color.parseColor("#55d8fe"));
            colors.add(Color.parseColor("#ffda83"));
            colors.add(Color.parseColor("#ff8373"));
            colors.add(Color.parseColor("#D195FD"));
            colors.add(Color.parseColor("#4777FF"));
            colors.add(Color.parseColor("#FFB575"));
        }


        //setup month
        DateFormat dateFormat = new SimpleDateFormat("M");
        Date date = new Date();
        Log.d("Month",dateFormat.format(date));

        int monthPosition = Integer.parseInt(dateFormat.format(date));

        month = months[monthPosition-1];


    }

    public void marchSpendingClicked(View view){
        Toast.makeText(this, "was clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MonthlySpending.class);
        startActivity(intent);
    }

}