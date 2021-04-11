package com.example.sustainableapp.controllers;

import android.app.Application;
import android.util.Log;
import android.view.View;

import com.example.sustainableapp.models.Database;
import com.example.sustainableapp.models.EnergyAction;
import com.example.sustainableapp.models.FoodAction;
import com.example.sustainableapp.models.SustainableAction;
import com.example.sustainableapp.models.TransportAction;
import com.example.sustainableapp.models.User;
import com.example.sustainableapp.views.AllResultsFragment;
import com.example.sustainableapp.views.EnergyActionFragment;
import com.example.sustainableapp.views.FoodActionFragment;
import com.example.sustainableapp.views.MyResultsFragment;
import com.example.sustainableapp.views.TransportActionFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FoodActionController extends Application {
    public static void checkBadge1Edited() {
        FoodActionFragment.checkBadge1Edited();
    }
    public static void checkBadge0Edited() {
        FoodActionFragment.checkBadge0Edited();
    }
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
                    -1, -1, -1);
            db.saveFA(fa);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
    public boolean getFAForFAFragment(String userId, String purpose) {
        Database db = new Database();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String dateStr = formatter.format(date);
        final String id = userId + dateStr;
        Log.i("mano", "FA ID: "+ id);
        db.getFADataForFAFragment(id, purpose);
        return true;
    }
    public boolean getFAForMyResults(String userID, String purpose) {
        Database db = new Database();
        db.getFADataForMyResults(userID, purpose);
        return true;
    }
    public static void checkFANotFound(List<FoodAction> list, String userID, String purpose) {
        if (purpose.equals("FoodActionFragment")) {
            FoodActionFragment.checkFANotFound(list);
        }
        else if (purpose.equals("MyResultsFragment")) {
            MyResultsFragment.checkFANotFound(list);
        }
        else if (purpose.equals("AllFA")) {
            //MyResultsFragment.checkFANotFound(list);
            double points = 0;
            MyResultsFragment.checkFAPoints(points);
        }
        else if (purpose.equals("AllResults")) {
            //MyResultsFragment.checkFANotFound(list);
            double points = 0;
            AllResultsFragment.checkFAPointsForUser(points, userID);
        }


    }
    public static void checkFAFound(List<FoodAction> list, String userID, String purpose) {
        if (purpose.equals("FoodActionFragment")) {
            FoodActionFragment.checkFAFound(list);
        }
        else if (purpose.equals("MyResultsFragment")) {
            MyResultsFragment.checkFAFound(list);
        }
        else if (purpose.equals("AllFA")) {
            double points = 0;
            points = sumAllFAPoints( list);
            MyResultsFragment.checkFAPoints(points);
        }
        else if (purpose.equals("AllResults")) {
            double points = 0;
            points = sumAllFAPoints( list);
            AllResultsFragment.checkFAPointsForUser(points, userID);
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
    public static double sumAllFAPoints(List<FoodAction> faList) {
        double points = 0;
        if (faList != null) {
            ArrayList<Double> arr = new ArrayList<Double>();
            for (int i = 0; i<faList.size();i++) {
                double temp = 0;
                    if (faList.get(i).getBreakfastFood()==0) {
                        temp = temp + 10;
                    } else if (faList.get(i).getBreakfastFood()==1) {
                        temp = temp + 7.5;
                    } else if (faList.get(i).getBreakfastFood()==2) {
                        temp = temp + 5;
                    } else if (faList.get(i).getBreakfastFood()==3) {
                        temp = temp + 2.5;
                    }

                    if (faList.get(i).getLunchFood()==0) {
                        temp = temp + 10;
                    } else if (faList.get(i).getLunchFood()==1) {
                        temp = temp + 7.5;
                    } else if (faList.get(i).getLunchFood()==2) {
                        temp = temp + 5;
                    } else if (faList.get(i).getLunchFood()==3) {
                        temp = temp + 2.5;
                    }

                    if (faList.get(i).getDinnerFood()==0) {
                        temp = temp + 10;
                    } else if (faList.get(i).getDinnerFood()==1) {
                        temp = temp + 7.5;
                    } else if (faList.get(i).getDinnerFood()==2) {
                        temp = temp + 5;
                    } else if (faList.get(i).getDinnerFood()==3) {
                        temp = temp + 2.5;
                    }
                    if (temp != 0) {
                        temp = temp / 3;
                    }
                    arr.add(temp);
                    points= points + temp;
            }
        }
        return points;
    }
    public static ArrayList<Double> FAPointsForGraph(List<FoodAction> FAData) {
        ArrayList<Double> arr = new ArrayList<Double>();
        //int numberOfActivity = 0;
        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = formatter.format(today);
        Date dateToCheck = new Date();

        for (int i = 0; i<FAData.size();i++) {
            double temp = 0;
            SustainableActionController sac = new SustainableActionController();
            try {
                dateToCheck = formatter.parse(FAData.get(i).getDate());
            } catch (Exception e) {

            }
            if (sac.isDateInDates(dateToCheck, FAData.get(0).getDate(), todayStr)) {
                //numberOfActivity++;
                if (FAData.get(i).getBreakfastFood()==0) {
                    temp = temp + 10;
                } else if (FAData.get(i).getBreakfastFood()==1) {
                    temp = temp + 7.5;
                } else if (FAData.get(i).getBreakfastFood()==2) {
                    temp = temp + 5;
                } else if (FAData.get(i).getBreakfastFood()==3) {
                    temp = temp + 2.5;
                }

                if (FAData.get(i).getLunchFood()==0) {
                    temp = temp + 10;
                } else if (FAData.get(i).getLunchFood()==1) {
                    temp = temp + 7.5;
                } else if (FAData.get(i).getLunchFood()==2) {
                    temp = temp + 5;
                } else if (FAData.get(i).getLunchFood()==3) {
                    temp = temp + 2.5;
                }

                if (FAData.get(i).getDinnerFood()==0) {
                    temp = temp + 10;
                } else if (FAData.get(i).getDinnerFood()==1) {
                    temp = temp + 7.5;
                } else if (FAData.get(i).getDinnerFood()==2) {
                    temp = temp + 5;
                } else if (FAData.get(i).getDinnerFood()==3) {
                    temp = temp + 2.5;
                }
                if (temp != 0) {
                    temp = temp / 3;
                }
                arr.add(temp);
            }
        }
        return arr;
    }
    public void checkForBadge1(FoodAction fa, User u) {
        double temp = 0;
        double points = 0;
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
        points= points + temp;
        if (u.getBadges().get(0)==false) {
            //if user does not have badge0
            //give badge0
            Database db = new Database();
            db.editBadge0(u, "FAController");
        }
        if (points == 10) {

            if (u.getBadges().get(1)==false) {
                //if user does not have badge1
                //give badge1
                Database db = new Database();
                db.editBadge1(u);
            }
        }
    }

}
