package com.example.financeapp;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class userInfo {

    public static ArrayList<Transactions> transactions = new ArrayList<>();
    public static String username;
    public static boolean signedin;
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

    public void setUser(int userid){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        // Read from the database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<Object> value = (ArrayList<Object>) dataSnapshot.child("1").child("Transactions").getValue();
                Log.d(TAG, "Value is: " + value.get(0));

                username = (String) dataSnapshot.child(String.valueOf(userid)).child("name").getValue();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void setTransactions(){

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

    public ArrayList<Transactions> getTransactionsByCategory(String category){
        ArrayList<Transactions> filteredlist = new ArrayList<>();

        for (Transactions t : transactions){
            if (t.getMainCategory().equals(category)){
                filteredlist.add(t);
            }
        }

        return filteredlist;
    }


}
