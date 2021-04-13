package com.example.financeapp.Fragments.MainActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputFilter;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.financeapp.MainActivity;
import com.example.financeapp.Objects.Transactions;
import com.example.financeapp.R;
import com.example.financeapp.ViewAdapters.RecyclerGroupAdapter;
import com.example.financeapp.ViewAdapters.transactionRecyclerAdapter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class transactionFragment extends Fragment{

    private Button modifyButton;

    private ImageView userButton;
    private ConstraintLayout filterButton;

    private TextView accountBalanceText, helloUsername;
    private RecyclerView transactionsGroupRecycler;

    private LinearLayoutManager layoutManager;


    public transactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findViews();
        initText();
        loadRecyclerViews(MainActivity.UserInfo.returnTransactions());


        //demo user button
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), userButton); //starts a popup to allow the selection of a user
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Log.d(TAG, "onMenuItemClick: clicked: "+ item);
                        int selectedUserId = Integer.parseInt((String) item.getTitle()); //stores the userid, either 1 or 2

                        MainActivity.UserInfo.setUser(selectedUserId, getContext()); //calls a method in the class userInfo that fetches data from firebase

                        return false;
                    }
                });
                popupMenu.show();

            }
        });

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                final EditText edittext = new EditText(getContext());
                alert.setTitle("Set Balance");

                FrameLayout container = new FrameLayout(getContext());
                FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                edittext.setLayoutParams(params);
                container.addView(edittext);


                edittext.setFilters(new InputFilter[] {new InputFilter.LengthFilter(13)});
                edittext.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                alert.setView(container);


                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        double balance = Double.parseDouble(edittext.getText().toString());
                        applyBalanceFragment(balance);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();
            }
        });


        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void applyBalanceFragment(double balance){
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        format.setCurrency(Currency.getInstance("CAD"));

        if(balance != 0){
            accountBalanceText.setText(format.format(balance));

            MainActivity.UserInfo.setBalance(balance);

            Toast.makeText(getActivity(), "Balance Updated", Toast.LENGTH_SHORT).show();
        }
    }


    private void findViews(){
        helloUsername = getView().findViewById(R.id.helloUsername);
        modifyButton = getView().findViewById(R.id.modifyBalance);
        accountBalanceText = getView().findViewById(R.id.accountBalanceText);
        userButton = getView().findViewById(R.id.userButton);
        transactionsGroupRecycler = getView().findViewById(R.id.transactionsGroupRecycler);
        filterButton = getView().findViewById(R.id.filterButton);
    }

    public void initText(){
        helloUsername.setText(MainActivity.UserInfo.returnUsername());


        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        format.setCurrency(Currency.getInstance("CAD"));

        accountBalanceText.setText(format.format(MainActivity.UserInfo.returnBalance()));
    }

    private void loadRecyclerViews(ArrayList<Transactions> transactions){
        Log.d(TAG, "initRecyclerView: init recyclerview locals");

        if (transactions.size() > 0){
            ArrayList<String> transactionsGroupList = new ArrayList<>();

            ListMultimap<String, Transactions> childHashMap = ArrayListMultimap.create();

            transactionsGroupList.add("Today");
            transactionsGroupList.add("Past Week");
            transactionsGroupList.add("Past Month");
            transactionsGroupList.add("Past Year");
            transactionsGroupList.add("All Time");

            for(Transactions t : transactions){
                LocalDate currentDate = LocalDate.now();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate date = LocalDate.parse(t.getDate(), formatter);

                //checks what category the transaction would fit depending on its date
                if(date.isEqual(currentDate)){ childHashMap.put("Today", t); }
                else if(date.isAfter(currentDate.minusWeeks(1)) && date.isBefore(currentDate)){ childHashMap.put("Past Week", t); }
                else if(date.isAfter(currentDate.minusMonths(1)) && date.isBefore(currentDate)){ childHashMap.put("Past Month", t); }
                else if(date.isAfter(currentDate.minusYears(1)) && date.isBefore(currentDate)){ childHashMap.put("Past Year", t); }
                else{ childHashMap.put("All Time", t); }

            }


            transactionsGroupRecycler.setNestedScrollingEnabled(false); //stops the recyclerview from scrolling
            RecyclerGroupAdapter groupAdapter = new RecyclerGroupAdapter(getActivity(), transactionsGroupList, childHashMap, "transactionFragment", getContext());
            transactionsGroupRecycler.setAdapter(groupAdapter);
            transactionsGroupRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        }






    }






}