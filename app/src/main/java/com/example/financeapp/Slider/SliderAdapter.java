package com.example.financeapp.Slider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.financeapp.R;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "";
    private List<SliderItem1> sliderItem1s;
    private List<Item> items;
    private ViewPager2 viewPager2;

    public SliderAdapter(List<Item> items, ViewPager2 viewPager2) {
        this.items = items;
        this.viewPager2 = viewPager2;
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //viewtpyes are 0 & 1

        if(viewType == 0){
            return new SliderViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.slider_item_container,
                            parent,
                            false
                    )
            );
        }

        else{
            return new SecondViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.slider_item_container2,
                            parent,
                            false
                    )
            );
        }
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == 0){
            ((SliderViewHolder) holder).setSliderText((SliderItem1) items.get(position).getObject());
        }
        else{
            ((SecondViewHolder) holder).setSliderText((SliderItem1) items.get(position).getObject());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{

        private TextView texx;
        private TextView description;

        SliderViewHolder(@NonNull View itemView){
            super(itemView);
            texx = itemView.findViewById(R.id.wordoftheday);
            description = itemView.findViewById(R.id.definition);
        }

        void setSliderText(SliderItem1 sliderItem1){
            texx.setText(sliderItem1.getText());
        }

        void setDesc(SliderItem1 sliderItem1){
            description.setText(sliderItem1.getDesc());
        }


    }

    class SecondViewHolder extends RecyclerView.ViewHolder{

        private TextView texx;
        private TextView description;

        SecondViewHolder(@NonNull View itemView){
            super(itemView);
            texx = itemView.findViewById(R.id.textbox);
            description = itemView.findViewById(R.id.wordoftheday);
        }

        void setSliderText(SliderItem1 sliderItem1){
            texx.setText(sliderItem1.getText());
        }

        void setDesc(SliderItem1 sliderItem1){
            description.setText(sliderItem1.getDesc());
        }


    }



}
