package com.example.sustainableapp.controllers;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.example.sustainableapp.models.Database;
import com.example.sustainableapp.models.EnergyAction;
import com.example.sustainableapp.models.SustainableAction;
import com.example.sustainableapp.models.User;
import com.example.sustainableapp.views.EditProfileFragment;
import com.example.sustainableapp.views.EnergyActionFragment;
import com.example.sustainableapp.views.LoginActivity;
import com.example.sustainableapp.views.ProfileFragment;
import com.example.sustainableapp.views.RegisterActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EnergyActionController extends Application {
    public static void checkEAEdited() {
        EnergyActionFragment.checkEAEdited();
    }
    public void addEnergyActionsToDB(SustainableAction sa) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        String dateStr = formatter.format(date);
        c.setTime(date);
        ArrayList<EnergyAction> eaList = new ArrayList<>();
        Database db = new Database();
        for (int i= 0; i< 7;i++) {

            String date2 = formatter.format(c.getTime());
            EnergyAction ea = new EnergyAction(sa.getId(), sa.getCategory(),sa.getUserID(), sa.getDateBegin(), sa.getDateEnd(), date2, false, "0:0", false, "0");
            db.saveEA(ea);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
    public void updateEnergyActionInDB(EnergyAction ea) {
        Database db = new Database();
        db.editEA(ea);
    }
    public List<String> validateEA(EnergyAction ea) {
        ArrayList<String> errors = new ArrayList();
        //jei shower checked min ir s negali but 0
        //devices nuo 0 iki 100
        //s nuo 0 iki 60
        errors.add(validateShowerMin(ea.isShower(), ea.getShowerTime()));
        errors.add(validateShowerS(ea.isShower(), ea.getShowerTime()));
        errors.add(validateDevices(ea.getDevicesOff()));

        return errors;
    }
    private String validateDevices(String s) {
        boolean numeric = true;
        try {
            int number = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            numeric = false;
        }

        if (s.isEmpty()) {
            return "Laukas negali būti tuščias";
        }
        else if (!numeric) {
            return "Laukas turi būti skaičius";
        }
        else if (numeric && (Integer.parseInt(s) > 100 || Integer.parseInt(s) < 0)) {
            return "Skaičius negali būti mažesnis nei 0 ar didesnis nei 100";
        }
        else {
            return "";
        }
    }
    private String validateShowerMin(boolean boo, String showerTime) {
        if (boo == true) {
            String[] sArr = showerTime.split(":", 5);
            Log.i("mano", "showertime: " + showerTime);
            int m1 = Integer.parseInt(sArr[0]);
            int s1 = Integer.parseInt(sArr[1]);

            Log.i("laikas", "m1 = " + m1 + ", s1 = " + s1);
            if (m1 == 0) {
                return "Laikas negali būt lygus 0";
            }
            if (m1 < 0 || m1 > 100) {
                return "Laikas negali būt mažiau nei 0 ar daugiau nei 100";
            }
        }
            return "";

    }
    private String validateShowerS(boolean boo, String showerTime) {
        if (boo == true) {
            String[] sArr = showerTime.split(":", 5);
            int m1 = Integer.parseInt(sArr[0]);
            int s1 = Integer.parseInt(sArr[1]);
            Log.i("laikas", "m1 = " + m1 + ", s1 = " + s1);
            if (s1 < 0 || s1 > 59) {
                return "Laikas negali būt mažiau nei 0 ar daugiau nei 59";
            }
        }
        return "";

    }
    public boolean getEAForEAFragment(String userId) {
        Database db = new Database();

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String dateStr = formatter.format(date);
        final String id = userId + dateStr;
        Log.i("mano", "EA ID: "+ id);
        db.getEADataForEAFragment(id);
        return true;


    }
    public static void checkEANotFound(List<EnergyAction> list) {
        EnergyActionFragment.checkEANotFound(list);
    }
    public static void checkEAFound(List<EnergyAction> list) {
        EnergyActionFragment.checkEAFound(list);
    }
}
