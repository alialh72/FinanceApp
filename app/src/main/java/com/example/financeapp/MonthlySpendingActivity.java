package com.example.financeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.core.gauge.pointers.Bar;
import com.example.financeapp.RecyclerViews.transactionRecyclerAdapter;
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
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.lang.reflect.Array;
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

public class MonthlySpendingActivity extends AppCompatActivity {

    private PieChart pieChart;
    private BarChart barChart;


    private View decorView;

    private TextView pageName;
    private TextView pieChartCenterText, pieChartValue, pieChartPercentage;
    private TextView transactionCategoryText;
    private TextView helpfulTipsTextView;

    private RecyclerView transactionsRecyclerView;

    private ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
    private ArrayList<BarEntry> barEntries = new ArrayList<>();

    private int highlightedPosition = 0;

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
        try {
            loadPieChartData();
            helpfulTips();
            loadBarChartData();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void findViews(){
        pieChart = findViewById(R.id.monthlyspendingpiechart);
        pieChartCenterText = findViewById(R.id.piechartcentercat);
        pieChartValue = findViewById(R.id.money);
        pieChartPercentage = findViewById(R.id.percentage);
        pageName = findViewById(R.id.pagename);
        transactionCategoryText = findViewById(R.id.transactionCategory);
        transactionsRecyclerView = findViewById(R.id.transactionsRecyclerView);
        barChart = findViewById(R.id.barchart);
        helpfulTipsTextView = findViewById(R.id.helpfultips);
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

    private void loadPieChartData() throws ParseException {
        ArrayList<String> second = new ArrayList<>();

        for (Transactions t : MainActivity.UserInfo.getSpendings(MainActivity.monthYear)){
            String c = t.getMainCategory();

            boolean isthere = false;
            for(PieEntry p : entries){
                if (p.getLabel().contains(c)){
                    isthere = true;
                }
            }


            if(isthere == false){
                double percentage = MainActivity.UserInfo.getValueByCategory(c, categoriesEnum.MainCategories.EXPENSE.getDisplayableType(), MainActivity.monthYear);
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

        Entry starter = (Entry) entries.get(highlightedPosition);
        pieChart.highlightValue(highlightedPosition, 0, false);
        pieChartClicked(starter.getY(), entries.get(highlightedPosition).getLabel());

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //display msg when value
                int x = pieChart.getData().getDataSet().getEntryIndex((PieEntry) e);
                float value = e.getY();
                String label = second.get(x);
                try {
                    pieChartClicked(value, label);
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void pieChartClicked(float value, String label) throws ParseException {
        pieChartCenterText.setText(label);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        format.setCurrency(Currency.getInstance("CAD"));

        pieChartValue.setText(format.format(value));

        double percentage = value/MainActivity.UserInfo.getTotalSpending(MainActivity.monthYear);

        DecimalFormat df = new DecimalFormat("#%");
        pieChartPercentage.setText(df.format(percentage) + " of "+MainActivity.month+ " spending");

        transactionCategoryText.setText(label+":");
        loadRecyclerViewForCategory(label);
    }

    private void loadBarChartData() throws ParseException {
        ArrayList<String> yAxisMonths = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("MMM ''yy");
        Date date = new Date();
        Log.d("Month",dateFormat.format(date));

        List<String> allDates = new ArrayList<>();
        String maxDate = dateFormat.format(date);
        SimpleDateFormat monthDate = new SimpleDateFormat("MMM ''yy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(monthDate.parse(maxDate));

        Log.d(TAG, "loadBarChartData: alldates: " + allDates);



        for (int i = 0; i < 6; i++){
            yAxisMonths.add(monthDate.format(cal.getTime()));
            cal.add(Calendar.MONTH, -1);
        }

        Collections.reverse(yAxisMonths);

        for (int i = 0; i < 6; i++){
            barEntries.add(new BarEntry(i, (float) MainActivity.UserInfo.getMonthlySpending(yAxisMonths.get(i))));
        }



        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");


        barChart.setDrawBorders(false);
        barChart.setDrawValueAboveBar(false);
        barChart.getDescription().setText("");
        barChart.setScaleEnabled(false);

        Legend l = barChart.getLegend();
        l.setEnabled(false);

        BarData barData = new BarData(barDataSet);
        barData.setDrawValues(false);
        barChart.setData(barData);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(yAxisMonths));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(yAxisMonths.size());
        barChart.getAxisLeft().setDrawGridLines(false);

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setMaximumFractionDigits(0);
                format.setCurrency(Currency.getInstance("CAD"));

                String label = yAxisMonths.get((int) e.getX());

                Toast.makeText(MonthlySpendingActivity.this, label+": "+format.format( e.getY()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }


    private void loadRecyclerViewForCategory(String category) throws ParseException {
        //get 5 most recent transactions
        Log.d(TAG, "initRecyclerView: init recyclerview locals");
        transactionsRecyclerView.setNestedScrollingEnabled(false); //stops the recyclerview from scrolling

        ArrayList<Transactions> reversed = new ArrayList<>(MainActivity.UserInfo.getTransactionsByCategory(category, categoriesEnum.MainCategories.EXPENSE.getDisplayableType(), MainActivity.month));
        Collections.reverse(reversed);
        transactionRecyclerAdapter transactionsAdapter = new transactionRecyclerAdapter(reversed, this);
        transactionsRecyclerView.setAdapter(transactionsAdapter);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    public void ArrowBack(View view) throws ParseException {
        if (highlightedPosition == 0){
            highlightedPosition = entries.size()-1;
        }
        else{
            highlightedPosition -= 1;
        }

        Entry starter = (Entry) entries.get(highlightedPosition);
        pieChart.highlightValue(highlightedPosition, 0, false);
        pieChartClicked(starter.getY(), entries.get(highlightedPosition).getLabel());
    }

    public void ArrowForward(View view) throws ParseException {
        if (highlightedPosition == entries.size()-1){
            highlightedPosition = 0;
        }
        else{
            highlightedPosition += 1;
        }

        Entry starter = (Entry) entries.get(highlightedPosition);
        pieChart.highlightValue(highlightedPosition, 0, false);
        pieChartClicked(starter.getY(), entries.get(highlightedPosition).getLabel());
    }


    public void helpfulTips() throws ParseException {

        for (Transactions t : MainActivity.UserInfo.getSpendings(MainActivity.monthYear)){
            String c = t.getMainCategory();

            if(MainActivity.UserInfo.getValueByCategory(c, "Expense", MainActivity.monthYear) > (0.5 * MainActivity.UserInfo.getTotalSpending(MainActivity.monthYear))){
                helpfulTipsTextView.setText("We've noticed that you've spent a lot on "+c+" this month, consider cutting back on your spending.");
            }
        }



    }


}