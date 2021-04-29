package com.example.financeapp.ViewAdapters;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.financeapp.FilterActivity;
import com.example.financeapp.TransactionCategoryActivity;
import com.example.financeapp.MainActivity;
import com.example.financeapp.Objects.gradientColors;
import com.example.financeapp.R;

import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    //initalise variables
    List<String> listGroup;
    HashMap<String,List<String>> listChild;
    Context mContext;
    String activity;

    //constructor
    public ExpandableListAdapter(List<String> listGroup, HashMap<String, List<String>> listChild,Context context){
        this.listGroup = listGroup;
        this.listChild = listChild;
        this.activity = activity;
        mContext = context;
        Log.d(TAG, "MainAdapter: listgroup: " + listGroup);
        Log.d(TAG, "MainAdapter: listchild: " + listChild);


        //this section stores the selected box's position so it isnt changed when selecting another option in another group
    }

    @Override
    public int getGroupCount() {
        //return group size
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //return child size
        return listChild.get(listGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        //return group item
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //return child item
        return listChild.get(listGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        //initialise view
        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category_expandable
                        ,viewGroup, false);

        //initialise and assign variables
        ConstraintLayout iconLayout = view.findViewById(R.id.iconconstraint);
        ImageView icon = view.findViewById(R.id.icon);

        TextView headerCategory = view.findViewById(R.id.category);
        String sgroup = String.valueOf(getGroup(groupPosition));

        headerCategory.setText(sgroup);

        //set background color
        gradientColors gradient = MainActivity.gradientsCategories.get(listGroup.get(groupPosition));

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.BR_TL,
                new int[] {gradient.getColor1(),gradient.getColor2()});
        gd.setCornerRadius(25f);

        iconLayout.setBackgroundDrawable(gd);

        //set icon
        int resID = mContext.getResources().getIdentifier(listGroup.get(groupPosition).toLowerCase().replace(" & ", ""), "drawable",  mContext.getPackageName());
        icon.setImageResource(resID);


        //return view
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        //initialise view
        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category_item_expandable
                        ,viewGroup, false);



        //initialise and assign variables
        TextView subCategoryView = view.findViewById(R.id.category);

        //initalise string
        String schild = String.valueOf(getChild(groupPosition,childPosition));
        Log.d(TAG, "getChildView: Schild: " + schild);
        String sgroup = String.valueOf(getGroup(groupPosition));
        Log.d(TAG, "getChildView: sgroup: " + sgroup);

        //set text on text view
        subCategoryView.setText(schild);

        if (schild == listGroup.get(groupPosition)){
            subCategoryView.setTypeface(null, Typeface.ITALIC);
        }



        //setonclicklistener
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TransactionCategoryActivity) mContext).setCategoryText(schild);
            }
        });



        //return view
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
