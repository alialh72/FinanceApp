package com.example.financeapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DateFormat;
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

public class spendingOvertimeFragment extends Fragment {

    private BarChart barChart;
    private ArrayList<BarEntry> barEntries = new ArrayList<>();

    public spendingOvertimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spending_overtime, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findViews();
        try {
            loadBarChartData();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void findViews(){
        barChart = getView().findViewById(R.id.barchart);


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

                Toast.makeText(getActivity(), label+": "+format.format( e.getY()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }
}