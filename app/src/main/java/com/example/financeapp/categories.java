package com.example.financeapp;

public enum categories {
    FOOD("Food & Drink"),
    ENTERTAINMENT("Entertainment"),
    EDUCATION("Education"),
    BILLS("Bills & Utillity"),
    CAR("Auto & Transport"),
    TAX("Tax"),
    CLOTHING("Clothing"),
    GIFTS("Gifts & Donations"),
    HEALTH("Health & Fitness"),
    INCOME("Income"),
    EXPENSE("Expense"),
    PETS("Pets"),
    TRAVEL("Travel"),
    SHOPPING("Shopping"),
    PERSONALCARE("Personal Care"),
    OTHER("Other");


    categories(String cat){category = cat;}

    @Override
    public String toString(){ return category; }

    private String category;

}
