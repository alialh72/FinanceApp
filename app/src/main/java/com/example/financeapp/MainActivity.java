package com.example.financeapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.financeapp.Enums.categoriesEnum;
import com.example.financeapp.InfoManager.educationInfo;
import com.example.financeapp.InfoManager.userInfo;
import com.example.financeapp.Objects.gradientColors;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";

    private BottomNavigationView bottomNav;
    private NavController navController;
    private View decorView;
    private ConstraintLayout overlay;
    private DrawerLayout drawerLayout;
    private TextView drawerUsername;

    public static userInfo UserInfo = new userInfo(); //THE OG USERINFO OBJECT
    public static educationInfo EducationInfo = new educationInfo();

    public static ArrayList<gradientColors> gradients = new ArrayList<>();
    public static HashMap<String, gradientColors> gradientsCategories = new HashMap<String, gradientColors>();

    public static String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    public static String month;
    public static String monthYear;

    public static ArrayList<Integer> colors = new ArrayList<>();

    public Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(EducationInfo.returnArticles().size() == 0){
            EducationInfo.readArticlesText(this);
            EducationInfo.readDefinitionsText(this);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        hideStatusBar();
        findViews();
        setupVars();

        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNav, navController);
        NavigationUI.setupActionBarWithNavController(this, navController);
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
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerUsername = findViewById(R.id.drawerUsername);
        bottomNav = findViewById(R.id.bottomNavigationView);
        overlay = findViewById(R.id.overlay);
    }


    //Floating action button, when clicked it opens up the addtransactions activity
    public void addTransaction(View view){
        Intent intent = new Intent(this, AddTransactionActivity.class);
        startActivity(intent);
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
            colors.add(Color.parseColor("#FE89E2"));
            colors.add(Color.parseColor("#3594E8"));
            colors.add(Color.parseColor("#47FFC2"));
            colors.add(Color.parseColor("#FFDA83"));
            colors.add(Color.parseColor("#8005c8"));
        }


        //setup month
        DateFormat dateFormat = new SimpleDateFormat("MMMM");
        Date date = new Date();
        Log.d("Month",dateFormat.format(date));

        month = dateFormat.format(date);

        DateFormat dateFormat2 = new SimpleDateFormat("MMM ''yy");
        monthYear = dateFormat2.format(date);

        //gradients
        if(gradients.size() == 0){
            //put all the hexes into a list
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

            //assign a gradient to a MainCategory
            int i = 0;
            for(categoriesEnum.MainCategories mainCategories : categoriesEnum.MainCategories.values()){
                gradientsCategories.put(mainCategories.getDisplayableType(), gradients.get(i));
                i++;
            }
        }


        setDrawerUsername();
    }

    //Monthly Spending Page Clicked
    public void monthlySpendingClicked(View view) throws ParseException {
        openSpendingOverview();
    }

    //Starts the loading animation
    public void loadScreen(){
        overlay.setVisibility(View.VISIBLE);
    }

    //ends the loading animation
    public void endLoadScreen(){
        overlay.setVisibility(View.GONE);
    }

    public void refreshActivity(){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }


    //HomeFragment --> When transactions container is clicked
    public void clickedTransactionsContainer(View view){
        bottomNav.setSelectedItemId(R.id.transactionFragment);
    }

    //Hamburger menu icon
    public void ClickMenu(View view){ drawerLayout.openDrawer(GravityCompat.START); }
    
    public void OverlayClick(View view){
        Log.d(TAG, "OverlayClick: nothing");
    }

    //DrawerLayout
    //Whenever a textview is clicked, it starts the respective fragment/activity
    public void NavEducation(View view){
        bottomNav.setSelectedItemId(R.id.educationFragment);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void NavTransactions(View view){
        bottomNav.setSelectedItemId(R.id.transactionFragment);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void NavHome(View view){
        bottomNav.setSelectedItemId(R.id.homeFragment);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void NavSpendings(View view) throws ParseException {
        openSpendingOverview();
    }

    private void openSpendingOverview() throws ParseException {
        if (UserInfo.getMonthlySpending(monthYear) > 0){
            Intent intent = new Intent(this, SpendingOverviewActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Start adding transactions to see your spending", Toast.LENGTH_SHORT).show();
        }
    }

    //Update the username in the drawer
    public void setDrawerUsername(){
        Log.d(TAG, "setDrawerUsername: Drawer Username: "  + MainActivity.UserInfo.returnUsername());
        drawerUsername.setText(MainActivity.UserInfo.returnUsername());
    }

    public void openSearch(View view){
        Intent intent = new Intent(this, EducationSearchActivity.class);
        this.startActivity(intent);
    }


}