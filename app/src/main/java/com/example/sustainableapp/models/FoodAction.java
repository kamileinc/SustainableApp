package com.example.sustainableapp.models;

import androidx.annotation.NonNull;

public class FoodAction extends SustainableAction{
    private String faID;
    private String date;
    private int breakfastFood;
    private int lunchFood;
    private int dinnerFood;

    public FoodAction() {
    }

    public FoodAction(String id, String category, String userID, String dateBegin, String dateEnd, String faID, String date, int breakfastFood, int lunchFood, int dinnerFood) {
        super(id, category, userID, dateBegin, dateEnd);
        this.faID = faID;
        this.date = date;
        this.breakfastFood = breakfastFood;
        this.lunchFood = lunchFood;
        this.dinnerFood = dinnerFood;
    }

    public FoodAction(String id, String category, String userID, String dateBegin, String dateEnd,  String date, int breakfastFood, int lunchFood, int dinnerFood) {
        super(id, category, userID, dateBegin, dateEnd);
        this.faID = userID + date;
        this.date = date;
        this.breakfastFood = breakfastFood;
        this.lunchFood = lunchFood;
        this.dinnerFood = dinnerFood;
    }

    @NonNull
    @Override
    public String toString() {
        return "FoodAction{" +
                "faID='" + faID + '\'' +
                ", date='" + date + '\'' +
                ", breakfastFood='" + breakfastFood + '\'' +
                ", lunchFood='" + lunchFood + '\'' +
                ", dinnerFood='" + dinnerFood + '\'' +
                '}';
    }

    public String getFaID() {
        return faID;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getBreakfastFood() {
        return breakfastFood;
    }
    public int getLunchFood() {
        return lunchFood;
    }
    public int getDinnerFood() {
        return dinnerFood;
    }
}
