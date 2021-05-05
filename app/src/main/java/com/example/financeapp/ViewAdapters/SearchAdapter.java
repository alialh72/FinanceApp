package com.example.financeapp.ViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.financeapp.ArticleActivity;
import com.example.financeapp.DefinitionActivity;
import com.example.financeapp.Objects.Article;
import com.example.financeapp.Objects.Definition;
import com.example.financeapp.R;
import com.example.financeapp.MainActivity;
import com.example.financeapp.EducationSearchActivity;
import com.example.financeapp.Slider.SliderAdapter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Object> list;
    private Context context;


    public SearchAdapter(ArrayList<Object> list, Context context){
        this.list = list;
        this.context = context;

    }

    @Override
    public int getItemViewType(int position) {
        //viewtype 0 : definition
        //viewtype 1 : article
        int viewtype = 1;
        if (list.get(position).getClass().getName().equals("com.example.financeapp.Objects.Definition")) {
            viewtype = 0;
        }
        else if (list.get(position).getClass().getName().equals("com.example.financeapp.Objects.Article")) {
            viewtype = 1;
        }

        return viewtype;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0){
            return new SearchAdapter.DefinitionViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.search_definition_item,
                            parent,
                            false
                    )
            );
        }

        else if(viewType == 1){
            return new SearchAdapter.ArticleViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.search_article_item,
                            parent,
                            false
                    )
            );
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (list.get(position).getClass().getName().equals("com.example.financeapp.Objects.Definition")){
            //bind definition viewholder
            DefinitionViewHolder definitionViewHolder = (DefinitionViewHolder) holder;
            Definition definition = (Definition) list.get(position);

            definitionViewHolder.word.setText(definition.getWord());

            definitionViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DefinitionActivity.class);
                    intent.putExtra("DEFINITION",definition);
                    Log.d(TAG, "startNewDefinition");
                    context.startActivity(intent);
                }
            });

        }



        else if (list.get(position).getClass().getName().equals("com.example.financeapp.Objects.Article")){
           //bind article viewholder

            ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
            Article article = (Article) list.get(position);

            articleViewHolder.title.setText(article.getTitle());
            articleViewHolder.description.setText(article.getDescription());
            articleViewHolder.info.setText("By "+article.getAuthor());

            Glide.with(context)
                    .asBitmap()
                    .load(article.getImage())
                    .into(articleViewHolder.imageView);

            articleViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ArticleActivity.class);
                    intent.putExtra("ARTICLE",article);
                    Log.d(TAG, "startNewArticle");
                    context.startActivity(intent);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<Object> filteredList){
        //called whenever the edittext is updated
        list = filteredList;
        notifyDataSetChanged();
    }

    class DefinitionViewHolder extends RecyclerView.ViewHolder{
        TextView word;

        public DefinitionViewHolder(@NonNull View itemView) {
            super(itemView);

            word = itemView.findViewById(R.id.word);
        }
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder{
        TextView title, description, info;
        ImageView imageView;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.image);
            info = itemView.findViewById(R.id.info);
        }
    }

}
