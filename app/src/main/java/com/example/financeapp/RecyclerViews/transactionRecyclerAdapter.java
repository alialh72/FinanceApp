package com.example.financeapp.RecyclerViews;

import android.content.Context;
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
import com.example.financeapp.Transactions;
import com.example.financeapp.gradientColors;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;

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

        holder.merchantTextView.setText(Localtransactions.get(position).getMerchant());
        holder.categoryTextView.setText(Localtransactions.get(position).getSubCategoryLabel());

        double value = Localtransactions.get(position).getValue();
        if (value < 0){
            holder.valueTextView.setTextColor(ContextCompat.getColor(mContext, R.color.red));
        }
        else{
            holder.valueTextView.setTextColor(ContextCompat.getColor(mContext, R.color.greenicon));
        }


        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        format.setCurrency(Currency.getInstance("CAD"));
        holder.valueTextView.setText(format.format(Localtransactions.get(position).getValue()));


        if(Localtransactions.get(position).getDate().equals(MainActivity.date)){
            holder.dateTextView.setText("Today");
        }
        else{
            holder.dateTextView.setText(MainActivity.date);
        }

        gradientColors gradient = MainActivity.gradientsCategories.get(Localtransactions.get(position).getSubCategory().getDisplayableType());

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.BR_TL,
                new int[] {gradient.getColor1(),gradient.getColor2()});
        gd.setCornerRadius(25f);

        holder.iconLayout.setBackgroundDrawable(gd);


        //set image
        int resID = mContext.getResources().getIdentifier(Localtransactions.get(position).getSubCategory().getDisplayableType().toLowerCase().replace(" & ", ""), "drawable",  mContext.getPackageName());
        holder.iconImg.setImageResource(resID);


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

        TextView merchantTextView, categoryTextView, valueTextView, dateTextView;
        ImageView iconImg;
        ConstraintLayout iconLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            merchantTextView = itemView.findViewById(R.id.merchant);
            categoryTextView = itemView.findViewById(R.id.category);
            valueTextView = itemView.findViewById(R.id.value);
            dateTextView = itemView.findViewById(R.id.date);
            iconImg = itemView.findViewById(R.id.icon);
            iconLayout = itemView.findViewById(R.id.iconconstraint);
        }
    }


}
