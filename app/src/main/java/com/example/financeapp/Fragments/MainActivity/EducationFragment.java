package com.example.financeapp.Fragments.MainActivity;

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

import com.example.financeapp.MainActivity;
import com.example.financeapp.Objects.ArticleCategory;
import com.example.financeapp.R;
import com.example.financeapp.ViewAdapters.miniArticleRecyclerAdapter;
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
    private ConstraintLayout categoryLayout1, categoryLayout2;





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
                        int selectedUserId = Integer.parseInt((String) item.getTitle());

                        MainActivity.UserInfo.setUser(selectedUserId, getContext());

                        return false;
                    }
                });
                popupMenu.show();

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
    }

    private void initText(){
        helloUsername.setText(MainActivity.UserInfo.returnUsername());
    }

    private void loadArticleRecyclerViews(articlesCategoryEnum cat1, articlesCategoryEnum cat2){
        categoryRecycler1.setNestedScrollingEnabled(false); //stops the recyclerview from scrolling
        categoryRecycler1.setAdapter(new miniArticleRecyclerAdapter(EducationInfo.getArticlesByCategory(cat1),getActivity()));
        categoryRecycler1.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        categoryRecycler2.setNestedScrollingEnabled(false); //stops the recyclerview from scrolling
        categoryRecycler2.setAdapter(new miniArticleRecyclerAdapter(EducationInfo.getArticlesByCategory(cat2),getActivity()));
        categoryRecycler2.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
    }

    private void setTopCategories(){
        ArrayList<ArticleCategory> articleCategories = EducationInfo.returnArticleCategories();

        Random rand = new Random();
        int pos1 = rand.nextInt(articleCategories.size());
        int pos2 = rand.nextInt(articleCategories.size());
        while (pos1 == pos2){ pos2 = rand.nextInt(articleCategories.size()); } //makes sure the 2 category positions selected are not the same

        ArticleCategory category1 = articleCategories.get(pos1);  //gets the string category and reverses it into an enum
        ArticleCategory category2 = articleCategories.get(pos2);   //gets the string category and reverses it into an enum

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




}