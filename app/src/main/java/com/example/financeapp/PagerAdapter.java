package com.example.financeapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.financeapp.Fragments.SpendingOverviewActivity.monthlySpendingFragment;
import com.example.financeapp.Fragments.SpendingOverviewActivity.spendingStatisticsFragment;

public class PagerAdapter extends FragmentStateAdapter {

    private static final String TAG ="" ;
    private int numOfTabs;

    public PagerAdapter(FragmentActivity fa, int numOfTabs){
        super(fa);
        this.numOfTabs = numOfTabs;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new monthlySpendingFragment();
                break;
            case 1:
                fragment =  new spendingStatisticsFragment();
                break;

        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return numOfTabs;
    }
}
