package com.example.financeapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.financeapp.Objects.Transactions;
import com.example.financeapp.ViewAdapters.transactionRecyclerAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;

import static android.content.ContentValues.TAG;

public class monthlySpendingFragment extends Fragment {

    private PieChart pieChart;

    private TextView pieChartCenterText, pieChartValue, pieChartPercentage;
    private TextView transactionCategoryText;
    private TextView helpfulTipsTextView;

    private RecyclerView transactionsRecyclerView;

    private ImageView arrowForward, arrowBackward;

    private ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

    private int highlightedPosition = 0;



    public monthlySpendingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monthly_spending, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: other");
        findViews();
        setupPieChart();
        try {
            loadPieChartData();
            helpfulTips();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        arrowForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (highlightedPosition == entries.size()-1){
                    highlightedPosition = 0;
                }
                else{
                    highlightedPosition += 1;
                }

                Entry starter = (Entry) entries.get(highlightedPosition);
                pieChart.highlightValue(highlightedPosition, 0, false);
                try { pieChartClicked(starter.getY(), entries.get(highlightedPosition).getLabel()); }
                catch (ParseException e) { e.printStackTrace(); }
            }
        });

        arrowBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (highlightedPosition == 0){
                    highlightedPosition = entries.size()-1;
                }
                else{
                    highlightedPosition -= 1;
                }

                Entry starter = (Entry) entries.get(highlightedPosition);
                pieChart.highlightValue(highlightedPosition, 0, false);
                try { pieChartClicked(starter.getY(), entries.get(highlightedPosition).getLabel()); }
                catch (ParseException e) { e.printStackTrace(); }
            }
        });

    }

    private void findViews(){
        pieChart = getView().findViewById(R.id.monthlyspendingpiechart);
        pieChartCenterText = getView().findViewById(R.id.piechartcentercat);
        pieChartValue = getView().findViewById(R.id.money);
        pieChartPercentage = getView().findViewById(R.id.percentage);
        transactionCategoryText = getView().findViewById(R.id.transactionCategory);
        transactionsRecyclerView = getView().findViewById(R.id.transactionsRecyclerView);
        helpfulTipsTextView = getView().findViewById(R.id.helpfultips);
        arrowBackward = getView().findViewById(R.id.arrowback);
        arrowForward = getView().findViewById(R.id.arrowforward);
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

    private void loadRecyclerViewForCategory(String category) throws ParseException {
        //get 5 most recent transactions
        Log.d(TAG, "initRecyclerView: init recyclerview locals");
        transactionsRecyclerView.setNestedScrollingEnabled(false); //stops the recyclerview from scrolling

        ArrayList<Transactions> reversed = new ArrayList<>(MainActivity.UserInfo.getTransactionsByCategory(category, categoriesEnum.MainCategories.EXPENSE.getDisplayableType(), MainActivity.month));
        Collections.reverse(reversed);
        transactionRecyclerAdapter transactionsAdapter = new transactionRecyclerAdapter(reversed, "MonthlySpendingActivity",getContext());
        transactionsRecyclerView.setAdapter(transactionsAdapter);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }


    public void helpfulTips() throws ParseException {

        for (Transactions t : MainActivity.UserInfo.getSpendings(MainActivity.monthYear)){
            String c = t.getMainCategory();

            if(MainActivity.UserInfo.getValueBySubCategory("Savings", "Expense", MainActivity.monthYear) < (MainActivity.UserInfo.getTotalIncome(MainActivity.monthYear) * 0.20)){
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setMaximumFractionDigits(2);
                format.setCurrency(Currency.getInstance("CAD"));
                helpfulTipsTextView.setText("We've noticed that you haven't been saving enough this month. Aim to save around 20% of your total income each month, which is: "+ format.format(MainActivity.UserInfo.getTotalIncome(MainActivity.monthYear) * 0.20));
            }
            else if(MainActivity.UserInfo.getValueByCategory(c, "Expense", MainActivity.monthYear) > (0.6 * MainActivity.UserInfo.getTotalSpending(MainActivity.monthYear))){
                if (c != "Expense" && c != "Other" && c != "Income"){
                    helpfulTipsTextView.setText("We've noticed that you've spent a lot on "+c+" this month, consider cutting back on your spending.");
                }

            }
        }

    }
}