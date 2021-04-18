package com.example.sustainableapp.views;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sustainableapp.R;
import com.example.sustainableapp.controllers.EnergyActionController;
import com.example.sustainableapp.controllers.FoodActionController;
import com.example.sustainableapp.controllers.TransportActionController;
import com.example.sustainableapp.controllers.UserController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.IntVariable;
import com.example.sustainableapp.models.Points;
import com.example.sustainableapp.models.User;
import com.google.android.material.tabs.TabLayout;
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
    private static BooVariable photosReturned;
    ProgressDialog dialog;
    public AllResultsFragment() {
    }
    public static AllResultsFragment newInstance() {
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
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_results, container, false);
        dialog = new ProgressDialog(view.getContext());
        dialog.setMessage("Kraunama informacija...");
        dialog.show();
        pointsList = new ArrayList<>();
        UserController uc = new UserController();
        uc.getAllUsers();
        TabLayout tabs = view.findViewById(R.id.tabs);
        foundUsers = new IntVariable();
        foundUsers.setListener(() -> {
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
        });
        FAPointsReturned = new BooVariable();
        FAPointsReturned.setListener(() -> allPointsReturned.setNumber(allPointsReturned.getNumber()+1));
        EAPointsReturned = new BooVariable();
        EAPointsReturned.setListener(() -> allPointsReturned.setNumber(allPointsReturned.getNumber()+1));
        TAPointsReturned = new BooVariable();
        TAPointsReturned.setListener(() -> allPointsReturned.setNumber(allPointsReturned.getNumber()+1));
        allPointsReturned = new IntVariable();
        allPointsReturned.setListener(() -> {
            if (allPointsReturned.getNumber()>=(usersList.size()*4)) {
                int temp =0;
                for (int i = 0; i< usersList.size();i++){
                    if (pointsList.get(i).getEaPoints()==-1 || pointsList.get(i).getFaPoints()==-1 || pointsList.get(i).getTaPoints()==-1 || pointsList.get(i).getBitmap()== null) {
                        temp++;
                    }
                }
                if (temp==0) {
                    pointsList.sort(new Points.SortByTotal());
                    drawTableForCategory(view, "Bendra");
                    allPointsReturned.setNumber(0);
                }
            }
        });
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedTabPosition = tabs.getSelectedTabPosition();
                if (selectedTabPosition==0) {
                    pointsList.sort(new Points.SortByTotal());
                    drawTableForCategory(view, "Bendra");
                }
                else if (selectedTabPosition==1) {
                    pointsList.sort(new Points.SortByFA());
                    drawTableForCategory(view, "Mityba");
                }
                else if (selectedTabPosition==2) {
                    pointsList.sort(new Points.SortByTA());
                    drawTableForCategory(view, "Transportas");
                }
                else if (selectedTabPosition==3) {
                    pointsList.sort(new Points.SortByEA());
                    drawTableForCategory(view, "Būstas");
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        photoReturned = new BooVariable();
        photoReturned.setListener(() -> {
            if (photoReturned.isBoo()) {
                allPointsReturned.setNumber(allPointsReturned.getNumber()+1);
            }
        });
        photosReturned = new BooVariable();
        photosReturned.setListener(() -> {
            if (photoReturned.isBoo()) {
                allPointsReturned.setNumber(allPointsReturned.getNumber()+1);
            }
        });
        return view;
    }
    public static void checkAllUsersFound(List<User> list) {
        usersList = (ArrayList<User>) list;
        if (!list.isEmpty()) {
            foundUsers.setID(String.valueOf(list.size()));
            if (foundUsers.getListener() != null) {
                foundUsers.getListener().onChange();
            }
        }
    }
    public static void checkFAPointsForUser(double points, String userID) {
        for (int i =0;i<pointsList.size();i++) {
            if (pointsList.get(i).getUserID().equals(userID)) {
                pointsList.get(i).setFaPoints(points);
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
    @SuppressLint("ResourceType")
    public void drawTableForCategory(View view, String category) {
        dialog.hide();
        if (pointsList!=null) {
            TableLayout tl = view.findViewById(R.id.main_table);
            tl.removeAllViews();
            TextView[] textArray = new TextView[pointsList.size()+7];
            TableRow[] tr_head = new TableRow[pointsList.size()+1];
            for(int i=0; i<=pointsList.size();i++){
                if (i == 0) {
                    tr_head[i] = new TableRow(view.getContext());
                    tr_head[i].setId(10);
                    tr_head[i].setBackgroundColor(0xFF6200EE);
                    tr_head[i].setLayoutParams(new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.WRAP_CONTENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
                    textArray[i] = new TextView(view.getContext());
                    textArray[i].setId(i + 333);
                    textArray[i].setText(getString(R.string.place));
                    textArray[i].setTextColor(Color.WHITE);
                    textArray[i].setPadding(30, 5, 30, 5);
                    tr_head[i].addView(textArray[i]);
                    textArray[i+1] = new TextView(view.getContext());
                    textArray[i+1].setId(i + 444);
                    textArray[i+1].setText(getString(R.string.photo));
                    textArray[i+1].setTextColor(Color.WHITE);
                    textArray[i+1].setPadding(30, 5, 30, 5);
                    tr_head[i].addView(textArray[i+1]);
                    textArray[i+2] = new TextView(view.getContext());
                    textArray[i+2].setId(i + 444);
                    textArray[i+2].setText(getString(R.string.username));
                    textArray[i+2].setTextColor(Color.WHITE);
                    textArray[i+2].setPadding(30, 5, 30, 5);
                    tr_head[i].addView(textArray[i+2]);
                    textArray[i+3] = new TextView(view.getContext());
                    textArray[i+3].setId(i + 444);
                    textArray[i+3].setText(category);
                    textArray[i+3].setTextColor(Color.WHITE);
                    textArray[i+3].setPadding(30, 5, 30, 5);
                    tr_head[i].addView(textArray[i+3]);
                }
                else {
                    if (i%2==0) {
                        tr_head[i] = new TableRow(view.getContext());
                        tr_head[i].setId(i + 1);
                        tr_head[i].setBackgroundColor(R.color.purple_500);
                        tr_head[i].setLayoutParams(new ViewGroup.LayoutParams(
                                TableLayout.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                    }
                    else {
                        tr_head[i] = new TableRow(view.getContext());
                        tr_head[i].setId(i + 1);
                        tr_head[i].setBackgroundColor(Color.WHITE);
                        tr_head[i].setLayoutParams(new ViewGroup.LayoutParams(
                                TableLayout.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                    }
                    textArray[i] = new TextView(view.getContext());
                    textArray[i].setId(i + 111);
                    String userPlace = Integer.toString(i);
                    textArray[i].setText(userPlace);
                    textArray[i].setTextColor(Color.BLACK);
                    textArray[i].setPadding(30, 5, 30, 5);
                    if (pointsList.get(i-1).getUserID().equals(userID)) {
                        textArray[i].setTypeface(null, Typeface. BOLD);
                        TextView user_place_tv = view.findViewById(R.id.user_place_tv);
                        String userGetsAPlace = getString(R.string.inThisCategory) + " " +  i + " " +  getString(R.string.usersPlace);
                        user_place_tv.setText(userGetsAPlace);
                    }
                    else {
                        textArray[i].setTypeface(null, Typeface. NORMAL);
                    }
                    tr_head[i].addView(textArray[i]);
                    ImageView iv = new ImageView(view.getContext());
                    iv.setImageBitmap(pointsList.get(i-1).getBitmap());
                    tr_head[i].addView(iv, 100, 100);
                    textArray[i] = new TextView(view.getContext());
                    textArray[i].setId(i + 111);
                    textArray[i].setText(pointsList.get(i-1).getUserName());
                    textArray[i].setTextColor(Color.BLACK);
                    textArray[i].setPadding(30, 5, 30, 5);
                    if (pointsList.get(i-1).getUserID().equals(userID)) {
                        textArray[i].setTypeface(null, Typeface. BOLD);
                    }
                    else {
                        textArray[i].setTypeface(null, Typeface. NORMAL);
                    }
                    tr_head[i].addView(textArray[i]);
                    String points = "";
                    switch (category) {
                        case "Mityba":
                            points = new DecimalFormat("##.##").format(pointsList.get(i - 1).getFaPoints());
                            break;
                        case "Būstas":
                            points = new DecimalFormat("##.##").format(pointsList.get(i - 1).getEaPoints());
                            break;
                        case "Transportas":
                            points = new DecimalFormat("##.##").format(pointsList.get(i - 1).getTaPoints());

                            break;
                        case "Bendra":
                            points = new DecimalFormat("##.##").format(pointsList.get(i - 1).getFaPoints() + pointsList.get(i - 1).getEaPoints() + pointsList.get(i - 1).getTaPoints());

                            break;
                    }
                    textArray[i] = new TextView(view.getContext());
                    textArray[i].setId(i + 222);
                    textArray[i].setText(points);
                    textArray[i].setTextColor(Color.BLACK);
                    textArray[i].setPadding(30, 5, 30, 5);
                    if (pointsList.get(i-1).getUserID().equals(userID)) {
                        textArray[i].setTypeface(null, Typeface. BOLD);
                    }
                    else {
                        textArray[i].setTypeface(null, Typeface. NORMAL);
                    }
                    tr_head[i].addView(textArray[i]);
                }
                tl.addView(tr_head[i], new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
            }
        }
        else {
            Toast.makeText(view.getContext(), "Nėra duomenų", Toast.LENGTH_LONG).show();
        }
    }

}