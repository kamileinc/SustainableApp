package com.example.sustainableapp.controllers;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.sustainableapp.models.Database;
import com.example.sustainableapp.models.EnergyAction;
import com.example.sustainableapp.models.SustainableAction;
import com.example.sustainableapp.views.EditProfileFragment;
import com.example.sustainableapp.views.EnergyActionFragment;
import com.example.sustainableapp.views.FoodActionFragment;
import com.example.sustainableapp.views.ProfileFragment;
import com.example.sustainableapp.views.TasksFragment;
import com.example.sustainableapp.views.TransportActionFragment;
import com.example.sustainableapp.views.WheelFragment;

import java.io.StringBufferInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SustainableActionController extends Application {
    public SustainableAction addNewCategory(String userID, String newCategory) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
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
        if (purpose.equals("TasksFragment")) {
            TasksFragment.checkUsersSAFound(sa);
        }
        else if (purpose.equals("WheelFragment")) {
            WheelFragment.checkUsersSAFound(sa);
        }
        else if (purpose.equals("EnergyActionFragment")) {
            EnergyActionFragment.checkUsersSAFound(sa);
        }
        else if (purpose.equals("TransportActionFragment")) {
            TransportActionFragment.checkUsersSAFound(sa);
        }
        else if (purpose.equals("FoodActionFragment")) {
            FoodActionFragment.checkUsersSAFound(sa);
        }
    }
    public static void checkUsersSANotFound(ArrayList<SustainableAction> sa, String purpose) {
        if (purpose.equals("TasksFragment")) {
            TasksFragment.checkUsersSANotFound(sa);
        }
        else if (purpose.equals("WheelFragment")) {
            WheelFragment.checkUsersSANotFound(sa);
        }
        else if (purpose.equals("EnergyActionFragment")) {
            EnergyActionFragment.checkUsersSANotFound(sa);
        }
        else if (purpose.equals("TransportActionFragment")) {
            TransportActionFragment.checkUsersSANotFound(sa);
        }
        else if (purpose.equals("FoodActionFragment")) {
            FoodActionFragment.checkUsersSANotFound(sa);
        }
    }
    public boolean isTodayInDates(String beginDate, String endDate) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
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
        Log.i("mano", "today: " + today.toString());
        Log.i("mano", "dateBegin: " + dateBegin.toString());
        Log.i("mano", "dateEng: " + dateEnd.toString());
        if((today.after(dateBegin) && today.before(dateEnd)) || today.equals(dateBegin) || today.equals(dateEnd)) {
            // In between
            return true;
        }
        else {
            return false;
        }
    }
}
