package com.example.sustainableapp.models;

public class FoodAction extends SustainableAction{
    private String faID;
    private String date;
    private String breakfastFood;
    private String lunchFood;
    private String dinnerFood;

    public FoodAction() {
    }

    public FoodAction(String id, String category, String userID, String dateBegin, String dateEnd, String faID, String date, String breakfastFood, String lunchFood, String dinnerFood) {
        super(id, category, userID, dateBegin, dateEnd);
        this.faID = faID;
        this.date = date;
        this.breakfastFood = breakfastFood;
        this.lunchFood = lunchFood;
        this.dinnerFood = dinnerFood;
    }

    public FoodAction(String id, String category, String userID, String dateBegin, String dateEnd,  String date, String breakfastFood, String lunchFood, String dinnerFood) {
        super(id, category, userID, dateBegin, dateEnd);
        this.faID = userID + date;
        this.date = date;
        this.breakfastFood = breakfastFood;
        this.lunchFood = lunchFood;
        this.dinnerFood = dinnerFood;
    }

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

    public void setFaID(String id) {
        this.faID = faID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getBreakfastFood() {
        return breakfastFood;
    }

    public void setBreakfastFood(String breakfastFood) {
        this.breakfastFood = breakfastFood;
    }

    public String getLunchFood() {
        return lunchFood;
    }

    public void setLunchFood(String lunchFood) {
        this.lunchFood = lunchFood;
    }

    public String getDinnerFood() {
        return dinnerFood;
    }

    public void setDinnerFood(String dinnerFood) {
        this.dinnerFood = dinnerFood;
    }
}
