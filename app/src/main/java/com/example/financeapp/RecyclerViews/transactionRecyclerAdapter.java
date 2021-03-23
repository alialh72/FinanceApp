package com.example.financeapp.RecyclerViews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financeapp.MainActivity;
import com.example.financeapp.R;
import com.example.financeapp.Transactions;

import java.util.ArrayList;

public class transactionRecyclerAdapter extends RecyclerView.Adapter<transactionRecyclerAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Transactions> Localtransactions = new ArrayList<>();
    private Context mContext;


    public transactionRecyclerAdapter(Context context){
        //get 5 most recent transactions
        int pos;
        for (pos = MainActivity.UserInfo.transactions.size()-1; Localtransactions.size()<4;pos--){
            Localtransactions.add(MainActivity.UserInfo.transactions.get(pos));
        }
        Log.d(TAG, "transactionRecycler: Localtransactions: "+Localtransactions);

        mContext = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: called.");

        //holder.cuisinetext.setText(mCuisineText.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: ");

            }
        });

    }



    @Override
    public int getItemCount() {
        return Localtransactions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView merchantTextView, categoryTextView, valueTextView;
        ImageView iconView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            merchantTextView = itemView.findViewById(R.id.merchant);
            categoryTextView = itemView.findViewById(R.id.category);
            valueTextView = itemView.findViewById(R.id.value);
            iconView = itemView.findViewById(R.id.icon);

        }
    }


}
