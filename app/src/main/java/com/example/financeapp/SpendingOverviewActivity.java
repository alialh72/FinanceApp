package com.example.financeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.financeapp.Objects.Transactions;
import com.example.financeapp.ViewAdapters.transactionRecyclerAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SpendingOverviewActivity extends AppCompatActivity {

    private PieChart pieChart;
    private BarChart barChart;


    private View decorView;

    private TextView pageName;
    private TextView pieChartCenterText, pieChartValue, pieChartPercentage;
    private TextView transactionCategoryText;
    private TextView helpfulTipsTextView;

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private TabItem monthlySpending, spendingOvertime;

    private RecyclerView transactionsRecyclerView;

    private ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
    private ArrayList<BarEntry> barEntries = new ArrayList<>();

    private int highlightedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending_overview);

        hideStatusBars();
        getSupportActionBar().hide();

        //findviews
        findViews();

        PagerAdapter pagerAdapter = new PagerAdapter(this,
                tabLayout.getTabCount());

        viewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected: tab: "+tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        initText();
    }

    private void findViews(){
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        monthlySpending = findViewById(R.id.monthSpendingTabItem);
        spendingOvertime = findViewById(R.id.spendingTabItem);
        pageName = findViewById(R.id.pagename);
    }

    private void initText(){
        pageName.setText("Spending Overview");
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