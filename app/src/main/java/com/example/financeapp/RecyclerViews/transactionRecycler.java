package com.example.financeapp.RecyclerViews;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.financeapp.MainActivity;
import com.example.financeapp.R;
import com.example.financeapp.Transactions;

import java.util.ArrayList;

public class transactionRecycler extends RecyclerView.Adapter<transactionRecycler.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Transactions> transactions = new ArrayList<>();
    private Context mContext;


    public transactionRecycler(ArrayList<Transactions> transactions, Context context){
        this.transactions = transactions;
        mContext = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_transaction_item, parent, false);
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
        return transactions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView merchantTextView, categoryTextView, moneyTextView;
        ConstraintLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            /*merchantTextView = itemView.findViewById(R.id.cuisine_image);
            categoryTextView = itemView.findViewById(R.id.cuisine_text);
            moneyTextView = itemView.findViewById(R.id.cuisine_text);
            parent_layout = itemView.findViewById(R.id.parent_layout);*/

        }
    }


}
