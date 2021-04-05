package com.example.financeapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anychart.core.annotations.Line;
import com.example.financeapp.Objects.Transactions;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
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

public class spendingStatisticsFragment extends Fragment {

    private BarChart barChartOverTime;
    private ArrayList<BarEntry> barEntries = new ArrayList<>();

    private LineChart lineChartCashFlow;
    private ArrayList<Entry> yValueLineChart = new ArrayList<>();

    public spendingStatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spending_statistics, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findViews();
        try {
            loadSpendingOvertimeBarChart();
            loadCashFlowLineChart();
        } catch (ParseException e) {
            e.printStackTrace();
        }






    }

    private void findViews(){
        barChartOverTime = getView().findViewById(R.id.barchartOverTime);
        lineChartCashFlow = getView().findViewById(R.id.lineChartCashFlow);

    }

    private void loadSpendingOvertimeBarChart() throws ParseException {
        ArrayList<String> xAxisMonths = new ArrayList<>();

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
            xAxisMonths.add(monthDate.format(cal.getTime()));
            cal.add(Calendar.MONTH, -1);
        }

        Collections.reverse(xAxisMonths);

        for (int i = 0; i < 6; i++){
            barEntries.add(new BarEntry(i, (float) MainActivity.UserInfo.getMonthlySpending(xAxisMonths.get(i))));
        }



        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");


        barChartOverTime.setDrawBorders(false);
        barChartOverTime.setDrawValueAboveBar(false);
        barChartOverTime.getDescription().setText("");
        barChartOverTime.setScaleEnabled(false);

        barChartOverTime.getLegend().setEnabled(false);

        BarData barData = new BarData(barDataSet);
        barData.setDrawValues(false);
        barChartOverTime.setData(barData);

        XAxis xAxis = barChartOverTime.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisMonths));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(xAxisMonths.size());

        barChartOverTime.getAxisLeft().setDrawAxisLine(false);
        barChartOverTime.getAxisRight().setDrawAxisLine(false);

        barChartOverTime.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setMaximumFractionDigits(0);
                format.setCurrency(Currency.getInstance("CAD"));

                String label = xAxisMonths.get((int) e.getX());

                Toast.makeText(getActivity(), label+": "+format.format( e.getY()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void loadCashFlowLineChart() throws ParseException {
        lineChartCashFlow.setDragEnabled(true);
        lineChartCashFlow.setScaleEnabled(false);
        lineChartCashFlow.setDrawBorders(false);
        lineChartCashFlow.setDrawGridBackground(false);
        lineChartCashFlow.getDescription().setText("");

        lineChartCashFlow.getAxisLeft().setDrawAxisLine(false);
        lineChartCashFlow.getAxisRight().setDrawAxisLine(false);

        lineChartCashFlow.getLegend().setEnabled(false);



        // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Calendar start = Calendar.getInstance();
        start.set(Calendar.DAY_OF_MONTH,
                start.getActualMinimum(Calendar.DAY_OF_MONTH));

        Calendar end = Calendar.getInstance();
        end.set(Calendar.DAY_OF_MONTH,
                end.getActualMaximum(Calendar.DAY_OF_MONTH));

        ArrayList<String> xAxisDays = new ArrayList<>();

        int i = 0;
        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            String formatteddate = sdf.format(date);
            double value = MainActivity.UserInfo.getCashFlowByDay(formatteddate);

            SimpleDateFormat newformat = new SimpleDateFormat("dd-MM");
            yValueLineChart.add(new Entry(i, (float) value));
            xAxisDays.add(newformat.format(date));
            i++;
        }

        XAxis xAxis = lineChartCashFlow.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisDays));
        xAxis.setLabelCount(xAxisDays.size()/4);


        LineDataSet set1 = new LineDataSet(yValueLineChart, "Data Set");

        set1.setFillAlpha(110);
        set1.setCircleHoleRadius(0f);
        set1.setCircleRadius(2f);
        set1.setDrawValues(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(set1);
        lineChartCashFlow.setData(data);

        lineChartCashFlow.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setMaximumFractionDigits(0);
                format.setCurrency(Currency.getInstance("CAD"));

                String label = xAxisDays.get((int) e.getX());

                Toast.makeText(getActivity(), label+": "+format.format( e.getY()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

}