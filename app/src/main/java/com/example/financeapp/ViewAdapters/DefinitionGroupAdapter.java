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

import com.example.financeapp.Objects.Definition;
import com.example.financeapp.Objects.Transactions;
import com.example.financeapp.R;
import com.google.common.collect.ListMultimap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import static android.content.ContentValues.TAG;

public class DefinitionGroupAdapter extends RecyclerView.Adapter<DefinitionGroupAdapter.ViewHolder> {
    private ArrayList<String> arrayListGroup;
    private ListMultimap<String, Definition> childHashmap;
    private Context mContext;

    public DefinitionGroupAdapter(ArrayList<String> arrayListGroup, ListMultimap<String, Definition> childHashmap, Context mContext){
        this.arrayListGroup = arrayListGroup;
        this.childHashmap = childHashmap;
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
                    .inflate(R.layout.definition_list_group, parent, false);
        }


        return new DefinitionGroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.groupHeader.setText(arrayListGroup.get(position));

        //init members
        ArrayList<Definition> childArrayList;

        if (childHashmap.get(arrayListGroup.get(position)) != null){
            childArrayList = new ArrayList<>(childHashmap.get(arrayListGroup.get(position)));
        }
        else{
            childArrayList = new ArrayList<>();
        }



        //sets the child recycler
        holder.childRecycler.setNestedScrollingEnabled(false); //stops the recyclerview from scrolling
        DefinitionRecyclerAdapter definitionRecyclerAdapter = new DefinitionRecyclerAdapter(childArrayList,mContext);
        holder.childRecycler.setAdapter(definitionRecyclerAdapter);
        holder.childRecycler.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
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
