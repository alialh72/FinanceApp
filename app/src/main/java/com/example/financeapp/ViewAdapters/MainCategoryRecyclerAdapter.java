package com.example.financeapp.ViewAdapters;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.financeapp.MainActivity;
import com.example.financeapp.R;
import com.example.financeapp.Objects.gradientColors;
import com.example.financeapp.TransactionsMainCategoryActivity;

import java.util.ArrayList;

public class MainCategoryRecyclerAdapter extends RecyclerView.Adapter<MainCategoryRecyclerAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> categories;
    private Context context;


    public MainCategoryRecyclerAdapter(ArrayList<String> categories, Context context){
        this.categories = categories;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_category, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String category = categories.get(position);
        
        holder.headerCategory.setText(category);
        Log.d(TAG, "onBindViewHolder: past here");

        //set background color
        gradientColors gradient = MainActivity.gradientsCategories.get(category);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.BR_TL,
                new int[] {gradient.getColor1(),gradient.getColor2()});
        gd.setCornerRadius(25f);

        holder.iconLayout.setBackgroundDrawable(gd);

        //set icon
        int resID = context.getResources().getIdentifier(category.toLowerCase().replace(" & ", ""), "drawable",  context.getPackageName());
        holder.icon.setImageResource(resID);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TransactionsMainCategoryActivity) context).setCategoryText(category);
            }
        });

    }



    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout iconLayout;
        ImageView icon;

        TextView headerCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iconLayout = itemView.findViewById(R.id.iconconstraint);
            icon = itemView.findViewById(R.id.icon);
            headerCategory = itemView.findViewById(R.id.category);
        }
    }


}
