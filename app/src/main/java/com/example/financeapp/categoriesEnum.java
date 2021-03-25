package com.example.financeapp;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class categoriesEnum {

    public interface CategoryInterface {

        String getDisplayableType();
    }

    public enum MainCategories implements CategoryInterface {

        FOOD("Food & Drink"),
        ENTERTAINMENT("Entertainment"),
        EDUCATION("Education"),
        BILLS("Bills & Utility"),
        CAR("Auto & Transport"),
        HEALTH("Health & Fitness"),
        INCOME("Income"),
        EXPENSE("Expense"),
        PETS("Pets"),
        TRAVEL("Travel"),
        SHOPPING("Shopping"),
        OTHER("Other");

        private final String type;

        MainCategories(final String type) {
            this.type = type;
        }

        public String getDisplayableType() {
            return type;
        }


    }

    public enum SubCategory implements CategoryInterface {
        FOOD("Food & Drink", MainCategories.FOOD),
        ENTERTAINMENT("Entertainment", MainCategories.ENTERTAINMENT),
        EDUCATION("Education", MainCategories.EDUCATION),
        BILLS("Bills & Utility", MainCategories.BILLS),
        CAR("Auto & Transport", MainCategories.CAR),
        HEALTH("Health & Fitness", MainCategories.HEALTH),
        INCOME("Income", MainCategories.INCOME),
        EXPENSE("Expense", MainCategories.EXPENSE),
        PETS("Pets", MainCategories.PETS),
        TRAVEL("Travel", MainCategories.TRAVEL),
        SHOPPING("Shopping", MainCategories.SHOPPING),
        OTHER("Other", MainCategories.OTHER),

        RESTAURANT("Restaurant", MainCategories.FOOD),
        FASTFOOD("Fast Food", MainCategories.FOOD),
        GROCERIES("Groceries", MainCategories.FOOD),
        COFFEE("Coffee Shop", MainCategories.FOOD),

        CLOTHING("Clothing", MainCategories.SHOPPING),
        TOYS("Toys", MainCategories.SHOPPING),


        UTILITIES("Utilities", MainCategories.BILLS),
        PHONE("Home Phone", MainCategories.BILLS),
        INTERNET("Internet", MainCategories.BILLS),
        TELEVISION("Television", MainCategories.BILLS),

        HOTEL("Hotel", MainCategories.TRAVEL),
        TAXI("Taxi", MainCategories.TRAVEL),
        PLANE("Air Travel", MainCategories.TRAVEL),

        SPA("Spa", MainCategories.HEALTH),
        SALON("Salon", MainCategories.HEALTH),
        GYM("Gym", MainCategories.HEALTH),
        DOCTOR("Doctor", MainCategories.HEALTH),

        TUITION("Tuition", MainCategories.EDUCATION),
        BOOKS("Books", MainCategories.EDUCATION),

        PETFOOD("Pet Food", MainCategories.PETS),
        VET("Veterinary", MainCategories.PETS),

        CARSERVICING("Service & Parts", MainCategories.CAR),
        AUTOINSURANCE("Auto Insurance", MainCategories.CAR),
        PARKING("Parking", MainCategories.CAR),
        FUEL("Gas & Fuel", MainCategories.CAR),

        MOVIES("Movies", MainCategories.ENTERTAINMENT),
        ARTS("Arts", MainCategories.ENTERTAINMENT),
        MUSIC("Music", MainCategories.ENTERTAINMENT);

        private final String label;
        private final MainCategories category;

        SubCategory(String label, MainCategories category) {
            this.label = label;
            this.category = category;
        }

        public String getDisplayableType() {
            return category.getDisplayableType();
        }

        public String getLabel() {
            return label;
        }


        public static final Map<String, SubCategory> LOOKUP = Maps.uniqueIndex(
                Arrays.asList(SubCategory.values()),
                SubCategory::getLabel);
    }


}