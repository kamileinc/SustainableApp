package com.example.sustainableapp.controllers;

import android.annotation.SuppressLint;
import android.app.Application;
import com.example.sustainableapp.models.Database;
import com.example.sustainableapp.models.Fact;
import com.example.sustainableapp.views.FactFragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FactController extends Application {
    public void getFacts(String category) {
        Database db = new Database();
        db.getFactsFromDB(category);
    }
    public static void checkFactsFound(ArrayList<Fact> facts) {
        ArrayList<Fact> factList = new ArrayList<>();
        int pickedFactInt = 0;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        switch (dayOfTheWeek) {
            case "Tuesday":
                pickedFactInt = 1;
                break;
            case "Wednesday":
                pickedFactInt = 2;
                break;
            case "Thursday":
                pickedFactInt = 3;
                break;
            case "Friday":
                pickedFactInt = 4;
                break;
            case "Saturday":
                pickedFactInt = 5;
                break;
            case "Sunday":
                pickedFactInt = 6;
                break;
        }
        factList.add(facts.get(pickedFactInt));
        FactFragment.checkFactsFound(factList);
    }
}
