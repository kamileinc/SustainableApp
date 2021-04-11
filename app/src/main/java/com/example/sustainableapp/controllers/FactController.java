package com.example.sustainableapp.controllers;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.sustainableapp.models.Database;
import com.example.sustainableapp.models.Fact;
import com.example.sustainableapp.models.SustainableAction;
import com.example.sustainableapp.views.EditProfileFragment;
import com.example.sustainableapp.views.FactFragment;
import com.example.sustainableapp.views.ProfileFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FactController extends Application {
    public void getFacts(String category) {
        Database db = new Database();
        db.getFactsFromDB(category);
    }
    public static void checkFactsFound(ArrayList<Fact> facts) {
        ArrayList factList = new ArrayList();
        int pickedFactInt = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        //Log.i("mano", "siandien yra: " + dayOfTheWeek + factsList.size());
        if (dayOfTheWeek.equals("Monday")) {
            pickedFactInt = 0;
        }
        else if (dayOfTheWeek.equals("Tuesday")) {
            pickedFactInt = 1;
        }
        else if (dayOfTheWeek.equals("Wednesday")) {
            pickedFactInt = 2;
        }
        else if (dayOfTheWeek.equals("Thursday")) {
            pickedFactInt = 3;
        }
        else if (dayOfTheWeek.equals("Friday")) {
            pickedFactInt = 4;
        }
        else if (dayOfTheWeek.equals("Saturday")) {
            pickedFactInt = 5;
        }
        else if (dayOfTheWeek.equals("Sunday")) {
            pickedFactInt = 6;
        }
        factList.add(facts.get(pickedFactInt));
        FactFragment.checkFactsFound(factList);
    }
}
