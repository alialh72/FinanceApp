package com.example.financeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    private BottomNavigationView bottomNav;
    private NavController navController;
    private View decorView;
    private ConstraintLayout overlay;

    public static userInfo UserInfo = new userInfo(); //THE OG USERINFO OBJECT
    public static ArrayList<gradientColors> gradients = new ArrayList<>();
    public static HashMap<String, gradientColors> gradientsCategories = new HashMap<String, gradientColors>();

    public static boolean loaded = false;
    public static String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    public static String month;
    public static String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public static ArrayList<Integer> colors = new ArrayList<>();

    public categoriesEnum.MainCategories MainCategories;
    public categoriesEnum.SubCategory SubCategories; //categories enum

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideStatusBar();

        findViews();

        if(UserInfo.username == null){
            UserInfo.setUser(1);
            overlay.setVisibility(View.VISIBLE);
            new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // do something after 1s
                }

                @Override
                public void onFinish() {
                    overlay.setVisibility(View.GONE);
                }

            }.start();
        }
        else{

        }



        setupVars();


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");



        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNav, navController);
        NavigationUI.setupActionBarWithNavController(this, navController);



        //createSampleTransactions();
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
        overlay = findViewById(R.id.overlay);
    }


    private void createSampleTransactions(){
        UserInfo.updateBalance(32124.42); //Set starting balance

        UserInfo.addTransaction(categoriesEnum.SubCategory.FOOD, "McDonald's", -35.50);
        UserInfo.addTransaction(categoriesEnum.SubCategory.MOVIES, "Movies", -14.95);
        UserInfo.addTransaction(categoriesEnum.SubCategory.INCOME, "Salary", 4200.00);
        UserInfo.addTransaction(categoriesEnum.SubCategory.UTILITIES, "BC Hydro", -250.75);
        UserInfo.addTransaction(categoriesEnum.SubCategory.PETFOOD, "PetSmart", -55.50);
        UserInfo.addTransaction(categoriesEnum.SubCategory.GYM, "Planet Fitness", -40.52);
        UserInfo.addTransaction(categoriesEnum.SubCategory.INCOME, "Stimmy Check", 1400);
    }

    public void addTransaction(View view){
        Log.d(TAG, "AddButton: Total Spending: " + MainActivity.UserInfo.getTotalSpending());
        Log.d(TAG, "AddButton: Account Balance: " + MainActivity.UserInfo.accountBalance);


        Toast.makeText(this, "was clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, AddTransactionActivity.class);
        startActivity(intent);


    }

    public void tester(View view){
        MainActivity.UserInfo.addTransaction(categoriesEnum.SubCategory.PLANE, "Hawaii", -205.50);
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

        gradients.add(new gradientColors("#FEC180", "#FF8993"));
        gradients.add(new gradientColors("#D0FFAE", "#34EBE9"));
        gradients.add(new gradientColors("#A254F2", "#8005c8"));
        gradients.add(new gradientColors("#FFF175", "#FFDA83"));
        gradients.add(new gradientColors("#4777FF", "#3594E8"));
        gradients.add(new gradientColors("#D195FD", "#FE89E2"));
        gradients.add(new gradientColors("#55D8FE", "#47FFC2"));

        gradients.add(new gradientColors("#FEC180", "#FF8993"));
        gradients.add(new gradientColors("#D0FFAE", "#34EBE9"));
        gradients.add(new gradientColors("#A254F2", "#8005c8"));
        gradients.add(new gradientColors("#FFF175", "#FFDA83"));
        gradients.add(new gradientColors("#4777FF", "#3594E8"));
        gradients.add(new gradientColors("#D195FD", "#FE89E2"));

        int i = 0;
        for(categoriesEnum.MainCategories mainCategories : categoriesEnum.MainCategories.values()){
            gradientsCategories.put(mainCategories.getDisplayableType(), gradients.get(i));
            i++;
        }

    }

    public void marchSpendingClicked(View view){
        Toast.makeText(this, "was clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MonthlySpendingActivity.class);
        startActivity(intent);
    }

    public void userButton(View view){

    }

    public void clickedTransactionsContainer(View view){
        //transactions
        bottomNav.setSelectedItemId(R.id.transactionFragment);

    }
    
    public void OverlayClick(View view){
        Log.d(TAG, "OverlayClick: nothing");
    }

}