package com.example.financeapp.ViewAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financeapp.Objects.Transactions;
import com.example.financeapp.R;
import com.google.common.collect.ListMultimap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import static android.content.ContentValues.TAG;

public class TransactionGroupAdapter extends RecyclerView.Adapter<TransactionGroupAdapter.ViewHolder> {
    private ArrayList<String> arrayListGroup;
    private ListMultimap<String, Transactions> childHashmap;
    private String sortBy;
    private String className;
    private Context mContext;

    public TransactionGroupAdapter(ArrayList<String> arrayListGroup, ListMultimap<String, Transactions> childHashmap, String className, String sortBy, Context mContext){
        this.sortBy = sortBy;
        this.arrayListGroup = arrayListGroup;
        this.childHashmap = childHashmap;
        this.className = className;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Log.d(TAG, "onCreateViewHolder: viewtype:"+viewType);
        if (viewType == 0){
           view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.emptylayout, parent, false);  //if there is nothing under one of the headers then it returns an empty layout
        }
        else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_group, parent, false);
        }


        return new TransactionGroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.groupHeader.setText(arrayListGroup.get(position));

        //init members
        ArrayList<Transactions> childArrayList;

        if (childHashmap.get(arrayListGroup.get(position)) != null){
            childArrayList = new ArrayList<>(childHashmap.get(arrayListGroup.get(position)));
        }
        else{
            childArrayList = new ArrayList<>();
        }

        if (sortBy.equals("date")){
            if(arrayListGroup.get(position).equals("Today")){
                Collections.reverse(childArrayList);   //this is because sorting doesnt account for same day transactions, so the best way to order them is just by reverse the order they were added
            }
            else{
                sortBy("date", childArrayList);
            }
        }
        else{
            if(sortBy.equals("Value: Low to High")){
                childArrayList = new ArrayList<>(sortByValue(childArrayList, true));
            }
            else if(sortBy.equals("Value: High to Low")){
                childArrayList = new ArrayList<>(sortByValue(childArrayList, false));
            }
        }

        //sets the child recycler
        holder.childRecycler.setNestedScrollingEnabled(false); //stops the recyclerview from scrolling
        TransactionRecyclerAdapter childRecyclerAdapter = new TransactionRecyclerAdapter(childArrayList,className,mContext);
        holder.childRecycler.setAdapter(childRecyclerAdapter);
        holder.childRecycler.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
    }

    private ArrayList<Transactions> sortByValue(ArrayList<Transactions> transactions, boolean reversed){
        //if reversed is true, sorts by low to high, else sorts by high to low
        ArrayList<Transactions> sorted = new ArrayList<>(transactions);
        if(reversed == true){
            Collections.sort(sorted, (c1, c2) -> (int) Math.round(Double.parseDouble(c2.getValue()) - Double.parseDouble(c1.getValue()))); //comparator
        }
        else{
            Collections.sort(sorted, (c1, c2) -> (int) Math.round(Double.parseDouble(c1.getValue()) - Double.parseDouble(c2.getValue())));
        }
        return sorted;
    }

    public void sortBy(String sortType, ArrayList<Transactions> unsortedTransactions){
        if (sortType == "date"){
            Collections.sort(unsortedTransactions, (c1, c2) -> {
                try {
                    return new SimpleDateFormat("dd-MM-yyyy").parse(c2.getDate()).compareTo(new SimpleDateFormat("dd-MM-yyyy").parse(c1.getDate())); //compares for the most recent date
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayListGroup.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (childHashmap.get(arrayListGroup.get(position)).size() == 0){   //checks if the childlist is empty
            return 0;
        }
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView groupHeader;
        RecyclerView childRecycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupHeader = itemView.findViewById(R.id.groupHeader);
            childRecycler = itemView.findViewById(R.id.childRecycler);

        }
    }

}
