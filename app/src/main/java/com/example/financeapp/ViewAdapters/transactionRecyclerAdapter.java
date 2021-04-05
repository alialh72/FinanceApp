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

import com.example.financeapp.MainActivity;
import com.example.financeapp.R;
import com.example.financeapp.SingleTransactionActivity;
import com.example.financeapp.Objects.Transactions;
import com.example.financeapp.Objects.gradientColors;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;

public class transactionRecyclerAdapter extends RecyclerView.Adapter<transactionRecyclerAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Transactions> transactions;
    private Context mContext;
    private String className;

    public transactionRecyclerAdapter(ArrayList<Transactions> transactions,String className,Context context){
        this.transactions = transactions;
        this.className = className;

        mContext = context;
        Log.d(TAG, "transactionRecyclerAdapter: TransactionAdapter: "+transactions.size());

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

        holder.merchantTextView.setText(transactions.get(position).getMerchant());
        holder.categoryTextView.setText(transactions.get(position).getSubCategoryLabel());

        double value = Double.parseDouble(transactions.get(position).getValue());
        if (value < 0){
            holder.valueTextView.setTextColor(ContextCompat.getColor(mContext, R.color.red));
        }
        else{
            holder.valueTextView.setTextColor(ContextCompat.getColor(mContext, R.color.greenicon));
        }

        //set value
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        format.setCurrency(Currency.getInstance("CAD"));
        holder.valueTextView.setText(format.format(Double.parseDouble(transactions.get(position).getValue())));

        //set date
        if(transactions.get(position).getDate().equals(MainActivity.date)){
            holder.dateTextView.setText("Today");
        }
        else{
            holder.dateTextView.setText(transactions.get(position).getDate());
        }

        //set background color
        gradientColors gradient = MainActivity.gradientsCategories.get(transactions.get(position).getSubCategory().getDisplayableType());

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.BR_TL,
                new int[] {gradient.getColor1(),gradient.getColor2()});
        gd.setCornerRadius(25f);

        //set icon image
        holder.iconLayout.setBackgroundDrawable(gd);

        //determines if there should be a line underneath the item or not
        if (position == transactions.size()-1){
            holder.backgroundLayout.setBackgroundResource(R.drawable.outlinetop);
        }
        else {
            holder.backgroundLayout.setBackgroundResource(R.drawable.outline);
        }


        //set icon image
        int resID = mContext.getResources().getIdentifier(transactions.get(position).getSubCategory().getDisplayableType().toLowerCase().replace(" & ", ""), "drawable",  mContext.getPackageName());
        holder.iconImg.setImageResource(resID);


        //make wider if transactionFragment
        if(className.equals("transactionFragment")){
            int width = (int) mContext.getResources().getDimension(R.dimen.transactionwidth);
            holder.parentLayout.setLayoutParams(new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: clicked on: "+position+ ", "+transactions.get(position).getValue());
                Log.d(TAG, "onClick: transactions.size: "+transactions.size());

                Intent intent = new Intent(mContext, SingleTransactionActivity.class);
                intent.putExtra("TRANSACTION", transactions.get(position));
                mContext.startActivity(intent);

            }
        });

    }



    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView merchantTextView, categoryTextView, valueTextView, dateTextView;
        ImageView iconImg;
        ConstraintLayout iconLayout, backgroundLayout, parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            merchantTextView = itemView.findViewById(R.id.merchant);
            categoryTextView = itemView.findViewById(R.id.category);
            valueTextView = itemView.findViewById(R.id.value);
            dateTextView = itemView.findViewById(R.id.date);
            iconImg = itemView.findViewById(R.id.icon);
            iconLayout = itemView.findViewById(R.id.iconconstraint);
            backgroundLayout = itemView.findViewById(R.id.background);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }


}
