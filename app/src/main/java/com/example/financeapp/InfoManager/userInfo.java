package com.example.financeapp.InfoManager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.financeapp.Enums.categoriesEnum;
import com.example.financeapp.MainActivity;
import com.example.financeapp.Objects.Transactions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class userInfo {

    private ArrayList<Transactions> transactions = new ArrayList<>();
    private String username = "Guest User";
    public static int accountID;
    public static boolean signedin = false;
    private static double accountBalance = 0;
    public categoriesEnum.MainCategories Categories;


    public ArrayList<Transactions> returnTransactions(){
        return transactions;
    }

    public String returnUsername(){
        return username;
    }


    public void addTransaction(categoriesEnum.SubCategories subCategory, String merchant, double value){
        categoriesEnum.MainCategories type;

        if (value < 0){
            type = Categories.EXPENSE;
        }
        else{
            type = Categories.INCOME;
        }


        Log.d(TAG, "addTransaction: date: " + MainActivity.date);

        Transactions newTransaction = new Transactions(MainActivity.date, type, subCategory, merchant, String.valueOf(value), "");

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



    public void setUser(int userid, Context mContext){
        accountID = userid;
        resetTransactions(); //deletes any stored transactions

        FirebaseDatabase database = FirebaseDatabase.getInstance(); //database connection
        DatabaseReference reference = database.getReference("Users");

        ((MainActivity)mContext).loadScreen();  //start load screen

        Log.d(TAG, "setUser: in here");

        //reads from a specific node in the database
        reference.child(String.valueOf(userid)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    if (task.getResult().child("Transactions").getValue() != null){
                        ArrayList<Object> node = (ArrayList<Object>) task.getResult().child("Transactions").getValue(); //stores all the transactions into an arraylist
                        username = (String) task.getResult().child("name").getValue();

                        //loops through all the transactions
                        for (int i = 0; i < node.size(); i++){
                            HashMap<String, String> transactionHashmap = (HashMap<String, String>) node.get(i); //breaks down each transaction into a hashmap

                            //gets all the values from the hashmap and assigns them to variables
                            String date = transactionHashmap.get("date");
                            String merchant = transactionHashmap.get("merchant");
                            categoriesEnum.SubCategories subCategory = categoriesEnum.SubCategories.LOOKUP.get(transactionHashmap.get("subCategoryLabel")); //converts the string into an enum using a hashmap defined in categoriesEnum
                            categoriesEnum.MainCategories type = categoriesEnum.MainCategories.valueOf(transactionHashmap.get("type").toUpperCase());
                            String value = transactionHashmap.get("value");
                            String id = transactionHashmap.get("id");

                            transactions.add(new Transactions(date, type, subCategory, merchant, value, id)); //adds the transaction to the public ArrayList
                        }

                        accountBalance = Double.parseDouble((String) task.getResult().child("balance").getValue()); //updates the accountbalance to the stored balance in the database
                    }
                    signedin = true; //sets the public boolean signedin to true

                    Log.d(TAG, "Username: "  + MainActivity.UserInfo.username);


                    ((MainActivity)mContext).refreshActivity(); //refresh mainactivity with the updated info
                    ((MainActivity)mContext).endLoadScreen();  //end loading screen
                }
            }
        });

    }

    public void resetTransactions(){
        transactions = new ArrayList<>();
    }

    public void removeTransaction(String id){
        //loops through the transactions ArrayList and compares each transactions id to the argument
        Integer position = null;
        for (int i = 0; i < transactions.size(); i++){
            if (transactions.get(i).getId().equals(id)){     //if the id matches
                reverseBalance(Double.parseDouble(transactions.get(i).getValue()));   //reverses the balance
                transactions.remove(i);  //deletes the transaction from the arraylist
                position = i;
            }
        }


        //if the user is signed in, we also have to delete it from firebase
        if(signedin = true && position != null){
            FirebaseDatabase database = FirebaseDatabase.getInstance(); //database connection
            DatabaseReference reference = database.getReference("Users").child(String.valueOf(accountID));

            reference.child("Transactions").child(String.valueOf((int) position)).removeValue(); //removes the transaction from the database
            reference.child("Transactions").setValue(transactions);  //reorders all the transactions in the database

        }

    }

    public void updateTransaction(Transactions newTransaction){
        for (int i = 0; i < transactions.size(); i++){
            if (transactions.get(i).getId().equals(newTransaction.getId())){
                transactions.set(i, newTransaction);

                if(signedin = true){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Users").child(String.valueOf(accountID));

                    reference.child("Transactions").child(String.valueOf(i)).setValue(newTransaction); //updates the transaction in the database
                }
            }
        }


    }

    public double returnBalance(){
        return accountBalance;
    }

    public void updateBalance(double value){
        accountBalance += value;
        Log.d(TAG, "updateBalance: New Account Balance: " + accountBalance);
    }

    public void reverseBalance(double value){
        accountBalance-= value;

        if(signedin = true){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Users");
            reference.child(String.valueOf(accountID)).child("balance").setValue(String.valueOf(accountBalance));
        }

    }

    public void setBalance(double balance){
        accountBalance = balance;

        if(signedin == true){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Users").child(String.valueOf(accountID));
            reference.child("balance").setValue(String.valueOf(balance));
        }
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
        }

        return runningtotal;
    }

    public double getTotalSpending() throws ParseException {
        double runningtotal = 0;
        for (Transactions t : transactions){
            if (t.getType().contains("Expense")){
                runningtotal += Math.abs(Double.parseDouble(t.getValue()));
            }
        }

        return runningtotal;
    }

    public double getMonthlyIncome(String date) throws ParseException {
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


    public double getValueBySubCategory(String category, String type, String date) throws ParseException {
        double runningtotal = 0;
        for (Transactions t : transactions){

            if(!(date.equals("----"))){

                SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
                DateFormat format2 = new SimpleDateFormat("MMM ''yy");
                String tDate = format2.format(format1.parse(t.getDate()));

                if (t.getSubCategoryLabel().equals(category) && t.getType().equals(type) && tDate.equals(date)){
                    runningtotal += Math.abs(Double.parseDouble(t.getValue()));
                }
            }

            else{
                if (t.getSubCategoryLabel().equals(category) && t.getType().equals(type)){
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
            String tDate = format2.format(format1.parse(t.getDate()));   //converts the transaction date to the required format

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
                String monthName = format2.format(format1.parse(t.getDate()));   //converts the transaction date to the required format

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

    public Double getCashFlowByDay(String date) throws ParseException {
        double runningTotal = 0;
        for (Transactions t : transactions){
            if (t.getDate().equals(date)){     //checks if the date matches
                runningTotal += Double.parseDouble(t.getValue());
            }
        }

        return runningTotal;
    }



}
