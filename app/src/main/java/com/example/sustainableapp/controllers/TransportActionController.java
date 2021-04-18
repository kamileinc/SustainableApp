package com.example.sustainableapp.controllers;

import android.annotation.SuppressLint;
import android.app.Application;
import com.example.sustainableapp.models.Database;
import com.example.sustainableapp.models.SustainableAction;
import com.example.sustainableapp.models.TransportAction;
import com.example.sustainableapp.models.User;
import com.example.sustainableapp.views.AllResultsFragment;
import com.example.sustainableapp.views.MyResultsFragment;
import com.example.sustainableapp.views.TransportActionFragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransportActionController extends Application {
    public static void checkBadge2Edited() {
        TransportActionFragment.checkBadge2Edited();
    }
    public static void checkBadge0Edited() {
        TransportActionFragment.checkBadge0Edited();
    }
    public void addTransportActionsToDB(SustainableAction sa) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Database db = new Database();
        for (int i= 0; i< 7;i++) {
            String date2 = formatter.format(c.getTime());
            TransportAction ta = new TransportAction(sa.getId(), sa.getCategory(),sa.getUserID(), sa.getDateBegin(), sa.getDateEnd(), date2, false, false, 0, false, 0, false, 0, false, 0, 0, 0);
            db.saveTA(ta);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
    public void getTAForTAFragment(String userId, String purpose) {
        Database db = new Database();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String dateStr = formatter.format(date);
        final String id = userId + dateStr;
        db.getTADataForTAFragment(id, purpose);
    }
    public static void checkTANotFound(String userID, String purpose) {
        switch (purpose) {
            case "TransportActionFragment":
                TransportActionFragment.checkTANotFound();
                break;
            case "AllTA": {
                double points = 0;
                MyResultsFragment.checkTAPoints(points);
                break;
            }
            case "AllResults": {
                double points = 0;
                AllResultsFragment.checkTAPointsForUser(points, userID);
                break;
            }
        }
    }
    public static void checkTAFound(List<TransportAction> list,  String userID, String purpose) {
        switch (purpose) {
            case "TransportActionFragment":
                TransportActionFragment.checkTAFound(list);
                break;
            case "MyResultsFragment":
                MyResultsFragment.checkTAFound(list);
                break;
            case "AllTA": {
                double points;
                points = sumAllTAPoints(list);
                MyResultsFragment.checkTAPoints(points);
                break;
            }
            case "AllResults": {
                double points;
                points = sumAllTAPoints(list);
                AllResultsFragment.checkTAPointsForUser(points, userID);
                break;
            }
        }
    }
    public List<String> validateTA(TransportAction ta) {
        ArrayList<String> errors = new ArrayList<>();
        errors.add(validateWalkingKm(ta.isWalking(), ta.getWalkingKM()));
        errors.add(validateBicyclingKm(ta.isBicycle(), ta.getBicycleKM()));
        errors.add(validatePublicTransportKm(ta.isPublicTransport(), ta.getPublicTransportKM()));
        errors.add(validateCarKm(ta.isCar(), ta.getCarKM()));
        errors.add(validateCarWithPassengersKm(ta.isCar(), ta.getCarPassengersKM(), ta.getCarKM()));
        errors.add(validateCarPassengers(ta.isCar(), ta.getCarPassengers(), ta.getCarPassengersKM()));
        return errors;
    }
    private String validateWalkingKm(boolean boo, int s) {

        if (boo) {
            if (s > 350 || s <= 0) {
                return "Skaičius negali būti mažesnis nei 1 ar didesnis nei 350";
            }
        }
        return "";

    }
    private String validateBicyclingKm(boolean boo, int s) {
        if (boo) {
            if (s > 500 || s <= 0) {
                return "Skaičius negali būti mažesnis nei 1 ar didesnis nei 500";
            }
        }
        return "";

    }
    private String validatePublicTransportKm(boolean boo, int s) {
        if (boo) {
            if (s > 2000 || s <= 0) {
                return "Skaičius negali būti mažesnis nei 1 ar didesnis nei 2000";
            }
        }
        return "";

    }
    private String validateCarKm(boolean boo, int s) {
        if (boo) {
            if (s > 2000 || s <= 0) {
                return "Skaičius negali būti mažesnis nei 1 ar didesnis nei 2000";
            }
        }
        return "";

    }
    private String validateCarWithPassengersKm(boolean boo, int s, int s2) {
        if (boo) {
            if (s > 2000 || s < 0) {
                return "Skaičius negali būti mažesnis nei 0 ar didesnis nei 2000";
            }
            else if (s > s2) {
                return "Skaičius negali būti didesnis nei važiavimo automobiliu skaičius";
            }
        }
        return "";

    }
    private String validateCarPassengers(boolean boo, int s, int s2) {
        if (boo) {
            if (s > 10 || s< 0) {
                return "Skaičius negali būti mažesnis nei 0 ar didesnis nei 10";
            }
            else if (s > 0 && s2 <= 0) {
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
    public void getTAForMyResults(String userID, String purpose) {
        Database db = new Database();
        db.getTADataForMyResults(userID, purpose);
    }
    public void getAllTransportPoints(String userID, String purpose) {
        Database db = new Database();
        db.getAllTA(userID, purpose);
    }
    public static double getPoints(TransportAction ta) {
        double temp = 0;
        if (ta != null) {
                if (ta.isNoTravelling()) {
                    temp = temp + 10;
                }
                else if (ta.isWalking() && !ta.isBicycle() && !ta.isPublicTransport() && !ta.isCar()) {
                    temp = temp + 10;
                }
                else if (ta.isBicycle() && !ta.isWalking() && !ta.isPublicTransport() && !ta.isCar()) {
                    temp = temp + 10;
                }
                else if (ta.isPublicTransport() && !ta.isBicycle() && !ta.isWalking() && !ta.isCar()) {
                    temp = temp + 7.5;
                }
                else if ((ta.isWalking() || ta.isBicycle()) && ta.isPublicTransport() && !ta.isCar()) {
                    temp = temp + 8.5;
                }
                else if (!ta.isWalking() && !ta.isBicycle() && !ta.isPublicTransport() && ta.isCar()) {
                    int passengers = ta.getCarPassengers();
                    if (passengers>0) {
                        temp = temp + 5;
                    }
                    else {
                        temp = temp + 2.5;
                    }
                }
                else if (!ta.isWalking() && !ta.isBicycle() && ta.isPublicTransport() && ta.isCar()) {
                    int passengers = ta.getCarPassengers();
                    if (passengers>0) {
                        temp = temp + 6;
                    }
                    else {
                        temp = temp + 3.5;
                    }
                }
                else if ((ta.isWalking() || ta.isBicycle()) && ta.isPublicTransport() && ta.isCar()) {
                    int passengers = ta.getCarPassengers();
                    if (passengers>0) {
                        temp = temp + 7;
                    }
                    else {
                        temp = temp + 4.5;
                    }
                }
        }
        return temp;
    }
    public static double sumAllTAPoints(List<TransportAction> taList) {
        double points = 0;
        for (int i = 0; i<taList.size();i++) {
            points = points + getPoints(taList.get(i));
        }
        return points;
    }
    public void checkForBadge2(TransportAction ta, User u) {
        if (!u.getBadges().get(0)) {
            Database db = new Database();
            db.editBadge0(u, "TAController");
        }
        if (getPoints(ta)==10) {
            if (!u.getBadges().get(2)) {
                Database db = new Database();
                db.editBadge2(u);
            }
        }
    }
    public static ArrayList<Double> TAPointsForGraph(List<TransportAction> TAData) {
        ArrayList<Double> arr = new ArrayList<>();
        Date today = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = formatter.format(today);
        Date dateToCheck = new Date();
        for (int i = 0; i<TAData.size();i++) {
            double temp = 0;
            SustainableActionController sac = new SustainableActionController();
            try {
                dateToCheck = formatter.parse(TAData.get(i).getDate());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (sac.isDateInDates(dateToCheck, TAData.get(0).getDate(), todayStr)) {
                if (TAData.get(i).isNoTravelling()) {
                    temp = temp + 10;
                }
                else if (TAData.get(i).isWalking() && !TAData.get(i).isBicycle() && !TAData.get(i).isPublicTransport() && !TAData.get(i).isCar()) {
                    temp = temp + 10;
                }
                else if (TAData.get(i).isBicycle() && !TAData.get(i).isWalking() && !TAData.get(i).isPublicTransport() && !TAData.get(i).isCar()) {
                    temp = temp + 10;
                }
                else if (TAData.get(i).isPublicTransport() && !TAData.get(i).isBicycle() && !TAData.get(i).isWalking() && !TAData.get(i).isCar()) {
                    temp = temp + 7.5;
                }
                else if ((TAData.get(i).isWalking() || TAData.get(i).isBicycle()) && TAData.get(i).isPublicTransport() && !TAData.get(i).isCar()) {
                    temp = temp + 8.5;
                }
                else if (!TAData.get(i).isWalking() && !TAData.get(i).isBicycle() && !TAData.get(i).isPublicTransport() && TAData.get(i).isCar()) {
                    int passengers = TAData.get(i).getCarPassengers();
                    if (passengers>0) {
                        temp = temp + 5;
                    }
                    else {
                        temp = temp + 2.5;
                    }
                }
                else if (!TAData.get(i).isWalking() && !TAData.get(i).isBicycle() && TAData.get(i).isPublicTransport() && TAData.get(i).isCar()) {
                    int passengers = TAData.get(i).getCarPassengers();
                    if (passengers>0) {
                        temp = temp + 6;
                    }
                    else {
                        temp = temp + 3.5;
                    }
                }
                else if ((TAData.get(i).isWalking() || TAData.get(i).isBicycle()) && TAData.get(i).isPublicTransport() && TAData.get(i).isCar()) {
                    int passengers = TAData.get(i).getCarPassengers();
                    if (passengers>0) {
                        temp = temp + 7;
                    }
                    else {
                        temp = temp + 4.5;
                    }
                }
                arr.add(temp);
            }
        }
        return arr;
    }
}
