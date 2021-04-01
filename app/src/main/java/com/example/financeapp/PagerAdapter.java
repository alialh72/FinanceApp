package com.example.financeapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

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
                fragment =  new spendingOvertimeFragment();
                break;

        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return numOfTabs;
    }
}
