package com.example.sustainableapp.controllers;

import android.app.Application;
import android.graphics.Bitmap;

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
        FactFragment.checkFactsFound(facts);
    }
}
