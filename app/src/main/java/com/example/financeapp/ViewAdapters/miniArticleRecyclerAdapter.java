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
import com.example.financeapp.Objects.Article;
import com.example.financeapp.R;
import com.example.financeapp.SingleTransactionActivity;
import com.example.financeapp.Objects.Transactions;
import com.example.financeapp.Objects.gradientColors;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;

public class miniArticleRecyclerAdapter extends RecyclerView.Adapter<miniArticleRecyclerAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Article> articles;
    private Context mContext;

    public miniArticleRecyclerAdapter(ArrayList<Article> articles, Context context){
        this.articles = articles;

        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_mini_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: called.");
        holder.articleTitle.setText(articles.get(position).getTitle());
        holder.articleAuthor.setText("By " + articles.get(position).getAuthor());
        holder.articleDescription.setText(StringUtils.abbreviate(articles.get(position).getDescription(), 90));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Mini Article: "+articles.get(position).getTitle());
            }
        });

    }



    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView articleTitle, articleAuthor, articleDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            articleTitle = itemView.findViewById(R.id.title);
            articleAuthor = itemView.findViewById(R.id.author);
            articleDescription = itemView.findViewById(R.id.description);
        }
    }


}
