package com.example.financeapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class userInfo {

    public static ArrayList<Transactions> transactions = new ArrayList<>();
    public static String username = "Guest User";
    public static int accountID;
    public static boolean signedin = false;
    public static double accountBalance = 0;
    public categoriesEnum.MainCategories Categories;
    public categoriesEnum.SubCategories SubCategories;


    public void addTransaction(categoriesEnum.SubCategories subCategory, String merchant, double value){
        categoriesEnum.MainCategories type;

        if (value < 0){
            type = Categories.EXPENSE;
        }
        else{
            type = Categories.INCOME;
        }


        Log.d(TAG, "addTransaction: date: " + MainActivity.date);

        Transactions newTransaction = new Transactions(MainActivity.date, type, subCategory, merchant, String.valueOf(value));

        transactions.add(newTransaction);

        updateBalance(value);

        if(signedin == true){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Users");

            reference.child(String.valueOf(accountID)).child("Transactions").setValue(transactions);
            reference.child(String.valueOf(accountID)).child("balance").setValue(String.valueOf(accountBalance));
        }


        Log.d(TAG, "addTransaction: Transactions: "+transactions);
    }

    public void setUser(int userid){
        transactions = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");
        Log.d(TAG, "setUser: in here");
        // Read from the database

        accountID = userid;

        reference.child(String.valueOf(userid)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    ArrayList<Object> node = (ArrayList<Object>) task.getResult().child("Transactions").getValue();
                    username = (String) task.getResult().child("name").getValue();
                    for (int i = 0; i < node.size(); i++){
                        HashMap<String, String> transactionhashmap = (HashMap<String, String>) node.get(i);
                        String date = transactionhashmap.get("date");
                        String merchant = transactionhashmap.get("merchant");
                        categoriesEnum.SubCategories subCategory = categoriesEnum.SubCategories.LOOKUP.get(transactionhashmap.get("subCategoryLabel"));
                        categoriesEnum.MainCategories type = categoriesEnum.MainCategories.valueOf(transactionhashmap.get("type").toUpperCase());
                        String value = transactionhashmap.get("value");

                        transactions.add(new Transactions(date, type, subCategory, merchant, value));
                    }
                    double b = Double.parseDouble((String) task.getResult().child("balance").getValue());
                    Log.d(TAG, "onComplete: num: "+ b);
                    setBalance(b);
                    Log.d(TAG, "onComplete: transactions: "+transactions);

                    signedin = true;
                }
            }
        });

    }


    public void removeTransaction(int position){
        double value = Double.parseDouble(transactions.get(position).getValue());

        transactions.remove(position);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");
        reference.child(String.valueOf(accountID)).child("Transactions").child(String.valueOf(position)).removeValue();

        reverseBalance(value);
    }

    public void updateBalance(double value){
        accountBalance += value;
        Log.d(TAG, "updateBalance: New Account Balance: " + accountBalance);
    }

    public void reverseBalance(double value){
        accountBalance-= value;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");
        reference.child(String.valueOf(accountID)).child("balance").setValue(String.valueOf(accountBalance));
    }

    public void setBalance(double balance){
        accountBalance = balance;
        //Log.d(TAG, "setBalance: " + String.valueOf(balance));
    }



    public double getMonthlySpending(String date) throws ParseException {
        double runningtotal = 0;
        for (Transactions t : transactions){
            SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
            DateFormat format2 = new SimpleDateFormat("MMM ''yy");
            String tDate = format2.format(format1.parse(t.getDate()));

            Log.d(TAG, "getMonthlySpending: tDate: "+ tDate);
            if (t.getType().contains("Expense") && tDate.equals(date)){
                runningtotal += Math.abs(Double.parseDouble(t.getValue()));
            }
            else{ }
        }

        return runningtotal;
    }

    public double getTotalSpending(String date) throws ParseException {
        double runningtotal = 0;
        for (Transactions t : transactions){
            SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
            DateFormat format2 = new SimpleDateFormat("MMM ''yy");
            String tDate = format2.format(format1.parse(t.getDate()));

            if (t.getType().contains("Expense") && tDate.equals(date)){
                runningtotal += Math.abs(Double.parseDouble(t.getValue()));
            }
            else{ }
        }

        return runningtotal;
    }

    public double getTotalIncome(String date) throws ParseException {
        double runningtotal = 0;
        for (Transactions t : transactions){
            SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
            DateFormat format2 = new SimpleDateFormat("MMM ''yy");
            String tDate = format2.format(format1.parse(t.getDate()));

            if (t.getType().contains("Income") && tDate.equals(date)){
                runningtotal += Double.parseDouble(t.getValue());
            }
            else{ }
        }

        return runningtotal;
    }

    public double getValueByCategory(String category, String type, String date) throws ParseException {

        double runningtotal = 0;
        for (Transactions t : transactions){

            if(!(date.equals("----"))){

                SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
                DateFormat format2 = new SimpleDateFormat("MMM ''yy");
                String tDate = format2.format(format1.parse(t.getDate()));

                if (t.getMainCategory().equals(category) && t.getType().equals(type) && tDate.equals(date)){
                    runningtotal += Math.abs(Double.parseDouble(t.getValue()));
                }
            }

            else{
                if (t.getMainCategory().equals(category) && t.getType().equals(type)){
                    runningtotal += Math.abs(Double.parseDouble(t.getValue()));
                }
            }

        }
        return runningtotal;
    }

    public ArrayList<Transactions> getSpendings(String date) throws ParseException {
        ArrayList<Transactions> spendings = new ArrayList<>();

        for (Transactions t : transactions){
            SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
            DateFormat format2 = new SimpleDateFormat("MMM ''yy");
            String tDate = format2.format(format1.parse(t.getDate()));

            if (Double.parseDouble(t.getValue()) < 0 && tDate.equals(date)){
                spendings.add(t);
            }
        }

        return spendings;
    }

    public ArrayList<Transactions> getTransactionsByCategory(String category, String type, String month) throws ParseException {
        ArrayList<Transactions> filteredlist = new ArrayList<>();

        for (Transactions t : transactions){

            if(!(month.equals("----"))){
                SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
                DateFormat format2 = new SimpleDateFormat("MMMM");
                String monthName = format2.format(format1.parse(t.getDate()));

                if (t.getMainCategory().equals(category) && t.getType().equals(type) && monthName.equals(month)){
                    filteredlist.add(t);
                }

            }

            else{
                if (t.getMainCategory().equals(category) && t.getType().equals(type)){
                    filteredlist.add(t);
                }
            }



        }

        return filteredlist;
    }



}
