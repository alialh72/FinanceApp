package com.example.financeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.financeapp.Fragments.HomeFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;

import static android.content.ContentValues.TAG;

public class MonthlySpending extends AppCompatActivity {

    private PieChart pieChart;

    private View decorView;

    private TextView pageName;
    private TextView pieChartCenterText, pieChartValue, pieChartPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_spending);

        hideStatusBars();
        getSupportActionBar().hide();

        //findviews
        findViews();
        initText();

        //init piechart
        setupPieChart();
        loadPieChartData();
    }

    private void findViews(){
        pieChart = findViewById(R.id.monthlyspendingpiechart);
        pieChartCenterText = findViewById(R.id.piechartcentercat);
        pieChartValue = findViewById(R.id.money);
        pieChartPercentage = findViewById(R.id.percentage);
        pageName = findViewById(R.id.pagename);
    }

    private void initText(){
        String month = MainActivity.month;

        pageName.setText(month+" Spending");
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

    public void setupPieChart(){
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(75.f);
        pieChart.setTransparentCircleRadius(75.f);
        pieChart.setUsePercentValues(false);

        pieChart.setDrawEntryLabels(false);


        pieChart.getDescription().setEnabled(false);




        Legend l = pieChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTextSize(15.f);
        l.setXEntrySpace(15.f);
        l.setYEntrySpace(3.f);
        l.setEnabled(false);

    }

    private void loadPieChartData(){
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        ArrayList<String> second = new ArrayList<>();

        for (Transactions t : MainActivity.UserInfo.getSpendings()){
            String c = t.getCategory();

            boolean isthere = false;
            for(PieEntry p : entries){
                if (p.getLabel().contains(c)){
                    isthere = true;
                }
            }


            if(isthere == false){
                double percentage = MainActivity.UserInfo.getValueByCategory(c);
                DecimalFormat df = new DecimalFormat("#.##");
                df.format(percentage);
                second.add(c);
                entries.add(new PieEntry((float) percentage,c));
            }
            else{ }
        }



        Log.d(TAG, "loadPieChartData: colors: "+ MainActivity.colors);


        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(MainActivity.colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(false);

        pieChart.setData(data);
        pieChart.setNoDataText("Start adding transactions to see your spending");
        pieChart.invalidate();

        pieChart.animate();

        Entry starter = (Entry) entries.get(0);
        pieChart.highlightValue(0, 0, false);
        pieChartClicked(starter.getY(), entries.get(0).getLabel());

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //display msg when value
                int x = pieChart.getData().getDataSet().getEntryIndex((PieEntry) e);
                float value = e.getY();
                String label = second.get(x);
                pieChartClicked(value, label);
            }

            @Override
            public void onNothingSelected() {

            }
        });


    }

    private void pieChartClicked(float value, String label){
        pieChartCenterText.setText(label);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        format.setCurrency(Currency.getInstance("CAD"));

        pieChartValue.setText(format.format(value));

        double percentage = value/MainActivity.UserInfo.getTotalSpending();

        DecimalFormat df = new DecimalFormat("#%");
        pieChartPercentage.setText(df.format(percentage) + " of "+MainActivity.month+ " spending");


    }


}