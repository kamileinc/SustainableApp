package com.example.sustainableapp.controllers;

import android.annotation.SuppressLint;
import android.app.Application;
import com.example.sustainableapp.models.Database;
import com.example.sustainableapp.models.SustainableAction;
import com.example.sustainableapp.views.EnergyActionFragment;
import com.example.sustainableapp.views.FoodActionFragment;
import com.example.sustainableapp.views.MyResultsFragment;
import com.example.sustainableapp.views.TasksFragment;
import com.example.sustainableapp.views.TransportActionFragment;
import com.example.sustainableapp.views.UserActivity;
import com.example.sustainableapp.views.WheelFragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SustainableActionController extends Application {
    public SustainableAction addNewCategory(String userID, String newCategory) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date dateBegin = new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        String beginDateStr = formatter.format(dateBegin);
        c.setTime(dateBegin);
        c.add(Calendar.DAY_OF_MONTH, 7);
        String dateEnd = formatter.format(c.getTime());
        SustainableAction sa = new SustainableAction(newCategory, userID, beginDateStr, dateEnd);
        Database db = new Database();
        db.addNewCategoryToDB(sa);
        return sa;
    }
    public void getUsersSustainableActions(String userID, String purpose) {
        Database db = new Database();
        db.getUsersSustainableActions(userID, purpose);
    }
    public static void checkUsersSAFound(ArrayList<SustainableAction> sa, String purpose) {
        switch (purpose) {
            case "TasksFragment":
                TasksFragment.checkUsersSAFound(sa);
                break;
            case "WheelFragment":
                WheelFragment.checkUsersSAFound(sa);
                break;
            case "EnergyActionFragment":
                EnergyActionFragment.checkUsersSAFound(sa);
                break;
            case "TransportActionFragment":
                TransportActionFragment.checkUsersSAFound(sa);
                break;
            case "FoodActionFragment":
                FoodActionFragment.checkUsersSAFound(sa);
                break;
            case "UserActivity":
                UserActivity.checkUsersSAFound(sa);
                break;
            case "MyResultsFragment":
                MyResultsFragment.checkUsersSAFound(sa);
                break;
        }
    }
    public static void checkUsersSANotFound(ArrayList<SustainableAction> sa, String purpose) {
        switch (purpose) {
            case "TasksFragment":
                TasksFragment.checkUsersSANotFound(sa);
                break;
            case "WheelFragment":
                WheelFragment.checkUsersSANotFound(sa);
                break;
            case "EnergyActionFragment":
                EnergyActionFragment.checkUsersSANotFound(sa);
                break;
            case "TransportActionFragment":
                TransportActionFragment.checkUsersSANotFound(sa);
                break;
            case "FoodActionFragment":
                FoodActionFragment.checkUsersSANotFound(sa);
                break;
            case "UserActivity":
                UserActivity.checkUsersSANotFound(sa);
                break;
            case "MyResultsFragment":
                MyResultsFragment.checkUsersSANotFound(sa);
                break;
        }
    }
    public boolean isTodayInDates(String beginDate, String endDate) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date(System.currentTimeMillis());
        Date dateBegin = new Date();
        Date dateEnd = new Date();
        try {
            dateBegin = formatter.parse(beginDate);
            dateEnd = formatter.parse(endDate);
        }
        catch(Exception e) {
            e.getStackTrace();
        }
        return (today.after(dateBegin) && today.before(dateEnd)) || today.equals(dateBegin) || today.equals(dateEnd);
    }
    public boolean isDateInDates(Date date, String beginDate, String endDate) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date dateBegin = new Date();
        Date dateEnd = new Date();
        String dateStr = formatter.format(date);
        try {
            date = formatter.parse(dateStr);
            dateBegin = formatter.parse(beginDate);
            dateEnd = formatter.parse(endDate);
        }
        catch(Exception e) {
            e.getStackTrace();
        }
        return (date.after(dateBegin) && date.before(dateEnd)) || date.equals(dateBegin) || date.equals(dateEnd);
    }
}
