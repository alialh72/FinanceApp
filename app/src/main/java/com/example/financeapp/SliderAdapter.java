package com.example.financeapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder>{

    private static final String TAG = "";
    private List<SliderItem> sliderItems;
    private ViewPager2 viewPager2;

    SliderAdapter(List<SliderItem> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slider_item_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setTexx(sliderItems.get(position));
        holder.setDesc(sliderItems.get(position));


    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{

        private TextView texx;
        private TextView description;

        SliderViewHolder(@NonNull View itemView){
            super(itemView);
            texx = itemView.findViewById(R.id.textbox);
            description = itemView.findViewById(R.id.wordoftheday);
        }

        void setTexx(SliderItem sliderItem){
            texx.setText(sliderItem.getText());
        }

        void setDesc(SliderItem sliderItem){
            description.setText(sliderItem.getDesc());
        }


    }



}
