package com.example.financeapp.Fragments;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.financeapp.RecyclerViews.transactionRecyclerAdapter;
import com.example.financeapp.Slider.Item;
import com.example.financeapp.MainActivity;
import com.example.financeapp.R;
import com.example.financeapp.Slider.SliderAdapter;
import com.example.financeapp.Slider.SliderItem1;
import com.example.financeapp.Transactions;
import com.example.financeapp.categoriesEnum;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    //Textviews
    private TextView accountBalanceText;
    private TextView monthlySpendingText, monthlyIncomeText;
    private TextView monthName;
    private TextView youveSpent;
    private TextView helloUsername;
    private ImageView userButton, hamburger;

    private TextView startSpendingPie, transactionInfo;

    private RecyclerView transactionsRecyclerView;

    private PieChart spendingPieChart;

    public static Context context;




    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

                            MainActivity.UserInfo.setUser(Integer.parseInt((String) item.getTitle()));
                            ((MainActivity)getActivity()).loadScreen();
                            new CountDownTimer(2000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    // do something after 1s
                                }

                                @Override
                                public void onFinish() {
                                    refreshFragment();
                                }

                            }.start();

                        return false;
                    }
                });
                popupMenu.show();
                /*
                */


            }
        });

        /*hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.UserInfo.removeTransaction(MainActivity.UserInfo.transactions.size()-1);
                refreshFragment();
            }
        });*/


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
        hamburger = getView().findViewById(R.id.hamburger);
        helloUsername = getView().findViewById(R.id.helloUsername);
    }

    public void initText() throws ParseException {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("CAD"));

        accountBalanceText.setText(format.format(MainActivity.UserInfo.accountBalance));
        monthlySpendingText.setText(format.format(MainActivity.UserInfo.getTotalSpending(MainActivity.monthYear)));
        monthlyIncomeText.setText(format.format(MainActivity.UserInfo.getTotalIncome(MainActivity.monthYear)));



        //setting up month


        String month = MainActivity.month;
        Log.d(TAG, "initText: month: " + month);

        monthName.setText("Your "+month+ " Spending:");


        //you've spending
        youveSpent.setText("You've spent "+format.format(MainActivity.UserInfo.getTotalSpending(MainActivity.monthYear)) +" so far");

        if(MainActivity.UserInfo.transactions.size() == 0){
            startSpendingPie.setVisibility(View.VISIBLE);
            transactionInfo.setText("Start adding transactions to track your spending");
        }
        else{
            startSpendingPie.setVisibility(View.GONE);
            transactionInfo.setText("Recent Transactions");
        }

        helloUsername.setText(MainActivity.UserInfo.username);
    }


    private void setSlider(){
        List<Item> items = new ArrayList<>();

        items.add(new Item(0, new SliderItem1("Page 1", "Radical")));
        items.add(new Item(1, new SliderItem1("30", "itworked")));
        items.add(new Item(0, new SliderItem1("Amazing", "It means amazing")));

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
                Toast.makeText(getActivity(), label + ": "+format.format(e.getY()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        spendingPieChart.setNoDataText("Start adding transactions to see your spending");
    }

    private void loadRecyclerViews(){
        //get 5 most recent transactions
        Log.d(TAG, "initRecyclerView: init recyclerview locals");

        if (MainActivity.UserInfo.transactions.size() > 0){
            transactionsRecyclerView.setNestedScrollingEnabled(false); //stops the recyclerview from scrolling
            transactionRecyclerAdapter transactionsAdapter = new transactionRecyclerAdapter(getRecentTransactions(),"HomeFragment",getActivity());
            transactionsRecyclerView.setAdapter(transactionsAdapter);
            transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        }

    }

    private ArrayList<Transactions> getRecentTransactions(){
        ArrayList<Transactions> Localtransactions = new ArrayList<>();

        if(MainActivity.UserInfo.transactions.size() > 4){
            Log.d(TAG, "getRecentTransactions: size is greater than 4");
            for (int pos = MainActivity.UserInfo.transactions.size()-1; pos>MainActivity.UserInfo.transactions.size()-5;pos--){
                Log.d(TAG, "getRecentTransactions: pos: "+pos);
                Localtransactions.add(MainActivity.UserInfo.transactions.get(pos));
            }
        }
        else{
            int rev = MainActivity.UserInfo.transactions.size()-1;
            for (int pos = 0; pos < MainActivity.UserInfo.transactions.size(); pos++){
                Log.d(TAG, "getRecentTransactions: pos: "+pos);
                Localtransactions.add(MainActivity.UserInfo.transactions.get(rev));
                rev--;
            }
        }

        return Localtransactions;
    }

    public void refreshFragment(){
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }



}