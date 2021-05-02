package com.example.financeapp.Fragments.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.financeapp.FilterActivity;
import com.example.financeapp.MainActivity;
import com.example.financeapp.Objects.Transactions;
import com.example.financeapp.R;
import com.example.financeapp.ViewAdapters.TransactionGroupAdapter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Currency;

import static com.example.financeapp.MainActivity.UserInfo;
import static android.content.ContentValues.TAG;

public class TransactionFragment extends Fragment{
    private ImageView userButton, modifyDropdown;
    private ConstraintLayout filterButton;

    private TextView accountBalanceText, helloUsername, resetFilters;
    private RecyclerView transactionsGroupRecycler;



    public TransactionFragment() {
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
        recyclerViewInit(UserInfo.returnTransactions(), false);


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

        //modify balance dropdown
        modifyDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), modifyDropdown);
                popupMenu.inflate(R.menu.modify_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //starts the alert dialog which allows the user to modify their balance
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
                                applyBalanceFragment(balance);  //calls the method which sets the balance
                            }
                        });

                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // what ever you want to do with No option.
                            }
                        });

                        alert.show();
                        return false;
                    }

                });
                popupMenu.show();

            }
        });


        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserInfo.returnTransactions().size() == 0){
                    Toast.makeText(getActivity(), "No transactions to filter", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getActivity(), FilterActivity.class); //calls the filterActivity
                    startActivityForResult(intent, 1);
                }
            }
        });

        resetFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewInit(UserInfo.returnTransactions(), false);  //resets the recyclerview
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    //there are 3 options
                    //1: a category and sortby has been chosen
                    //2: a sortby has been chosen
                    //3: a category has been chosen

                    if((data.getStringExtra("Category") != null) && (data.getStringExtra("Filter") != null)){
                        String category = data.getStringExtra("Category");
                        String filter = data.getStringExtra("Filter");

                        sortBy(filterByCategory(category), filter);;
                    }
                    else if(data.getStringExtra("Filter") != null){
                        String filter = data.getStringExtra("Filter");
                        sortBy(UserInfo.returnTransactions(), filter);
                    }
                    else if(data.getStringExtra("Category") != null){
                        String category = data.getStringExtra("Category");
                        Log.d(TAG, "onActivityResult: category: "+ category);
                        recyclerViewInit(filterByCategory(category), false);
                    }
                }
                break;
            }

        }
    }

    private ArrayList<Transactions> filterByCategory(String category){
        ArrayList<Transactions> filteredTransactions = new ArrayList<>();
        for (Transactions t : UserInfo.returnTransactions()){
            if(t.getMainCategory().equals(category)){
                filteredTransactions.add(t);
            }
        }
        Log.d(TAG, "filterByCategory: filtered: "+filteredTransactions);
        if (filteredTransactions.size() == 0){
            Toast.makeText(getActivity(), "No transactions match that category", Toast.LENGTH_SHORT).show();
        }
        return filteredTransactions;
    }

    private void sortBy(ArrayList<Transactions> transactions, String sort){
        ArrayList<Transactions> dupe = new ArrayList<>(transactions);
        if (sort.equals("Date: New to Old")){
            recyclerViewInit(dupe, false);
        }
        else if(sort.equals("Date: Old to New")){
            recyclerViewInit(dupe, true);
        }
        else{
            ArrayList<String> groupList = new ArrayList<>();
            ListMultimap<String, Transactions> childHashMap = ArrayListMultimap.create();

            groupList.add(sort);
            for (Transactions t : transactions){
                childHashMap.put(sort, t);
            }

            loadRecyclerViews(groupList, childHashMap, sort);
        }
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
        modifyDropdown = getView().findViewById(R.id.modify);
        helloUsername = getView().findViewById(R.id.helloUsername);
        accountBalanceText = getView().findViewById(R.id.accountBalanceText);
        userButton = getView().findViewById(R.id.userButton);
        transactionsGroupRecycler = getView().findViewById(R.id.transactionsGroupRecycler);
        filterButton = getView().findViewById(R.id.filterButton);
        resetFilters = getView().findViewById(R.id.resetFilters);
    }

    public void initText(){
        helloUsername.setText(MainActivity.UserInfo.returnUsername());


        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        format.setCurrency(Currency.getInstance("CAD"));

        accountBalanceText.setText(format.format(MainActivity.UserInfo.returnBalance()));
    }

    private void loadRecyclerViews(ArrayList<String> transactionsGroupList, ListMultimap<String, Transactions> childHashMap, String sortBy){
        Log.d(TAG, "initRecyclerView: init recyclerview locals");

        transactionsGroupRecycler.setNestedScrollingEnabled(false); //stops the recyclerview from scrolling
        TransactionGroupAdapter groupAdapter = new TransactionGroupAdapter(transactionsGroupList, childHashMap, "transactionFragment", sortBy,getContext());
        transactionsGroupRecycler.setAdapter(groupAdapter);
        transactionsGroupRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
    }

    private void recyclerViewInit(ArrayList<Transactions> transactions, boolean reversed) {
        ArrayList<Transactions> mainArray = new ArrayList<>(transactions);
        if (mainArray.size() > 0) {
            ArrayList<String> transactionsGroupList = new ArrayList<>();

            ListMultimap<String, Transactions> childHashMap = ArrayListMultimap.create();

            if (reversed == true){
                transactionsGroupList.add("All Time");
                transactionsGroupList.add("Past Year");
                transactionsGroupList.add("Past Month");
                transactionsGroupList.add("Past Week");
                transactionsGroupList.add("Today");
            }
            else{
                transactionsGroupList.add("Today");
                transactionsGroupList.add("Past Week");
                transactionsGroupList.add("Past Month");
                transactionsGroupList.add("Past Year");
                transactionsGroupList.add("All Time");
            }


            for (Transactions t : mainArray) {
                LocalDate currentDate = LocalDate.now();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate date = LocalDate.parse(t.getDate(), formatter);

                //checks what category the transaction would fit depending on its date
                if (date.isEqual(currentDate)) {
                    childHashMap.put("Today", t);
                } else if (date.isAfter(currentDate.minusWeeks(1)) && date.isBefore(currentDate)) {
                    childHashMap.put("Past Week", t);
                } else if (date.isAfter(currentDate.minusMonths(1)) && date.isBefore(currentDate)) {
                    childHashMap.put("Past Month", t);
                } else if (date.isAfter(currentDate.minusYears(1)) && date.isBefore(currentDate)) {
                    childHashMap.put("Past Year", t);
                } else {
                    childHashMap.put("All Time", t);
                }

            }

            loadRecyclerViews(transactionsGroupList, childHashMap, "date");
        }

    }

}