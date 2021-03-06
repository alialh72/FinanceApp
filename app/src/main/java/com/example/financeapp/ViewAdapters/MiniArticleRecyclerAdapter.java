package com.example.financeapp.ViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financeapp.ArticleActivity;
import com.example.financeapp.Objects.Article;
import com.example.financeapp.R;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class MiniArticleRecyclerAdapter extends RecyclerView.Adapter<MiniArticleRecyclerAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Article> articles;
    private boolean extended;
    private Context mContext;

    public MiniArticleRecyclerAdapter(ArrayList<Article> articles, Context context, boolean extended){
        this.articles = articles;
        this.extended = extended;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        if (extended == true){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_extended_layout, parent, false);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_mini_layout, parent, false);
        }


        Log.d(TAG, "onCreateViewHolder: view: "+view);
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
                Intent intent = new Intent(mContext, ArticleActivity.class);
                intent.putExtra("ARTICLE", articles.get(position));
                mContext.startActivity(intent);

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
