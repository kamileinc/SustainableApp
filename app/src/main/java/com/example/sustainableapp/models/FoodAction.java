package com.example.sustainableapp.models;

public class FoodAction extends SustainableAction{
    private String id;
    private String date;
    private String shoppingDate;
    private String shoppingPlaces;
    private String shoppingItems;
    private String localItems;
    private String seasonalItems;
    private String breakfastFood;
    private String lunchFood;
    private String dinnerFood;

    public FoodAction(String id, String category, String userID, String dateBegin, String dateEnd, String id1, String date, String shoppingDate, String shoppingPlaces, String shoppingItems, String localItems, String seasonalItems, String breakfastFood, String lunchFood, String dinnerFood) {
        super(id, category, userID, dateBegin, dateEnd);
        this.id = id1;
        this.date = date;
        this.shoppingDate = shoppingDate;
        this.shoppingPlaces = shoppingPlaces;
        this.shoppingItems = shoppingItems;
        this.localItems = localItems;
        this.seasonalItems = seasonalItems;
        this.breakfastFood = breakfastFood;
        this.lunchFood = lunchFood;
        this.dinnerFood = dinnerFood;
    }

    public FoodAction(String id, String category, String userID, String dateBegin, String dateEnd,  String date, String shoppingDate, String shoppingPlaces, String shoppingItems, String localItems, String seasonalItems, String breakfastFood, String lunchFood, String dinnerFood) {
        super(id, category, userID, dateBegin, dateEnd);
        this.id = Long.toString(System.currentTimeMillis()/1000);
        this.date = date;
        this.shoppingDate = shoppingDate;
        this.shoppingPlaces = shoppingPlaces;
        this.shoppingItems = shoppingItems;
        this.localItems = localItems;
        this.seasonalItems = seasonalItems;
        this.breakfastFood = breakfastFood;
        this.lunchFood = lunchFood;
        this.dinnerFood = dinnerFood;
    }

    @Override
    public String toString() {
        return "FoodAction{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", shoppingDate='" + shoppingDate + '\'' +
                ", shoppingPlaces='" + shoppingPlaces + '\'' +
                ", shoppingItems='" + shoppingItems + '\'' +
                ", localItems='" + localItems + '\'' +
                ", seasonalItems='" + seasonalItems + '\'' +
                ", breakfastFood='" + breakfastFood + '\'' +
                ", lunchFood='" + lunchFood + '\'' +
                ", dinnerFood='" + dinnerFood + '\'' +
                '}';
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShoppingDate() {
        return shoppingDate;
    }

    public void setShoppingDate(String shoppingDate) {
        this.shoppingDate = shoppingDate;
    }

    public String getShoppingPlaces() {
        return shoppingPlaces;
    }

    public void setShoppingPlaces(String shoppingPlaces) {
        this.shoppingPlaces = shoppingPlaces;
    }

    public String getShoppingItems() {
        return shoppingItems;
    }

    public void setShoppingItems(String shoppingItems) {
        this.shoppingItems = shoppingItems;
    }

    public String getLocalItems() {
        return localItems;
    }

    public void setLocalItems(String localItems) {
        this.localItems = localItems;
    }

    public String getSeasonalItems() {
        return seasonalItems;
    }

    public void setSeasonalItems(String seasonalItems) {
        this.seasonalItems = seasonalItems;
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
