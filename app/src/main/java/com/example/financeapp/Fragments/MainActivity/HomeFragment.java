package com.example.financeapp.Fragments.MainActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.financeapp.MainActivity;
import com.example.financeapp.Objects.Article;
import com.example.financeapp.Objects.Definition;
import com.example.financeapp.Objects.Insight;
import com.example.financeapp.Objects.Transactions;
import com.example.financeapp.R;
import com.example.financeapp.Slider.Item;
import com.example.financeapp.Slider.SliderAdapter;
import com.example.financeapp.ViewAdapters.transactionRecyclerAdapter;
import com.example.financeapp.Enums.categoriesEnum;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;
import static com.example.financeapp.MainActivity.EducationInfo;
import static com.example.financeapp.MainActivity.UserInfo;


public class HomeFragment extends Fragment{

    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    //Textviews
    private TextView accountBalanceText;
    private TextView monthlySpendingText, monthlyIncomeText;
    private TextView monthName;
    private TextView youveSpent;
    private TextView helloUsername;
    private ImageView userButton;

    private TextView startSpendingPie, transactionInfo;

    private RecyclerView transactionsRecyclerView;

    private PieChart spendingPieChart;

    public Context context;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        findViews();
        setSlider();


        try {
            initText();
            loadPieChartData();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        setupPieChart();
        loadRecyclerViews();


        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), userButton);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Log.d(TAG, "onMenuItemClick: clicked: "+ item);
                        int selectedUserId = Integer.parseInt((String) item.getTitle());

                        UserInfo.setUser(selectedUserId, getContext());

                        return false;
                    }
                });
                popupMenu.show();

            }
        });


    }



    private void findViews(){
        viewPager2 = getView().findViewById(R.id.viewpagerimage);
        spendingPieChart = getView().findViewById(R.id.monthlyspendingpiechart);
        accountBalanceText = getView().findViewById(R.id.accountBalanceText);
        monthlySpendingText = getView().findViewById(R.id.monthlySpendingText);
        monthlyIncomeText = getView().findViewById(R.id.monthlyIncomeText);
        monthName = getView().findViewById(R.id.monthSpendingText);
        youveSpent = getView().findViewById(R.id.youvespent);
        transactionsRecyclerView = getView().findViewById(R.id.transactionsRecyclerView);
        userButton = getView().findViewById(R.id.userButton);
        startSpendingPie = getView().findViewById(R.id.pieChartStartSpending);
        transactionInfo = getView().findViewById(R.id.transactionInfo);
        helloUsername = getView().findViewById(R.id.helloUsername);
    }

    public void initText() throws ParseException {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("CAD"));

        accountBalanceText.setText(format.format(UserInfo.returnBalance()));
        monthlySpendingText.setText(format.format(UserInfo.getMonthlySpending(MainActivity.monthYear)));
        monthlyIncomeText.setText(format.format(UserInfo.getMonthlyIncome(MainActivity.monthYear)));



        //setting up month


        String month = MainActivity.month;
        Log.d(TAG, "initText: month: " + month);

        monthName.setText("Your "+month+ " Spending:");


        //you've spending
        youveSpent.setText("You've spent "+format.format(UserInfo.getMonthlySpending(MainActivity.monthYear)) +" so far");

        if(UserInfo.returnTransactions().size() == 0){
            startSpendingPie.setVisibility(View.VISIBLE);
            spendingPieChart.setVisibility(View.GONE);
            transactionInfo.setText("Start adding transactions to track your spending");
        }
        else{
            startSpendingPie.setVisibility(View.GONE);
            transactionInfo.setText("Recent Transactions");
        }

        helloUsername.setText(UserInfo.returnUsername());
    }


    private void setSlider(){
        List<Item> items = new ArrayList<>();

        Random rand = new Random();
        Log.d(TAG, "setSlider: "+ EducationInfo.returnDefinitions().size());


        int randomPosDefinition = rand.nextInt(EducationInfo.returnDefinitions().size());
        int randomPosArticle = rand.nextInt(EducationInfo.returnArticles().size());

        Definition selectedDefinition = EducationInfo.returnDefinitions().get(randomPosDefinition);
        Article selectedArticle = EducationInfo.returnArticles().get(randomPosArticle);

        items.add(new Item(0, selectedDefinition));
        items.add(new Item(1, selectedArticle));
        items.add(new Item(2, new Insight("This do be your insight doe")));

        viewPager2.setAdapter(new SliderAdapter(items, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();

        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1-Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 7000);
            }
        });
    }

    private Runnable sliderRunnable = new Runnable(){
        @Override
        public void run(){
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }

    };

    public void setupPieChart(){
        spendingPieChart.setDrawHoleEnabled(true);
        spendingPieChart.setUsePercentValues(false);
        spendingPieChart.setDrawEntryLabels(false);
        spendingPieChart.getDescription().setEnabled(false);


        Legend l = spendingPieChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTextSize(15.f);
        l.setXEntrySpace(15.f);
        l.setYEntrySpace(3.f);
        l.setEnabled(true);
    }

    private void loadPieChartData() throws ParseException {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        ArrayList<String> second = new ArrayList<>();

        for (Transactions t : UserInfo.getSpendings(MainActivity.monthYear)){
            String c = t.getMainCategory();

            boolean isthere = false;
            for(PieEntry p : entries){
                if (p.getLabel().contains(c)){
                    isthere = true;
                }
            }


            if(isthere == false){
                double percentage = UserInfo.getValueByCategory(c, categoriesEnum.MainCategories.EXPENSE.getDisplayableType(), MainActivity.monthYear);
                DecimalFormat df = new DecimalFormat("#.##");
                df.format(percentage);
                second.add(c);
                entries.add(new PieEntry((float) percentage,c));
            }
            else{ }
        }

        Log.d(TAG, "loadPieChartData: colors: "+ MainActivity.colors);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("CAD"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(MainActivity.colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(false);

        spendingPieChart.setData(data);
        spendingPieChart.invalidate();

        spendingPieChart.animate();

        spendingPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //display msg when value
                int x = spendingPieChart.getData().getDataSet().getEntryIndex((PieEntry) e);
                String label = second.get(x);
                Snackbar.make(getView().findViewById(R.id.view), label + ": "+format.format(e.getY()), Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(getResources().getColor(R.color.mainGreen))
                        .show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        spendingPieChart.setNoDataText("Start adding transactions to see your spending");
    }

    private void loadRecyclerViews(){
        //get 4 most recent transactions
        Log.d(TAG, "initRecyclerView: init recyclerview locals");

        if (UserInfo.returnTransactions().size() > 0){
            transactionsRecyclerView.setNestedScrollingEnabled(false); //stops the recyclerview from scrolling
            transactionRecyclerAdapter transactionsAdapter = new transactionRecyclerAdapter(getRecentTransactions(),"HomeFragment",getActivity());
            transactionsRecyclerView.setAdapter(transactionsAdapter);
            transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        }

    }

    private ArrayList<Transactions> getRecentTransactions(){
        ArrayList<Transactions> dupSorted = new ArrayList<>(UserInfo.returnTransactions());
        Collections.sort(dupSorted, (c1, c2) -> {
            try {
                return new SimpleDateFormat("dd-MM-yyyy").parse(c2.getDate()).compareTo(new SimpleDateFormat("dd-MM-yyyy").parse(c1.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        });

        ArrayList<Transactions> recent = new ArrayList<>();
        recent.addAll(dupSorted.subList(0,4));
        return recent;
    }


}