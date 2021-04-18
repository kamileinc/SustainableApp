package com.example.sustainableapp.controllers;

import android.annotation.SuppressLint;
import android.app.Application;
import com.example.sustainableapp.models.Database;
import com.example.sustainableapp.models.EnergyAction;
import com.example.sustainableapp.models.SustainableAction;
import com.example.sustainableapp.models.TransportAction;
import com.example.sustainableapp.models.User;
import com.example.sustainableapp.views.AllResultsFragment;
import com.example.sustainableapp.views.EnergyActionFragment;
import com.example.sustainableapp.views.MyResultsFragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EnergyActionController extends Application {
    public static void checkEAEdited() {
        EnergyActionFragment.checkEAEdited();
    }
    public static void checkBadge3Edited() {
        EnergyActionFragment.checkBadge3Edited();
    }
    public static void checkBadge0Edited() {
        EnergyActionFragment.checkBadge0Edited();
    }
    public void addEnergyActionsToDB(SustainableAction sa) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Database db = new Database();
        for (int i= 0; i< 7;i++) {
            String date2 = formatter.format(c.getTime());
            EnergyAction ea = new EnergyAction(sa.getId(), sa.getCategory(),sa.getUserID(), sa.getDateBegin(), sa.getDateEnd(), date2, false, false, "0:0", false, 0);
            db.saveEA(ea);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
    public void updateEnergyActionInDB(EnergyAction ea) {
        Database db = new Database();
        db.editEA(ea);
    }
    public List<String> validateEA(EnergyAction ea) {
        ArrayList<String> errors = new ArrayList<>();
        errors.add(validateShowerMin(ea.isShower(), ea.getShowerTime()));
        errors.add(validateShowerS(ea.isShower(), ea.getShowerTime()));
        errors.add(validateDevices(ea.getDevicesOff()));
        return errors;
    }
    private String validateDevices(int s) {
        if (s > 100 || s < 0) {
            return "Skaičius negali būti mažesnis nei 0 ar didesnis nei 100";
        }
        else {
            return "";
        }
    }
    private String validateShowerMin(boolean boo, String showerTime) {
        if (boo) {
            String[] sArr = showerTime.split(":", 5);
            int m1 = Integer.parseInt(sArr[0]);
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
        if (boo) {
            String[] sArr = showerTime.split(":", 5);
            int s1 = Integer.parseInt(sArr[1]);
            if (s1 < 0 || s1 > 59) {
                return "Laikas negali būt mažiau nei 0 ar daugiau nei 59";
            }
        }
        return "";
    }
    public void getEAForMyResults(String userID, String purpose) {
        Database db = new Database();
        db.getEADataForMyResults(userID, purpose);
    }
    public void getEAForEAFragment(String userId, String purpose) {
        Database db = new Database();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String dateStr = formatter.format(date);
        final String id = userId + dateStr;
        db.getEADataForEAFragment(id, purpose);
    }
    public static void checkEANotFound(String userID,  String purpose) {
        if (purpose.equals("AllEA")) {
            double points = 0;
            MyResultsFragment.checkEAPoints(points);
        }
        else if (purpose.equals("AllResults")) {
            double points = 0;
            AllResultsFragment.checkEAPointsForUser(points, userID);
        }
    }
    public static void checkEAFound(List<EnergyAction> list, String userID, String purpose) {
        switch (purpose) {
            case "EnergyActionFragment":
                EnergyActionFragment.checkEAFound(list);
                break;
            case "MyResultsFragment":
                MyResultsFragment.checkEAFound(list);
                break;
            case "AllEA": {
                double points;
                points = sumAllEAPoints(list);
                MyResultsFragment.checkEAPoints(points);
                break;
            }
            case "AllResults": {
                double points;
                points = sumAllEAPoints(list);
                AllResultsFragment.checkEAPointsForUser(points, userID);
                break;
            }
        }
    }
    public void getAllEnergyPoints(String userID, String purpose) {
        Database db = new Database();
        db.getAllEA(userID, purpose);
    }
    public static double getPoints(EnergyAction ea) {
        double temp = 0;
        if (ea != null) {
                if (ea.isNoWater()) {
                    temp = temp + 10;
                }
                else if (ea.isShower() && !ea.isBath()) {
                    String[] sArr = ea.getShowerTime().split(":", 5);
                    int m1 = Integer.parseInt(sArr[0]);
                    if (m1<5) {
                        temp = temp + 7.5;
                    }
                    else if (m1>=5 && m1<10) {
                        temp = temp + 5;
                    }
                    else if (m1>10) {
                        temp = temp + 2.5;
                    }
                }
                else if (!ea.isShower() && ea.isBath()) {
                    temp = temp + 2.5;
                }
                else if (ea.isShower() && ea.isBath()) {
                    temp = temp + 1;
                }
                int devicesOff = ea.getDevicesOff();
                if (devicesOff > 0 && devicesOff <= 5) {
                    temp = temp + 5;
                }
                else if (devicesOff > 5 && devicesOff <= 9) {
                    temp = temp + 7.5;
                }
                else if (devicesOff >=10) {
                    temp = temp + 10;
                }
                if (temp != 0) {
                    temp = temp / 2;
                }
            }
        return temp;
    }
    public static double sumAllEAPoints(List<EnergyAction> eaList) {
        double points = 0;
        for (int i = 0; i<eaList.size();i++) {
            points = points + getPoints(eaList.get(i));
        }
        return points;
    }
    public void checkForBadge3(EnergyAction ea, User u) {
        if (!u.getBadges().get(0)) {
            Database db = new Database();
            db.editBadge0(u, "EAController");
        }
        if (getPoints(ea) == 10) {
            if (!u.getBadges().get(3)) {
                Database db = new Database();
                db.editBadge3(u);
            }
        }
    }
    public static ArrayList<Double> EAPointsForGraph(List<EnergyAction> EAData) {
        ArrayList<Double> arr = new ArrayList<>();
        Date today = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = formatter.format(today);
        Date dateToCheck = new Date();
        for (int i = 0; i<EAData.size();i++) {
            double temp = 0;
            SustainableActionController sac = new SustainableActionController();
            try {
                dateToCheck = formatter.parse(EAData.get(i).getDate());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (sac.isDateInDates(dateToCheck, EAData.get(0).getDate(), todayStr)) {
                if (EAData.get(i).isNoWater()) {
                    temp = temp + 10;
                }
                else if (EAData.get(i).isShower() && !EAData.get(i).isBath()) {
                    String[] sArr = EAData.get(i).getShowerTime().split(":", 5);
                    int m1 = Integer.parseInt(sArr[0]);
                    if (m1<5) {
                        temp = temp + 7.5;
                    }
                    else if (m1>=5 && m1<10) {
                        temp = temp + 5;
                    }
                    else if (m1>10) {
                        temp = temp + 2.5;
                    }
                }
                else if (!EAData.get(i).isShower() && EAData.get(i).isBath()) {
                    temp = temp + 2.5;
                }
                else if (EAData.get(i).isShower() && EAData.get(i).isBath()) {
                    temp = temp + 1;
                }
                int devicesOff = EAData.get(i).getDevicesOff();
                if (devicesOff > 0 && devicesOff <= 5) {
                    temp = temp + 5;
                }
                else if (devicesOff > 5 && devicesOff <= 8) {
                    temp = temp + 7.5;
                }
                else if (devicesOff >10) {
                    temp = temp + 10;
                }
                if (temp != 0) {
                    temp = temp / 2;
                }
                arr.add(temp);
            }
        }
        return arr;
    }
}
