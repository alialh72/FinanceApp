package com.example.financeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

public class AddTransactionActivity extends AppCompatActivity implements MerchantDialog.MerchantDialogListener {

    private static final String TAG = "";
    private View decorView;
    private TextView numberText;
    private TextView plusMinus;

    private TextView merchantText, categoryText;

    private Button incomeButton, expenseButton;
    private ConstraintLayout incomeLayout, expenseLayout;

    private Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button0, buttonDecimal;
    private ImageButton buttonBackspace;

    private categoriesEnum.MainCategories type = categoriesEnum.MainCategories.EXPENSE;
    private String numberString = "";

    private String merchantName = "Cash";
    public  String category = "Other";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        hideStatusBars();
        getSupportActionBar().hide();
        findViews();

        setCategoryText();



        //income and expense buttons
        Log.d(TAG, "onClick: type: "+type.getDisplayableType());
        incomeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == categoriesEnum.MainCategories.EXPENSE){
                    type = categoriesEnum.MainCategories.INCOME;
                    incomeLayout.setBackgroundResource(R.drawable.button_outlines_clicked);
                    expenseLayout.setBackgroundResource(R.drawable.button_outlines);
                    plusMinus.setText("+");
                    Log.d(TAG, "onClick: type: "+type.getDisplayableType());
                }
            }
        });

        expenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == categoriesEnum.MainCategories.INCOME){
                    type = categoriesEnum.MainCategories.EXPENSE;
                    expenseLayout.setBackgroundResource(R.drawable.button_outlines_clicked);
                    incomeLayout.setBackgroundResource(R.drawable.button_outlines);
                    plusMinus.setText("-");
                    Log.d(TAG, "onClick: type: "+type.getDisplayableType());
                }
            }
        });

        buttonDecimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!numberString.contains(".")){
                    if (numberString.equals("")){
                        numberString += "0.";
                    }
                    else{
                        numberString+=".";
                    }
                    numberText.setText(numberString);
                }
            }
        });

        buttonBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberString.length() > 0){
                    numberString = numberString.substring(0, numberString.length() - 1);
                    numberText.setText(numberString);
                }
            }
        });



        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(numberString.contains(".") && numberString.length() - numberString.indexOf(".") == 3){
                    Toast.makeText(AddTransactionActivity.this, "Only two decimal places", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(numberString.length() == 8){
                        Toast.makeText(AddTransactionActivity.this, "Too many digits", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (!numberString.equals("0")){
                            numberString += "0";
                        }


                        numberText.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(Double.parseDouble(numberString)));

                    }
                }



            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(numberString.contains(".") && numberString.length() - numberString.indexOf(".") == 3){
                    Toast.makeText(AddTransactionActivity.this, "Only two decimal places", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (numberString.length() == 8){
                        Toast.makeText(AddTransactionActivity.this, "Too many digits", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (numberString.equals("0")){
                            numberString = "1";
                        }
                        else{
                            numberString += "1";
                        }

                        numberText.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(Double.parseDouble(numberString)));
                    }
                }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(numberString.contains(".") && numberString.length() - numberString.indexOf(".") == 3){
                    Toast.makeText(AddTransactionActivity.this, "Only two decimal places", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (numberString.length() == 8){
                        Toast.makeText(AddTransactionActivity.this, "Too many digits", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (numberString.equals("0")){
                            numberString = "2";
                        }
                        else{
                            numberString += "2";
                        }
                        numberText.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(Double.parseDouble(numberString)));
                    }
                }


            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(numberString.contains(".") && numberString.length() - numberString.indexOf(".") == 3){
                    Toast.makeText(AddTransactionActivity.this, "Only two decimal places", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (numberString.length() == 8){
                        Toast.makeText(AddTransactionActivity.this, "Too many digits", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (numberString.equals("0")){
                            numberString = "3";
                        }
                        else{
                            numberString += "3";
                        }
                        numberText.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(Double.parseDouble(numberString)));
                    }
                }


            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(numberString.contains(".") && numberString.length() - numberString.indexOf(".") == 3){
                    Toast.makeText(AddTransactionActivity.this, "Only two decimal places", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (numberString.length() == 8){
                        Toast.makeText(AddTransactionActivity.this, "Too many digits", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (numberString.equals("0")){
                            numberString = "4";
                        }
                        else{
                            numberString += "4";
                        }
                        numberText.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(Double.parseDouble(numberString)));
                    }
                }

            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(numberString.contains(".") && numberString.length() - numberString.indexOf(".") == 3){
                    Toast.makeText(AddTransactionActivity.this, "Only two decimal places", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (numberString.length() == 8){
                        Toast.makeText(AddTransactionActivity.this, "Too many digits", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (numberString.equals("0")){
                            numberString = "5";
                        }
                        else{
                            numberString += "5";
                        }
                        numberText.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(Double.parseDouble(numberString)));
                    }
                }

            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(numberString.contains(".") && numberString.length() - numberString.indexOf(".") == 3){
                    Toast.makeText(AddTransactionActivity.this, "Only two decimal places", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (numberString.length() == 8){
                        Toast.makeText(AddTransactionActivity.this, "Too many digits", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (numberString.equals("0")){
                            numberString = "6";
                        }
                        else{
                            numberString += "6";
                        }
                        numberText.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(Double.parseDouble(numberString)));
                    }
                }

            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(numberString.contains(".") && numberString.length() - numberString.indexOf(".") == 3){
                    Toast.makeText(AddTransactionActivity.this, "Only two decimal places", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (numberString.length() == 8){
                        Toast.makeText(AddTransactionActivity.this, "Too many digits", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (numberString.equals("0")){
                            numberString = "7";
                        }
                        else{
                            numberString += "7";
                        }
                        numberText.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(Double.parseDouble(numberString)));
                    }
                }

            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(numberString.contains(".") && numberString.length() - numberString.indexOf(".") == 3){
                    Toast.makeText(AddTransactionActivity.this, "Only two decimal places", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (numberString.length() == 8){
                        Toast.makeText(AddTransactionActivity.this, "Too many digits", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (numberString.equals("0")){
                            numberString = "8";
                        }
                        else{
                            numberString += "8";
                        }
                        numberText.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(Double.parseDouble(numberString)));
                    }
                }

            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(numberString.contains(".") && numberString.length() - numberString.indexOf(".") == 3){
                    Toast.makeText(AddTransactionActivity.this, "Only two decimal places", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (numberString.length() == 8){
                        Toast.makeText(AddTransactionActivity.this, "Too many digits", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (numberString.equals("0")){
                            numberString = "9";
                        }
                        else{
                            numberString += "9";
                        }
                        numberText.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(Double.parseDouble(numberString)));
                    }
                }

            }
        });





    }


    private void findViews(){
        numberText = findViewById(R.id.numberfield);
        plusMinus = findViewById(R.id.plusminus);
        incomeLayout = findViewById(R.id.incomeConstraint);
        expenseLayout = findViewById(R.id.expenseConstraint);

        merchantText = findViewById(R.id.merchant);
        categoryText = findViewById(R.id.category);


        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        buttonBackspace = findViewById(R.id.buttonBackspace);
        buttonDecimal = findViewById(R.id.buttonDecimal);
    }


    private void hideStatusBars(){
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
    }

    private int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN;
    }

    public void GoBack(View view){
        finish();
    }

    public void selectMerchant(View view){
        openDialog();
    }

    public void selectCategory(View view){
        Intent intent = new Intent(this, CategoryActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    category = data.getStringExtra("Category");
                    setCategoryText();
                    // TODO Update your TextView.
                }
                break;
            }
        }
    }

    private void openDialog(){
        MerchantDialog merchantDialog = new MerchantDialog();
        merchantDialog.show(getSupportFragmentManager(), "Merchant");
    }

    public void setCategoryText(){
        categoryText.setText(category);
    }

    @Override
    public void applyTexts(String merchantName) {
        this.merchantName = merchantName;
        merchantText.setText(StringUtils.abbreviate(this.merchantName, 15));
        Log.d(TAG, "applyTexts: merchantName: "+this.merchantName);
    }


    public void AddTransaction(View view){
        double value;
        if(type.getDisplayableType() == "Income"){
            value = Double.parseDouble(numberString);
        }
        else{
            value = Double.parseDouble("-"+numberString);
        }

        if (!(value == 0)){
            categoriesEnum.SubCategory subCategory = categoriesEnum.SubCategory.LOOKUP.get(category);
            MainActivity.UserInfo.addTransaction(subCategory, merchantName, value);
            Log.d(TAG, "AddTransaction: Transactions: "+ MainActivity.UserInfo.transactions);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Transaction cannot be $0", Toast.LENGTH_SHORT).show();
        }
    }

}