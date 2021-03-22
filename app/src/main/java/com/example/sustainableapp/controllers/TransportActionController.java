package com.example.sustainableapp.controllers;

import android.app.Application;
import android.util.Log;

import com.example.sustainableapp.models.Database;
import com.example.sustainableapp.models.EnergyAction;
import com.example.sustainableapp.models.SustainableAction;
import com.example.sustainableapp.models.TransportAction;
import com.example.sustainableapp.views.EnergyActionFragment;
import com.example.sustainableapp.views.MyResultsFragment;
import com.example.sustainableapp.views.TransportActionFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransportActionController extends Application {
    public void addTransportActionsToDB(SustainableAction sa) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        String dateStr = formatter.format(date);
        c.setTime(date);
        ArrayList<EnergyAction> eaList = new ArrayList<>();
        Database db = new Database();
        for (int i= 0; i< 7;i++) {
            String date2 = formatter.format(c.getTime());
            TransportAction ta = new TransportAction(sa.getId(), sa.getCategory(),sa.getUserID(), sa.getDateBegin(), sa.getDateEnd(), date2, false, false, "", false, "", false, "", false, "", "", "");
            db.saveTA(ta);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
    public boolean getTAForTAFragment(String userId, String purpose) {
        Database db = new Database();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String dateStr = formatter.format(date);
        final String id = userId + dateStr;
        Log.i("mano", "TA ID: "+ id);
        db.getTADataForTAFragment(id, purpose);
        return true;


    }
    public static void checkTANotFound(List<TransportAction> list, String purpose) {
        if (purpose.equals("TransportActionFragment")) {
            TransportActionFragment.checkTANotFound(list);
        }
        else if (purpose.equals("MyResultsFragment")) {
            MyResultsFragment.checkTANotFound(list);
        }
    }
    public static void checkTAFound(List<TransportAction> list,  String purpose) {
        if (purpose.equals("TransportActionFragment")) {
            TransportActionFragment.checkTAFound(list);
        }
        else if (purpose.equals("MyResultsFragment")) {
            MyResultsFragment.checkTAFound(list);
        }
    }
    public List<String> validateTA(TransportAction ta) {
        ArrayList<String> errors = new ArrayList();
        //walking 1 - 350
        //bicycling 1 -500
        //public transport 1 - 2000
        //car 1 - 2000
        // is kuriu 0 - nedaugiau nei car
        //paveziau 0 - 10

        errors.add(validateWalkingKm(ta.isWalking(), ta.getWalkingKM()));
        errors.add(validateBicyclingKm(ta.isBicycle(), ta.getBicycleKM()));
        errors.add(validatePublicTransportKm(ta.isPublicTransport(), ta.getPublicTransportKM()));
        errors.add(validateCarKm(ta.isCar(), ta.getCarKM()));
        errors.add(validateCarWithPassengersKm(ta.isCar(), ta.getCarPassengersKM(), ta.getCarKM()));
        errors.add(validateCarPassengers(ta.isCar(), ta.getCarPassengers(), ta.getCarPassengersKM()));
        return errors;
    }
    private String validateWalkingKm(boolean boo, String s) {
        boolean numeric = true;
        try {
            int number = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            numeric = false;
        }
        if (boo) {
            if (s.isEmpty()) {
                return "Laukas negali būti tuščias";
            }
            else if (!numeric) {
                return "Laukas turi būti skaičius";
            }
            else if (numeric && (Integer.parseInt(s) > 350 || Integer.parseInt(s) <= 0)) {
                return "Skaičius negali būti mažesnis nei 1 ar didesnis nei 350";
            }
        }
        return "";

    }
    private String validateBicyclingKm(boolean boo, String s) {
        boolean numeric = true;
        try {
            int number = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            numeric = false;
        }
        if (boo) {
            if (s.isEmpty()) {
                return "Laukas negali būti tuščias";
            }
            else if (!numeric) {
                return "Laukas turi būti skaičius";
            }
            else if (numeric && (Integer.parseInt(s) > 500 || Integer.parseInt(s) <= 0)) {
                return "Skaičius negali būti mažesnis nei 1 ar didesnis nei 500";
            }
        }
        return "";

    }
    private String validatePublicTransportKm(boolean boo, String s) {
        boolean numeric = true;
        try {
            int number = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            numeric = false;
        }
        if (boo) {
            if (s.isEmpty()) {
                return "Laukas negali būti tuščias";
            }
            else if (!numeric) {
                return "Laukas turi būti skaičius";
            }
            else if (numeric && (Integer.parseInt(s) > 2000 || Integer.parseInt(s) <= 0)) {
                return "Skaičius negali būti mažesnis nei 1 ar didesnis nei 2000";
            }
        }
        return "";

    }
    private String validateCarKm(boolean boo, String s) {
        boolean numeric = true;
        try {
            int number = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            numeric = false;
        }
        if (boo) {
            if (s.isEmpty()) {
                return "Laukas negali būti tuščias";
            }
            else if (!numeric) {
                return "Laukas turi būti skaičius";
            }
            else if (numeric && (Integer.parseInt(s) > 2000 || Integer.parseInt(s) <= 0)) {
                return "Skaičius negali būti mažesnis nei 1 ar didesnis nei 2000";
            }
        }
        return "";

    }
    private String validateCarWithPassengersKm(boolean boo, String s, String s2) {
        boolean numeric = true;
        try {
            int number = Integer.parseInt(s);
            int number2 = Integer.parseInt(s2);
        } catch (NumberFormatException e) {
            numeric = false;
        }
        if (boo) {
            if (s.isEmpty()) {
                return "Laukas negali būti tuščias";
            }
            else if (!numeric) {
                return "Laukas turi būti skaičius";
            }
            else if (numeric && (Integer.parseInt(s) > 2000 || Integer.parseInt(s) < 0)) {
                return "Skaičius negali būti mažesnis nei 0 ar didesnis nei 2000";
            }
            else if (numeric && (Integer.parseInt(s) > Integer.parseInt(s2) )) {
                return "Skaičius negali būti didesnis nei važiavimo automobiliu skaičius";
            }
        }
        return "";

    }
    private String validateCarPassengers(boolean boo, String s, String s2) {
        boolean numeric = true;
        try {
            int number = Integer.parseInt(s);
            int number2 = Integer.parseInt(s2);
        } catch (NumberFormatException e) {
            numeric = false;
        }
        if (boo) {
            if (s.isEmpty()) {
                return "Laukas negali būti tuščias";
            }
            else if (!numeric) {
                return "Laukas turi būti skaičius";
            }
            else if (numeric && (Integer.parseInt(s) > 10 || Integer.parseInt(s) < 0)) {
                return "Skaičius negali būti mažesnis nei 0 ar didesnis nei 10";
            }
            else if (numeric && (Integer.parseInt(s) > 0 && Integer.parseInt(s2) <= 0)) {
                return "Skaičius negali būti didesnis už 0, kai pavežimo km yra lygūs 0";
            }
        }
        return "";
    }
    public void updateTransportActionInDB(TransportAction ta) {
        Database db = new Database();
        db.editTA(ta);
    }
    public static void checkTAEdited() {
        TransportActionFragment.checkTAEdited();
    }
    public boolean getTAForMyResults(String userID, String purpose) {
        Database db = new Database();
        db.getTADataForMyResults(userID, purpose);
        return true;
    }
}
