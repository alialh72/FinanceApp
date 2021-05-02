package com.example.financeapp.ViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financeapp.DefinitionActivity;
import com.example.financeapp.MainActivity;
import com.example.financeapp.Objects.Definition;
import com.example.financeapp.R;
import com.example.financeapp.SingleTransactionActivity;
import com.example.financeapp.Objects.Transactions;
import com.example.financeapp.Objects.gradientColors;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;

public class DefinitionRecyclerAdapter extends RecyclerView.Adapter<DefinitionRecyclerAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Definition> definitions;
    private Context context;

    public DefinitionRecyclerAdapter(ArrayList<Definition> definitions, Context context){
        this.definitions = definitions;

        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.definition_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: called.");
        //set word
        Definition definition = definitions.get(position);
        holder.wordTextView.setText(definition.getWord());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DefinitionActivity.class);
                intent.putExtra("DEFINITION", definition);
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return definitions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView wordTextView;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            wordTextView = itemView.findViewById(R.id.wordTextView);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }


}
