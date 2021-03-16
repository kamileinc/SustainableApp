package com.example.sustainableapp.controllers;

import android.app.Application;
import android.util.Log;

import com.example.sustainableapp.models.Database;
import com.example.sustainableapp.models.EnergyAction;
import com.example.sustainableapp.models.FoodAction;
import com.example.sustainableapp.models.SustainableAction;
import com.example.sustainableapp.models.TransportAction;
import com.example.sustainableapp.views.EnergyActionFragment;
import com.example.sustainableapp.views.FoodActionFragment;
import com.example.sustainableapp.views.TransportActionFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FoodActionController extends Application {
    public void addFoodActionsToDB(SustainableAction sa) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        String dateStr = formatter.format(date);
        c.setTime(date);
       // ArrayList<EnergyAction> eaList = new ArrayList<>();
        Database db = new Database();
        for (int i= 0; i< 7;i++) {
            String date2 = formatter.format(c.getTime());
            FoodAction fa = new FoodAction(sa.getId(), sa.getCategory(),sa.getUserID(), sa.getDateBegin(), sa.getDateEnd(), date2,
                    "", "", "");
            db.saveFA(fa);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
    public boolean getFAForFAFragment(String userId) {
        Database db = new Database();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String dateStr = formatter.format(date);
        final String id = userId + dateStr;
        Log.i("mano", "FA ID: "+ id);
        db.getFADataForFAFragment(id);
        return true;
    }
    public static void checkFANotFound(List<FoodAction> list) {
        FoodActionFragment.checkFANotFound(list);
    }
    public static void checkFAFound(List<FoodAction> list) {
        FoodActionFragment.checkFAFound(list);
    }
    public void updateFoodActionInDB(FoodAction fa) {
        Database db = new Database();
        db.editFA(fa);
    }
    public static void checkFAEdited() {
        FoodActionFragment.checkFAEdited();
    }
}
