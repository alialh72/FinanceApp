package com.example.financeapp;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class userInfo {

    public static ArrayList<Transactions> transactions = new ArrayList<>();
    public static double accountBalance = 0;
    public categoriesEnum.MainCategories Categories;
    public categoriesEnum.SubCategory SubCategories;


    public void addTransaction(categoriesEnum.SubCategory subCategory, String merchant, double value){
        categoriesEnum.MainCategories type;

        if (value < 0){
            type = Categories.EXPENSE;
        }
        else{
            type = Categories.INCOME;
        }


        Log.d(TAG, "addTransaction: date: " + MainActivity.date);
        transactions.add(new Transactions(MainActivity.date, type, subCategory, merchant, value));

        updateBalance(value);

        Log.d(TAG, "addTransaction: Transactions: "+transactions);
    }

    public void removeTransaction(int position){
        transactions.remove(position);
    }

    public void updateBalance(double value){
        accountBalance += value;
        Log.d(TAG, "updateBalance: New Account Balance: " + accountBalance);
    }

    public double getTotalSpending(){
        double runningtotal = 0;
        for (Transactions t : transactions){
            if (t.getType().contains("Expense")){
                runningtotal += Math.abs(t.getValue());
            }
            else{ }
        }

        return runningtotal;
    }

    public double getTotalIncome(){
        double runningtotal = 0;
        for (Transactions t : transactions){
            if (t.getType().contains("Income")){
                runningtotal += t.getValue();
            }
            else{ }
        }

        return runningtotal;
    }

    public double getValueByCategory(String category){
        double runningtotal = 0;
        for (Transactions t : transactions){
            if (t.getMainCategory().contains(category)){
                runningtotal += Math.abs(t.getValue());
            }
        }
        return runningtotal;
    }

    public ArrayList<Transactions> getSpendings(){
        ArrayList<Transactions> spendings = new ArrayList<>();

        for (Transactions t : transactions){
            if (t.getValue() < 0){
                spendings.add(t);
            }
        }

        return spendings;
    }


}
