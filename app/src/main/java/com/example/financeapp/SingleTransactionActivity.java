package com.example.financeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.financeapp.Dialog.MerchantDialog;
import com.example.financeapp.Enums.categoriesEnum;
import com.example.financeapp.Objects.Transactions;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

public class SingleTransactionActivity extends AppCompatActivity implements MerchantDialog.MerchantDialogListener{

    private static final String TAG = "SingleTransactionActivity";
    private View decorView;
    private TextView topBarText;

    private TextView valueTitle, merchantTitle, dateTitle;
    private TextView valueChangeText, merchantChangeText, categoryChangeText, dateChangeText;


    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Transactions transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_transaction);

        Log.d(TAG, "onCreate: new activity");

        Intent intent = getIntent();
        transaction = intent.getParcelableExtra("TRANSACTION");
        Log.d(TAG, "onCreate: getIntent");

        hideStatusBars();
        getSupportActionBar().hide();

        //findviews
        findViews();
        try {
            initText();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "onDateSet: inside date");
                month = month + 1;
                SimpleDateFormat format2 = new SimpleDateFormat("d-M-yyyy");
                try {
                    SimpleDateFormat finalformat = new SimpleDateFormat("MMMM dd, yyyy");
                    String newDate = format1.format(format2.parse(dayOfMonth + "-"+month+"-"+year));
                    transaction.setDate(newDate);
                    MainActivity.UserInfo.updateTransaction(transaction);

                    dateChangeText.setText(finalformat.format(format2.parse(dayOfMonth + "-"+month+"-"+year)));
                    dateTitle.setText(finalformat.format(format2.parse(dayOfMonth + "-"+month+"-"+year)));

                    Toast.makeText(SingleTransactionActivity.this, "Transaction Updated", Toast.LENGTH_SHORT).show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };


    }

    private void findViews(){
        topBarText = findViewById(R.id.pagename);
        valueTitle = findViewById(R.id.value);
        merchantTitle = findViewById(R.id.merchant);
        dateTitle = findViewById(R.id.date);

        valueChangeText = findViewById(R.id.valueText);
        merchantChangeText = findViewById(R.id.merchantText);
        categoryChangeText = findViewById(R.id.categoryText);
        dateChangeText = findViewById(R.id.dateText);
    }

    private void initText() throws ParseException {
        topBarText.setText("Edit Transaction");

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        format.setCurrency(Currency.getInstance("CAD"));
        valueTitle.setText(format.format(Double.parseDouble(transaction.getValue())));
        merchantTitle.setText(transaction.getMerchant());

        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("MMMM dd, yyyy");
        Date date = format1.parse(transaction.getDate());

        dateTitle.setText(format2.format(date));


        valueChangeText.setText(format.format(Double.parseDouble(transaction.getValue())));
        merchantChangeText.setText(transaction.getMerchant());
        dateChangeText.setText(format2.format(date));
        categoryChangeText.setText(transaction.getSubCategoryLabel());
    }

    public void GoBack(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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


    public void MerchantOnClick(View view){
        MerchantDialog merchantDialog = new MerchantDialog();
        merchantDialog.show(getSupportFragmentManager(), "Merchant");

    }

    @Override
    public void applyTexts(String merchantName) {
        if(!merchantName.equals("")){
            merchantChangeText.setText(StringUtils.abbreviate(merchantName, 15));
            merchantTitle.setText(StringUtils.abbreviate(merchantName, 15));

            transaction.setMerchant(merchantName);
            MainActivity.UserInfo.updateTransaction(transaction);

            Toast.makeText(this, "Transaction Updated", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "applyTexts: merchantName: "+merchantName);
    }

    public void ValueOnClick(View view){
        Intent intent = new Intent(this, ValueActivity.class);
        startActivityForResult(intent, 2);
        Log.d(TAG, "CategoryOnClick: testttt");
    }

    public void CategoryOnClick(View view){
        Intent intent = new Intent(this, TransactionCategoryActivity.class);
        startActivityForResult(intent, 1);
        Log.d(TAG, "CategoryOnClick: testttt");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    categoryChangeText.setText(data.getStringExtra("Category"));
                    transaction.setCategory(categoriesEnum.SubCategories.LOOKUP.get(data.getStringExtra("Category")));
                    Log.d(TAG, "onActivityResult: transaction.getCategory: "+transaction.getSubCategoryLabel());
                    MainActivity.UserInfo.updateTransaction(transaction);
                    Toast.makeText(this, "Transaction Updated", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case(2): {
                if (resultCode == Activity.RESULT_OK) {
                    NumberFormat format = NumberFormat.getCurrencyInstance();
                    format.setMaximumFractionDigits(2);
                    format.setCurrency(Currency.getInstance("CAD"));
                    String formattedvalue = format.format(Double.parseDouble(data.getStringExtra("Value")));


                    valueChangeText.setText(formattedvalue);
                    valueTitle.setText(formattedvalue);
                    transaction.setValue(data.getStringExtra("Value"));
                    MainActivity.UserInfo.updateTransaction(transaction);
                    Toast.makeText(this, "Transaction Updated", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public void DateOnClick(View view) throws ParseException {
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("M");
        SimpleDateFormat dayFormat = new SimpleDateFormat("d");

        int year = Integer.parseInt(yearFormat.format(format1.parse(transaction.getDate())));
        int month = Integer.parseInt(monthFormat.format(format1.parse(transaction.getDate()))) - 1;
        int day = Integer.parseInt(dayFormat.format(format1.parse(transaction.getDate())));



        DatePickerDialog dialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();


    }

    public void deleteTransaction(View view){
        MainActivity.UserInfo.removeTransaction(transaction.getId());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}