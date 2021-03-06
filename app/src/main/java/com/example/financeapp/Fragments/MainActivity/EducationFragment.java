package com.example.financeapp.Fragments.MainActivity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.financeapp.ArticleCategoryActivity;
import com.example.financeapp.GlossaryActivity;
import com.example.financeapp.MainActivity;
import com.example.financeapp.Objects.ArticleCategory;
import com.example.financeapp.R;
import com.example.financeapp.ViewAdapters.MiniArticleRecyclerAdapter;
import com.example.financeapp.Enums.articlesCategoryEnum;

import java.util.ArrayList;
import java.util.Random;

import static android.content.ContentValues.TAG;
import static com.example.financeapp.MainActivity.EducationInfo;

public class EducationFragment extends Fragment {

    private TextView helloUsername;
    private ImageView userButton;

    private RecyclerView categoryRecycler1, categoryRecycler2;

    private TextView categoryTitle1, categoryTitle2;
    private ConstraintLayout categoryLayout1, categoryLayout2, categoryClickable1, categoryClickable2;

    private ConstraintLayout constraintTaxes, constraintSavings, constraintFinance, constraintInvesting, constraintAccounts, constraintCrypto;
    private ConstraintLayout glossaryButton;

    private ArticleCategory category1,category2;


    public EducationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_education, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findViews();
        initText();

        setTopCategories();


        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), userButton);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Log.d(TAG, "onMenuItemClick: clicked: "+ item);
                        int selectedUserId = Integer.parseInt((String) item.getTitle()); //gets the userid

                        MainActivity.UserInfo.setUser(selectedUserId, getContext());

                        return false;
                    }
                });
                popupMenu.show();

            }
        });


        categoryLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewArticleCategory(category1);
            }
        });


        categoryLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewArticleCategory(category2);
            }
        });


        categoryClickable1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewArticleCategory(category1);
            }
        });


        categoryClickable2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewArticleCategory(category2);
            }
        });

        constraintSavings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewArticleCategory(EducationInfo.getArticleCategoryByEnum(articlesCategoryEnum.SAVINGS));
            }
        });

        constraintFinance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewArticleCategory(EducationInfo.getArticleCategoryByEnum(articlesCategoryEnum.FINANCE));
            }
        });

        constraintTaxes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewArticleCategory(EducationInfo.getArticleCategoryByEnum(articlesCategoryEnum.TAXES));
            }
        });

        constraintInvesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewArticleCategory(EducationInfo.getArticleCategoryByEnum(articlesCategoryEnum.INVESTING));
            }
        });

        constraintCrypto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewArticleCategory(EducationInfo.getArticleCategoryByEnum(articlesCategoryEnum.CRYPTO));
            }
        });

        constraintAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewArticleCategory(EducationInfo.getArticleCategoryByEnum(articlesCategoryEnum.ACCOUNTS));
            }
        });



        glossaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GlossaryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findViews(){
        userButton = getView().findViewById(R.id.userButton);
        helloUsername = getView().findViewById(R.id.helloUsername);

        categoryRecycler1 = getView().findViewById(R.id.categoryRecycler1);
        categoryRecycler2 = getView().findViewById(R.id.categoryRecycler2);

        categoryTitle1 = getView().findViewById(R.id.categoryTitle1);
        categoryTitle2 = getView().findViewById(R.id.categoryTitle2);

        categoryLayout1 = getView().findViewById(R.id.category1);
        categoryLayout2 = getView().findViewById(R.id.category2);

        categoryClickable1 = getView().findViewById(R.id.categoryClickable1);
        categoryClickable2 = getView().findViewById(R.id.categoryClickable2);

        constraintAccounts = getView().findViewById(R.id.constraintAccounts);
        constraintCrypto = getView().findViewById(R.id.constraintCrypto);
        constraintFinance = getView().findViewById(R.id.constraintFinance);
        constraintTaxes = getView().findViewById(R.id.constraintTaxes);
        constraintInvesting = getView().findViewById(R.id.constraintInvesting);
        constraintSavings = getView().findViewById(R.id.constraintSavings);

        glossaryButton = getView().findViewById(R.id.glossaryButton);
    }

    private void initText(){
        helloUsername.setText(MainActivity.UserInfo.returnUsername());
    }

    private void loadArticleRecyclerViews(articlesCategoryEnum cat1, articlesCategoryEnum cat2){

        Log.d(TAG, "loadArticleRecyclerViews: "+EducationInfo.getArticlesByCategory(cat1));
        //categoryRecycler1.setNestedScrollingEnabled(false); //stops the recyclerview from scrolling
        categoryRecycler1.setAdapter(new MiniArticleRecyclerAdapter(EducationInfo.getArticlesByCategory(cat1),getActivity(), false));
        categoryRecycler1.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        //categoryRecycler2.setNestedScrollingEnabled(false); //stops the recyclerview from scrolling
        categoryRecycler2.setAdapter(new MiniArticleRecyclerAdapter(EducationInfo.getArticlesByCategory(cat2),getActivity(), false));
        categoryRecycler2.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
    }

    private void setTopCategories(){
        ArrayList<ArticleCategory> articleCategories = EducationInfo.returnArticleCategories();
        Log.d(TAG, "setTopCategories: articleCategories.size()"+articleCategories.size());

        Random rand = new Random();
        Random rand2 = new Random();

        int pos1 = rand.nextInt(articleCategories.size());
        int pos2 = rand2.nextInt(articleCategories.size());
        while (pos1 == pos2){ pos2 = rand2.nextInt(articleCategories.size()); } //makes sure the 2 category positions selected are not the same
        Log.d(TAG, "setTopCategories: pos1:"+pos1);
        Log.d(TAG, "setTopCategories: pos2:"+pos2);

        category1 = articleCategories.get(pos1);  //gets the string category using the random position
        category2 = articleCategories.get(pos2);   //gets the string category using the random position

        //set background and text of the category layouts
        GradientDrawable gd1 = new GradientDrawable(
                GradientDrawable.Orientation.BR_TL,
                new int[] {category1.getColor(), category1.getColor()});
        gd1.setCornerRadius(25f);


        GradientDrawable gd2 = new GradientDrawable(
                GradientDrawable.Orientation.BR_TL,
                new int[] {category2.getColor(), category2.getColor()});
        gd2.setCornerRadius(25f);


        categoryLayout1.setBackgroundDrawable(gd1);
        categoryTitle1.setText(category1.getCategory().getType());

        categoryLayout2.setBackgroundDrawable(gd2);
        categoryTitle2.setText(category2.getCategory().getType());


        loadArticleRecyclerViews(category1.getCategory(), category2.getCategory());
    }

    private void startNewArticleCategory(ArticleCategory articleCategory){
        Intent intent = new Intent(getActivity(), ArticleCategoryActivity.class);
        intent.putExtra("ARTICLECATEGORY",articleCategory);
        Log.d(TAG, "startNewArticleCategory: startarticlecat");
        startActivity(intent);
    }

}