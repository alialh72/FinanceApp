package com.example.financeapp.ViewAdapters;

import android.app.Activity;
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

public class RecyclerGroupAdapter extends RecyclerView.Adapter<RecyclerGroupAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<String> arrayListGroup;
    private ListMultimap<String, Transactions> childHashmap;
    private String className;
    private Context mContext;

    public RecyclerGroupAdapter(Activity activity, ArrayList<String> arrayListGroup, ListMultimap<String, Transactions> childHashmap, String className, Context mContext){
        this.activity = activity;
        this.arrayListGroup = arrayListGroup;
        this.childHashmap = childHashmap;
        this.className = className;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_group, parent, false);
        return new RecyclerGroupAdapter.ViewHolder(view);
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

        if(arrayListGroup.get(position).equals("Today")){
            Collections.reverse(childArrayList);   //this is because sorting doesnt account for same day transactions, so the best way to order them is just by reversing the list
        }
        else{
            sortBy("date", childArrayList);
        }


        Log.d(TAG, "onBindViewHolder: GroupHeader: "+arrayListGroup.get(position));
        Log.d(TAG, "onBindViewHolder: ChildArrayList: "+childArrayList);

        holder.childRecycler.setNestedScrollingEnabled(false); //stops the recyclerview from scrolling
        transactionRecyclerAdapter childRecyclerAdapter = new transactionRecyclerAdapter(childArrayList,className,mContext);
        holder.childRecycler.setAdapter(childRecyclerAdapter);
        holder.childRecycler.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
    }

    public void sortBy(String sortType, ArrayList<Transactions> unsortedTransactions){
        if (sortType == "date"){
            Collections.sort(unsortedTransactions, (c1, c2) -> {
                try {
                    return new SimpleDateFormat("dd-MM-yyyy").parse(c2.getDate()).compareTo(new SimpleDateFormat("dd-MM-yyyy").parse(c1.getDate()));
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
