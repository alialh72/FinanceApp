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
import com.example.financeapp.DefinitionActivity;
import com.example.financeapp.Objects.Article;
import com.example.financeapp.Objects.ArticleCategory;
import com.example.financeapp.Objects.Definition;
import com.example.financeapp.Objects.Insight;
import com.example.financeapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;
import static com.example.financeapp.MainActivity.EducationInfo;

public class SliderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "SliderAdapter";
    private List<Item> items;
    private Context context;

    public Article selectedArticle;
    public Article selectedArticleFromCategory;

    public SliderAdapter(List<Item> items, ViewPager2 viewPager2, Context context) {
        this.items = items;
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
            return new ArticleCategoryViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.slider_item_category_article,
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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DefinitionActivity.class);
                    intent.putExtra("DEFINITION",definition);
                    Log.d(TAG, "startNewDefinition");
                    context.startActivity(intent);
                }
            });
        }
        else if(getItemViewType(position) == 1){
            selectedArticle = (Article) items.get(position).getObject();

            ((ArticleViewHolder) holder).setArticle(selectedArticle);
            
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ArticleActivity.class);
                    intent.putExtra("ARTICLE",selectedArticle);
                    Log.d(TAG, "startNewArticle");
                    context.startActivity(intent);
                }
            });
            
            

        }

        else if(getItemViewType(position) == 2){
            ArticleCategory articleCategory = (ArticleCategory) items.get(position).getObject();

            ((ArticleCategoryViewHolder) holder).setArticle(articleCategory);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ArticleActivity.class);
                    intent.putExtra("ARTICLE",selectedArticleFromCategory);
                    Log.d(TAG, "startNewArticle");
                    context.startActivity(intent);
                }
            });
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

        void setArticle(Article article){
            title.setText(article.getTitle());
            author.setText("By "+article.getAuthor());
            description.setText(article.getDescription());
        }

    }


    class ArticleCategoryViewHolder extends RecyclerView.ViewHolder{

        private TextView title, category, description, author;

        ArticleCategoryViewHolder(@NonNull View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.title);
            category = itemView.findViewById(R.id.category);
            author = itemView.findViewById(R.id.author);
            description = itemView.findViewById(R.id.description);
        }

        void setArticle(ArticleCategory articleCategory){
            category.setText(articleCategory.getCategory().getType() + ":");

            //select random article from category
            Random rand = new Random();
            ArrayList<Article> articles = new ArrayList<>(articleCategory.getArticles());
            int randomArticle = rand.nextInt(articles.size());
            selectedArticleFromCategory = articles.get(randomArticle);

            if (articleCategory.getArticles().size() > 1){
                while(selectedArticle.getTitle().equals(selectedArticleFromCategory.getTitle())){
                    randomArticle = rand.nextInt(articles.size());
                    selectedArticleFromCategory = articles.get(randomArticle);
                }
            }


            title.setText(selectedArticleFromCategory.getTitle());
            author.setText("By "+selectedArticleFromCategory.getAuthor());
            description.setText(selectedArticleFromCategory.getDescription());
        }


    }



}
