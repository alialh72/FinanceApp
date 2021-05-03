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
import java.util.Currency;

public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Transactions> transactions;
    private Context context;
    private String className;

    public TransactionRecyclerAdapter(ArrayList<Transactions> transactions, String className, Context context){
        this.transactions = transactions;
        this.className = className;

        this.context = context;
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
        Transactions transaction = transactions.get(position);
        holder.merchantTextView.setText(transaction.getMerchant());
        holder.categoryTextView.setText(transaction.getSubCategoryLabel());

        double value = Double.parseDouble(transaction.getValue());
        if (value < 0){
            holder.valueTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        else{
            holder.valueTextView.setTextColor(ContextCompat.getColor(context, R.color.greenIcon));
        }

        //set value
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        format.setCurrency(Currency.getInstance("CAD"));
        holder.valueTextView.setText(format.format(Double.parseDouble(transaction.getValue())));

        //set date
        if(transaction.getDate().equals(MainActivity.date)){
            holder.dateTextView.setText("Today");
        }
        else{
            holder.dateTextView.setText(transaction.getDate());
        }

        //set background color
        gradientColors gradient = MainActivity.gradientsCategories.get(transaction.getSubCategory().getDisplayableType());

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
        //converts the text into the name of the icons drawable file
        int resID = context.getResources().getIdentifier(transaction.getSubCategory().getDisplayableType().toLowerCase().replace(" & ", ""), "drawable",  context.getPackageName());
        holder.iconImg.setImageResource(resID);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: clicked on: "+position+ ", "+transaction.getValue());
                Log.d(TAG, "onClick: transactions.size: "+transactions.size());

                Intent intent = new Intent(context, SingleTransactionActivity.class);
                intent.putExtra("TRANSACTION", transaction);
                context.startActivity(intent);

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
