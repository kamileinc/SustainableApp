package com.example.sustainableapp.views;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sustainableapp.R;
import com.example.sustainableapp.controllers.EnergyActionController;
import com.example.sustainableapp.controllers.FoodActionController;
import com.example.sustainableapp.controllers.SustainableActionController;
import com.example.sustainableapp.controllers.TransportActionController;
import com.example.sustainableapp.controllers.UserController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.FoodAction;
import com.example.sustainableapp.models.IntVariable;
import com.example.sustainableapp.models.Points;
import com.example.sustainableapp.models.SustainableAction;
import com.example.sustainableapp.models.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class AllResultsFragment extends Fragment {
    String userID;
    static ArrayList<User> usersList;
    private static IntVariable foundUsers;
    static ArrayList<Points> pointsList = new ArrayList<>();
    private static BooVariable FAPointsReturned;
    private static BooVariable EAPointsReturned;
    private static BooVariable TAPointsReturned;
    private static IntVariable allPointsReturned;
    private static BooVariable photoReturned;
    TableLayout t1;
    public AllResultsFragment() {
        // Required empty public constructor
    }

    public static AllResultsFragment newInstance(String param1, String param2) {
        AllResultsFragment fragment = new AllResultsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userID = bundle.getString("userID", "0");
            Log.i("mano", "user id: " + userID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_results, container, false);
        pointsList = new ArrayList<>();
        UserController uc = new UserController();
        uc.getAllUsers();

        foundUsers = new IntVariable();
        foundUsers.setListener(new IntVariable.ChangeListener() {
            @Override
            public void onChange() {
                Log.i("mano", "RASTI USERS: " + usersList.size());
                if (pointsList.size()==0) {
                    for (int i = 0; i < usersList.size(); i++) {
                        pointsList.add(new Points(usersList.get(i).getId(), usersList.get(i).getUsername(), usersList.get(i).getPhoto()));
                        FoodActionController fac = new FoodActionController();
                        String purpose = "AllResults";
                        fac.getAllFoodPoints(usersList.get(i).getId(), purpose);

                        TransportActionController tac = new TransportActionController();
                        tac.getAllTransportPoints(usersList.get(i).getId(), purpose);

                        EnergyActionController eac = new EnergyActionController();
                        eac.getAllEnergyPoints(usersList.get(i).getId(), purpose);

                        uc.loadImageForView(usersList.get(i).getId() ,usersList.get(i).getId() + ".jpg", purpose);

                    }
                }
                Log.i("mano", "POINTS LIST: " + pointsList.size() + ",  " + pointsList.get(0).getEaPoints());
            }});
        FAPointsReturned = new BooVariable();
        FAPointsReturned.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                allPointsReturned.setNumber(allPointsReturned.getNumber()+1);
            }
        });
        EAPointsReturned = new BooVariable();
        EAPointsReturned.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                allPointsReturned.setNumber(allPointsReturned.getNumber()+1);
            }
        });
        TAPointsReturned = new BooVariable();
        TAPointsReturned.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                allPointsReturned.setNumber(allPointsReturned.getNumber()+1);
            }
        });

        allPointsReturned = new IntVariable();
        allPointsReturned.setListener(new IntVariable.ChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onChange() {
                if (allPointsReturned.getNumber()>=(usersList.size()*4)) {
                    Log.i("mano", "visi SA gauti");
                    int temp =0;
                    for (int i = 0; i< usersList.size();i++){
                        //Log.i("mano", "POINTS: " + pointsList.get(i).toString());
                        if (pointsList.get(i).getEaPoints()==-1 || pointsList.get(i).getFaPoints()==-1 || pointsList.get(i).getTaPoints()==-1 || pointsList.get(i).getBitmap()== null) {
                            temp++;
                        }
                    }
                    if (temp==0) {
                        // parodyt rez lentelej


                        TableLayout tl = (TableLayout) view.findViewById(R.id.main_table);
                        /*
                        TableRow tr_head = new TableRow(view.getContext());
                        tr_head.setId(10);
                        tr_head.setBackgroundColor(Color.GRAY);        // part1
                        tr_head.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        TextView label_hello = new TextView(view.getContext());
                        label_hello.setId(20);
                        label_hello.setText("HELLO");
                        label_hello.setTextColor(Color.WHITE);          // part2
                        label_hello.setPadding(5, 5, 5, 5);
                        tr_head.addView(label_hello);// add the column to the table row here

                        TextView label_android = new TextView(view.getContext());    // part3
                        label_android.setId(21);// define id that must be unique
                        label_android.setText("ANDROID..!!"); // set the text for the header
                        label_android.setTextColor(Color.WHITE); // set the color
                        label_android.setPadding(5, 5, 5, 5); // set the padding (if required)
                        tr_head.addView(label_android); // add the column to the table row here
                        tl.addView(tr_head, new TableLayout.LayoutParams(
                                ViewGroup.LayoutParams.FILL_PARENT,                    //part4
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        */
                        TextView[] textArray = new TextView[7];
                        TableRow[] tr_head = new TableRow[pointsList.size()+1];

                        for(int i=0; i<=pointsList.size();i++){
                            //JSONObject product = productsList.getJSONObject(i);
                            //JSONObject productData = product.getJSONObject("Product");
                            //String productDescription = productData.getString("description");
                            if (i == 0) {
                                tr_head[i] = new TableRow(view.getContext());
                                tr_head[i].setId(10);

                                tr_head[i].setBackgroundColor(Color.GRAY);        // part1
                                tr_head[i].setLayoutParams(new TableLayout.LayoutParams(
                                        TableLayout.LayoutParams.MATCH_PARENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT));
                                textArray[i] = new TextView(view.getContext());
                                textArray[i].setId(i + 333);
                                textArray[i].setText("Vartotojo vardas");
                                textArray[i].setTextColor(Color.WHITE);
                                textArray[i].setPadding(30, 5, 30, 5);
                                tr_head[i].addView(textArray[i]);
/*
                                tr_head[i+1] = new TableRow(view.getContext());
                                tr_head[i+1].setId(16);

                                tr_head[i+1].setBackgroundColor(Color.GRAY);        // part1
                                tr_head[i+1].setLayoutParams(new TableLayout.LayoutParams(
                                        TableLayout.LayoutParams.MATCH_PARENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT));
                                        */
                                textArray[i+1] = new TextView(view.getContext());
                                textArray[i+1].setId(i + 444);
                                textArray[i+1].setText("Mityba");
                                textArray[i+1].setTextColor(Color.WHITE);
                                textArray[i+1].setPadding(30, 5, 30, 5);
                                tr_head[i].addView(textArray[i+1]);

                                textArray[i+2] = new TextView(view.getContext());
                                textArray[i+2].setId(i + 444);
                                textArray[i+2].setText("Transportas");
                                textArray[i+2].setTextColor(Color.WHITE);
                                textArray[i+2].setPadding(30, 5, 30, 5);
                                tr_head[i].addView(textArray[i+2]);

                                textArray[i+3] = new TextView(view.getContext());
                                textArray[i+3].setId(i + 444);
                                textArray[i+3].setText("Būstas");
                                textArray[i+3].setTextColor(Color.WHITE);
                                textArray[i+3].setPadding(30, 5, 30, 5);
                                tr_head[i].addView(textArray[i+3]);

                                textArray[i+4] = new TextView(view.getContext());
                                textArray[i+4].setId(i + 444);
                                textArray[i+4].setText("Bendras");
                                textArray[i+4].setTextColor(Color.WHITE);
                                textArray[i+4].setPadding(30, 5, 30, 5);
                                tr_head[i].addView(textArray[i+4]);

                                tl.addView(tr_head[i], new TableLayout.LayoutParams(
                                        TableLayout.LayoutParams.MATCH_PARENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT));
                            }
                            else {
                                //Create the tablerows
                                tr_head[i] = new TableRow(view.getContext());
                                tr_head[i].setId(i + 1);
                                tr_head[i].setBackgroundColor(Color.GRAY);
                                tr_head[i].setLayoutParams(new ViewGroup.LayoutParams(
                                        TableLayout.LayoutParams.MATCH_PARENT,
                                        TableRow.LayoutParams.WRAP_CONTENT));

                                // Here create the TextView dynamically

                                textArray[i] = new TextView(view.getContext());
                                textArray[i].setId(i + 111);
                                textArray[i].setText(pointsList.get(i-1).getUserName());
                                textArray[i].setTextColor(Color.WHITE);
                                textArray[i].setPadding(30, 5, 30, 5);
                                tr_head[i].addView(textArray[i]);


                                textArray[i] = new TextView(view.getContext());
                                textArray[i].setId(i + 222);
                                textArray[i].setText(new DecimalFormat("##.##").format(pointsList.get(i-1).getFaPoints()));
                                textArray[i].setTextColor(Color.WHITE);
                                textArray[i].setPadding(30, 5, 30, 5);
                                tr_head[i].addView(textArray[i]);

                                textArray[i] = new TextView(view.getContext());
                                textArray[i].setId(i + 222);
                                textArray[i].setText(new DecimalFormat("##.##").format(pointsList.get(i-1).getTaPoints()));
                                textArray[i].setTextColor(Color.WHITE);
                                textArray[i].setPadding(30, 5, 30, 5);
                                tr_head[i].addView(textArray[i]);


                                textArray[i] = new TextView(view.getContext());
                                textArray[i].setId(i + 222);
                                textArray[i].setText(new DecimalFormat("##.##").format(pointsList.get(i-1).getEaPoints()));
                                textArray[i].setTextColor(Color.WHITE);
                                textArray[i].setPadding(30, 5, 30, 5);
                                tr_head[i].addView(textArray[i]);

                                textArray[i] = new TextView(view.getContext());
                                textArray[i].setId(i + 222);
                                textArray[i].setText("NEZINAU");
                                textArray[i].setTextColor(Color.WHITE);
                                textArray[i].setPadding(30, 5, 30, 5);
                                tr_head[i].addView(textArray[i]);




                                tl.addView(tr_head[i], new TableLayout.LayoutParams(
                                        TableLayout.LayoutParams.MATCH_PARENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT));
                            }

                        } // end of for loop
                        for (int i = 0; i< usersList.size();i++){
                            Log.i("mano", "POINTS: " + pointsList.get(i).toString());

                        }
                        allPointsReturned.setNumber(0);
                    }
                }
            }});
        photoReturned = new BooVariable();
        photoReturned.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                if (photoReturned.isBoo()) {
                    allPointsReturned.setNumber(allPointsReturned.getNumber()+1);
                }
            }
        });
        //GAUTI VISUS VARTOTOJUS
        //GAUTI VISUS SA
        //GAUTI VISUS FA
        //GAUTI VISUS TA
        //GAUTI VISUS EA
        return view;
    }
    public static void checkAllUsersFound(List<User> list) {

        usersList = (ArrayList<User>) list;
        if (!list.isEmpty()) {
            foundUsers.setID(String.valueOf(list.size()));
            if (foundUsers.getListener() != null) {
                foundUsers.getListener().onChange();
                list = null;
            }
        }
    }
    public static void checkFAPointsForUser(double points, String userID) {
        for (int i =0;i<pointsList.size();i++) {
            if (pointsList.get(i).getUserID().equals(userID)) {
                pointsList.get(i).setFaPoints(points);
                Log.i("mano", "settina " + points +" fa points to: " + pointsList.get(i).getUserName());
            }
        }
        FAPointsReturned.setBoo(true);
        if (FAPointsReturned.getListener() != null) {
            FAPointsReturned.getListener().onChange();
        }
    }

    public static void checkEAPointsForUser(double points, String userID) {
        for (int i =0;i<pointsList.size();i++) {
            if (pointsList.get(i).getUserID().equals(userID)) {
                pointsList.get(i).setEaPoints(points);
                Log.i("mano", "settina " + points +" ea points to: " + pointsList.get(i).getUserName());
            }
        }
        EAPointsReturned.setBoo(true);
        if (EAPointsReturned.getListener() != null) {
            EAPointsReturned.getListener().onChange();
        }
    }
    public static void checkTAPointsForUser(double points, String userID) {
        for (int i =0;i<pointsList.size();i++) {
            if (pointsList.get(i).getUserID().equals(userID)) {
                pointsList.get(i).setTaPoints(points);
                Log.i("mano", "settina " + points +" Ta points to: " + pointsList.get(i).getUserName());
            }
        }
        TAPointsReturned.setBoo(true);
        if (TAPointsReturned.getListener() != null) {
            TAPointsReturned.getListener().onChange();
        }
    }
    public static void checkPhotoReturned(String userID, Bitmap bmp) {
        for (int i =0;i<pointsList.size();i++) {
            if (pointsList.get(i).getUserID().equals(userID)) {
                pointsList.get(i).setBitmap(bmp);
            }
        }
        photoReturned.setBoo(true);
        photoReturned.getListener().onChange();

    }


}