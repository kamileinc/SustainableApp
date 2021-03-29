package com.example.sustainableapp.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.ColorRes;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sustainableapp.R;
import com.example.sustainableapp.controllers.EnergyActionController;
import com.example.sustainableapp.controllers.FoodActionController;
import com.example.sustainableapp.controllers.SustainableActionController;
import com.example.sustainableapp.controllers.TransportActionController;
import com.example.sustainableapp.controllers.UserController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.Database;
import com.example.sustainableapp.models.FoodAction;
import com.example.sustainableapp.models.IntVariable;
import com.example.sustainableapp.models.Points;
import com.example.sustainableapp.models.SustainableAction;
import com.example.sustainableapp.models.User;
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
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
    private static ArrayList<Bitmap> bitmapList = new ArrayList<>();
    private static ArrayList<String> userIDList = new ArrayList<>();
    ProgressDialog dialog;
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
        dialog = new ProgressDialog(view.getContext());
        dialog.setMessage("Kraunama informacija...");
        dialog.show();
        pointsList = new ArrayList<>();
        UserController uc = new UserController();
        uc.getAllUsers();
        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
        foundUsers = new IntVariable();
        foundUsers.setListener(new IntVariable.ChangeListener() {
            @Override
            public void onChange() {
                Log.i("mano", "RASTI USERS: " + usersList.size());
                if (pointsList.size()==0) {
                    //UserController uc = new UserController();
                   // uc.getAllPhotos(usersList, "AllResultsTable");
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
                        Collections.sort(pointsList, new Points.SortByTotal());
                        drawTableForCategory(view, "Bendra");
                        allPointsReturned.setNumber(0);
                    }
                }
            }});
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedTabPosition = tabs.getSelectedTabPosition();
                if (selectedTabPosition==0) {
                    Collections.sort(pointsList, new Points.SortByTotal());
                    drawTableForCategory(view, "Bendra");
                }
                else if (selectedTabPosition==1) {
                    Collections.sort(pointsList, new Points.SortByFA());
                    drawTableForCategory(view, "Mityba");
                }
                else if (selectedTabPosition==2) {
                    Collections.sort(pointsList, new Points.SortByTA());
                    drawTableForCategory(view, "Transportas");
                }
                else if (selectedTabPosition==3) {
                    Collections.sort(pointsList, new Points.SortByEA());
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
        photoReturned.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                if (photoReturned.isBoo()) {
                    allPointsReturned.setNumber(allPointsReturned.getNumber()+1);
                }
            }
        });
        photosReturned = new BooVariable();
        photosReturned.setListener(new BooVariable.ChangeListener() {
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
    public static void checkAllPhotosReturned(ArrayList<Bitmap> bmpList, ArrayList<String> uList) {
        bitmapList = bmpList;
        userIDList = uList;
        photosReturned.setBoo(true);
        photosReturned.getListener().onChange();

    }
    /*
@SuppressLint("ResourceType")
public void drawTable(View view) {
    TableLayout tl = (TableLayout) view.findViewById(R.id.main_table);

    TextView[] textArray = new TextView[7];
    TableRow[] tr_head = new TableRow[pointsList.size()+1];

    for(int i=0; i<=pointsList.size();i++){
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
}
*/
    @SuppressLint("ResourceType")
    public void drawTableForCategory(View view, String category) {
        dialog.hide();
        if (pointsList!=null) {
            Log.i("mano", "pointslistas dabar: " + pointsList.size());
            TableLayout tl = (TableLayout) view.findViewById(R.id.main_table);
            tl.removeAllViews();
            TextView[] textArray = new TextView[pointsList.size()+7];
            TableRow[] tr_head = new TableRow[pointsList.size()+1];

            for(int i=0; i<=pointsList.size();i++){
                if (i == 0) {
                    tr_head[i] = new TableRow(view.getContext());
                    tr_head[i].setId(10);

                    tr_head[i].setBackgroundColor(0xFF6200EE);        // part1
                    // tr_head[i].setLayoutParams(new TableLayout.LayoutParams(200,200));

                    tr_head[i].setLayoutParams(new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.WRAP_CONTENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));


                    textArray[i] = new TextView(view.getContext());
                    textArray[i].setId(i + 333);
                    textArray[i].setText("Vieta");
                    textArray[i].setTextColor(Color.WHITE);
                    textArray[i].setPadding(30, 5, 30, 5);
                    tr_head[i].addView(textArray[i]);

                    textArray[i+1] = new TextView(view.getContext());
                    textArray[i+1].setId(i + 444);
                    textArray[i+1].setText("Nuotrauka");
                    textArray[i+1].setTextColor(Color.WHITE);
                    textArray[i+1].setPadding(30, 5, 30, 5);
                    tr_head[i].addView(textArray[i+1]);

                    textArray[i+2] = new TextView(view.getContext());
                    textArray[i+2].setId(i + 444);
                    textArray[i+2].setText("Vartotojo vardas");
                    textArray[i+2].setTextColor(Color.WHITE);
                    textArray[i+2].setPadding(30, 5, 30, 5);
                    tr_head[i].addView(textArray[i+2]);

                    textArray[i+3] = new TextView(view.getContext());
                    textArray[i+3].setId(i + 444);
                    textArray[i+3].setText(category);
                    textArray[i+3].setTextColor(Color.WHITE);
                    textArray[i+3].setPadding(30, 5, 30, 5);
                    tr_head[i].addView(textArray[i+3]);

                    tl.addView(tr_head[i], new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.WRAP_CONTENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
                }
                else {
                    //Create the tablerows

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
                    // Here create the TextView dynamically

                    textArray[i] = new TextView(view.getContext());
                    textArray[i].setId(i + 111);
                    textArray[i].setText(Integer.toString(i));
                    textArray[i].setTextColor(Color.BLACK);
                    textArray[i].setPadding(30, 5, 30, 5);
                    if (pointsList.get(i-1).getUserID().equals(userID)) {
                        textArray[i].setTypeface(null, Typeface. BOLD);
                        TextView user_place_tv = view.findViewById(R.id.user_place_tv);
                        user_place_tv.setText("❁❁❁ Šioje kategorijoje jūs užimate " + i + " vietą! ❁❁❁");
                    }
                    else {
                        textArray[i].setTypeface(null, Typeface. NORMAL);
                    }
                    tr_head[i].addView(textArray[i]);

                    ImageView iv = new ImageView(view.getContext());
                /*
                Log.i("mano", "userIDList: " + userIDList.size());
                Log.i("mano", "usersList: " + usersList.size());
                Log.i("mano", "bitmapList: " + bitmapList.size());
                for (int j = 0; j<userIDList.size(); j++) {
                    if (userIDList.get(j).equals(usersList.get((i-1)).getId())) {
                        iv.setImageBitmap(bitmapList.get(j));
                    }
                }
                */
                    //iv.setImageResource(R.drawable.badge0);
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
                    if (category.equals("Mityba")) {
                        points = new DecimalFormat("##.##").format(pointsList.get(i-1).getFaPoints());
                    }
                    else if (category.equals("Būstas")) {
                        points = new DecimalFormat("##.##").format(pointsList.get(i-1).getEaPoints());
                    }
                    else if (category.equals("Transportas")) {
                        points = new DecimalFormat("##.##").format(pointsList.get(i-1).getTaPoints());

                    }
                    else if (category.equals("Bendra")) {
                        points = new DecimalFormat("##.##").format(pointsList.get(i-1).getFaPoints() + pointsList.get(i-1).getEaPoints() + pointsList.get(i-1).getTaPoints());

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



                    tl.addView(tr_head[i], new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.WRAP_CONTENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
                }

            } // end of for loop
            for (int i = 0; i< usersList.size();i++){
                Log.i("mano", "POINTS: " + pointsList.get(i).toString());

            }
        }
        else {
            Toast.makeText(view.getContext(), "Nera duomenu", Toast.LENGTH_LONG).show();

        }

    }

}