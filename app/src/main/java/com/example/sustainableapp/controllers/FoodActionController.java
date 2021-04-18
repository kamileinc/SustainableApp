package com.example.sustainableapp.controllers;

import android.annotation.SuppressLint;
import android.app.Application;
import com.example.sustainableapp.models.Database;
import com.example.sustainableapp.models.FoodAction;
import com.example.sustainableapp.models.SustainableAction;
import com.example.sustainableapp.models.User;
import com.example.sustainableapp.views.FoodActionFragment;
import com.example.sustainableapp.views.MyResultsFragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.sustainableapp.views.AllResultsFragment.checkFAPointsForUser;

public class FoodActionController extends Application {
    public static void checkBadge1Edited() {
        FoodActionFragment.checkBadge1Edited();
    }
    public static void checkBadge0Edited() {
        FoodActionFragment.checkBadge0Edited();
    }
    public void addFoodActionsToDB(SustainableAction sa) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Database db = new Database();
        for (int i= 0; i< 7;i++) {
            String date2 = formatter.format(c.getTime());
            FoodAction fa = new FoodAction(sa.getId(), sa.getCategory(),sa.getUserID(), sa.getDateBegin(), sa.getDateEnd(), date2,
                    -1, -1, -1);
            db.saveFA(fa);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
    public void getFAForFAFragment(String userId, String purpose) {
        Database db = new Database();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String dateStr = formatter.format(date);
        final String id = userId + dateStr;
        db.getFADataForFAFragment(id, purpose);
    }
    public void getFAForMyResults(String userID, String purpose) {
        Database db = new Database();
        db.getFADataForMyResults(userID, purpose);
    }
    public static void checkFANotFound(String userID, String purpose) {
        if (purpose.equals("AllFA")) {
            double points = 0;
            MyResultsFragment.checkFAPoints(points);
        }
        else if (purpose.equals("AllResults")) {
            double points = 0;
            checkFAPointsForUser(points, userID);
        }
    }
    public static void checkFAFound(List<FoodAction> list, String userID, String purpose) {
        switch (purpose) {
            case "FoodActionFragment":
                FoodActionFragment.checkFAFound(list);
                break;
            case "MyResultsFragment":
                MyResultsFragment.checkFAFound(list);
                break;
            case "AllFA": {
                double points;
                points = sumAllFAPoints(list);
                MyResultsFragment.checkFAPoints(points);
                break;
            }
            case "AllResults": {
                double points;
                points = sumAllFAPoints(list);
                checkFAPointsForUser(points, userID);
                break;
            }
        }
    }
    public void updateFoodActionInDB(FoodAction fa) {
        Database db = new Database();
        db.editFA(fa);
    }
    public static void checkFAEdited() {
        FoodActionFragment.checkFAEdited();
    }
    public void getAllFoodPoints(String userID, String purpose) {
        Database db = new Database();
        db.getAllFA(userID, purpose);
    }
    public static double getPoints(FoodAction fa) {
        double temp = 0;
        if (fa.getBreakfastFood()==0) {
            temp = temp + 10;
        } else if (fa.getBreakfastFood()==1) {
            temp = temp + 7.5;
        } else if (fa.getBreakfastFood()==2) {
            temp = temp + 5;
        } else if (fa.getBreakfastFood()==3) {
            temp = temp + 2.5;
        }

        if (fa.getLunchFood()==0) {
            temp = temp + 10;
        } else if (fa.getLunchFood()==1) {
            temp = temp + 7.5;
        } else if (fa.getLunchFood()==2) {
            temp = temp + 5;
        } else if (fa.getLunchFood()==3) {
            temp = temp + 2.5;
        }

        if (fa.getDinnerFood()==0) {
            temp = temp + 10;
        } else if (fa.getDinnerFood()==1) {
            temp = temp + 7.5;
        } else if (fa.getDinnerFood()==2) {
            temp = temp + 5;
        } else if (fa.getDinnerFood()==3) {
            temp = temp + 2.5;
        }
        if (temp != 0) {
            temp = temp / 3;
        }
        return temp;
    }
    public static double sumAllFAPoints(List<FoodAction> faList) {
        double points = 0;
        if (faList != null) {
            for (int i = 0; i<faList.size();i++) {
                    points = points + getPoints(faList.get(i));
            }
        }
        return points;
    }
    public static ArrayList<Double> FAPointsForGraph(List<FoodAction> FAData) {
        ArrayList<Double> arr = new ArrayList<>();
        Date today = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = formatter.format(today);
        Date dateToCheck = new Date();
        for (int i = 0; i<FAData.size();i++) {
            SustainableActionController sac = new SustainableActionController();
            try {
                dateToCheck = formatter.parse(FAData.get(i).getDate());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (sac.isDateInDates(dateToCheck, FAData.get(0).getDate(), todayStr)) {
                arr.add(getPoints(FAData.get(i)));
            }
        }
        return arr;
    }
    public void checkForBadge1(FoodAction fa, User u) {
        if (!u.getBadges().get(0)) {
            Database db = new Database();
            db.editBadge0(u, "FAController");
        }
        if (getPoints(fa) == 10) {
            if (!u.getBadges().get(1)) {
                Database db = new Database();
                db.editBadge1(u);
            }
        }
    }

}
