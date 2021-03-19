package com.example.financeapp;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class userInfo {

    public static ArrayList<Transactions> transactions = new ArrayList<>();
    public static float accountBalance;
    public categories Categories;


    public void addTransaction(categories category, String seller, float value){
        categories type;

        if (value < 0){
            type = Categories.EXPENSE;
        }
        else{
            type = Categories.INCOME;
        }


        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        transactions.add(new Transactions(date, type,category, seller, value));
        Log.d(TAG, "addTransaction: Transactions: "+transactions);
    }

    public void removeTransaction(int position){
        transactions.remove(position);
    }

    public void getTotalSpending(){
        float runningtotal = 0;
        for (Transactions t : transactions){
            if (t.getType().contains("Expense")){
                runningtotal += Math.abs(t.getValue());
            }
            else{ }
        }
    }

    public void getTotalIncome(){
        float runningtotal = 0;
        for (Transactions t : transactions){
            if (t.getType().contains("Income")){
                runningtotal += Math.abs(t.getValue());
            }
            else{ }
        }
    }


}
