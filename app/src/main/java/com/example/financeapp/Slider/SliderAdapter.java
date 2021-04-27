package com.example.financeapp.Slider;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.financeapp.ArticleActivity;
import com.example.financeapp.ArticleCategoryActivity;
import com.example.financeapp.Objects.Article;
import com.example.financeapp.Objects.Definition;
import com.example.financeapp.Objects.Insight;
import com.example.financeapp.R;

import java.util.List;

import static android.content.ContentValues.TAG;
import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;

public class SliderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "SliderAdapter";
    private List<Definition> sliderDefinition;
    private List<Item> items;
    private ViewPager2 viewPager2;
    private Context context;

    public SliderAdapter(List<Item> items, ViewPager2 viewPager2, Context context) {
        this.items = items;
        this.viewPager2 = viewPager2;
        this.context = context;
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //viewtpyes are 0, 1 & 2
        //0 = word definition
        //1 = article
        //2 = insight

        if(viewType == 0){
            return new DefinitionViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.slider_item_definition,
                            parent,
                            false
                    )
            );
        }

        else if(viewType == 1){
            return new ArticleViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.slider_item_article,
                            parent,
                            false
                    )
            );
        }

        else{
            return new InsightsViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.slider_item_insight,
                            parent,
                            false
                    )
            );
        }
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == 0){
            Definition definition = (Definition) items.get(position).getObject();

            ((DefinitionViewHolder) holder).setTitle(definition);
            ((DefinitionViewHolder) holder).setDescription(definition);
        }
        else if(getItemViewType(position) == 1){
            Article article = (Article) items.get(position).getObject();

            ((ArticleViewHolder) holder).setTitle(article);
            ((ArticleViewHolder) holder).setAuthor(article);
            ((ArticleViewHolder) holder).setDescription(article);
            
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ArticleActivity.class);
                    intent.putExtra("ARTICLE",article);
                    Log.d(TAG, "startNewArticle");
                    context.startActivity(intent);
                }
            });
            
            

        }

        else if(getItemViewType(position) == 2){
            Insight insight = (Insight) items.get(position).getObject();

            ((InsightsViewHolder) holder).setInsight(insight);
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

    class DefinitionViewHolder extends RecyclerView.ViewHolder{

        private TextView title, description;

        DefinitionViewHolder(@NonNull View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.wordoftheday);
            description = itemView.findViewById(R.id.definition);
        }

        void setTitle(Definition definition){
            title.setText(definition.getWord());
        }

        void setDescription(Definition definition){
            description.setText(definition.getDescription());
        }


    }

    class ArticleViewHolder extends RecyclerView.ViewHolder{

        private TextView title, description, author;

        ArticleViewHolder(@NonNull View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            description = itemView.findViewById(R.id.description);
        }

        void setTitle(Article article){
            title.setText(article.getTitle());
        }

        void setAuthor(Article article){author.setText("By "+article.getAuthor());}

        void setDescription(Article article){
            description.setText(article.getDescription());
        }


    }


    class InsightsViewHolder extends RecyclerView.ViewHolder{

        private TextView insightsTextView;

        InsightsViewHolder(@NonNull View itemView){
            super(itemView);
            insightsTextView = itemView.findViewById(R.id.insights);
        }

        void setInsight(Insight insight){
            insightsTextView.setText(insight.getInsight1());
        }


    }



}
