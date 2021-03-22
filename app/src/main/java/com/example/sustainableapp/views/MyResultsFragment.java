package com.example.sustainableapp.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.EGLConfig;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sustainableapp.MainActivity;
import com.example.sustainableapp.R;
import com.example.sustainableapp.controllers.EnergyActionController;
import com.example.sustainableapp.controllers.FoodActionController;
import com.example.sustainableapp.controllers.SustainableActionController;
import com.example.sustainableapp.controllers.TransportActionController;
import com.example.sustainableapp.controllers.UserController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.EnergyAction;
import com.example.sustainableapp.models.FoodAction;
import com.example.sustainableapp.models.IntVariable;
import com.example.sustainableapp.models.SustainableAction;
import com.example.sustainableapp.models.TransportAction;
import com.example.sustainableapp.models.User;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class MyResultsFragment extends Fragment {
    String userID;
    static ArrayList<SustainableAction> saList = new ArrayList<>();
    private static BooVariable saListReturned;
    private static IntVariable foundFA;
    static ArrayList<FoodAction> FAData;
    private static IntVariable foundEA;
    static ArrayList<EnergyAction> EAData;
    private static IntVariable foundTA;
    static ArrayList<TransportAction> TAData;
    private static IntVariable foundProfile;
    static ArrayList<User> profileData;
    private static Bitmap bitmap;
    private static BooVariable photoReturned;
    GraphView graph;
    TextView noData, food_tv, transport2_tv, energy_tv, badges_tv, total_tv, firstName2_tv, lastName2_tv;
    ImageView image_iv;
    public MyResultsFragment() {
        // Required empty public constructor
    }

    public static MyResultsFragment newInstance(String param1, String param2) {
        MyResultsFragment fragment = new MyResultsFragment();
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
        View view =  inflater.inflate(R.layout.fragment_my_results, container, false);
        getUsersSustainableActions();
        UserController uc = new UserController();
        uc.getProfile(userID, "MyResultsFragment");
        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
        TabItem food_tab = view.findViewById(R.id.food_tab);
        TabItem all_tab = view.findViewById(R.id.all_tab);
        TabItem transport_tab = view.findViewById(R.id.transport_tab);
        TabItem energy_tab = view.findViewById(R.id.energy_tab);
        noData = view.findViewById(R.id.noData_tv);
        photoReturned = new BooVariable();
        photoReturned.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {

                if (photoReturned.isBoo()) {
                    image_iv.setImageBitmap(bitmap);
                    //photo_iv.setImageBitmap(Bitmap.createScaledBitmap(bitmap, photo_iv.getWidth(), photo_iv.getHeight(), false));

                }
            }
        });
        foundProfile = new IntVariable();
        foundProfile.setListener(new IntVariable.ChangeListener() {
            @Override
            public void onChange() {
                Log.i("mano", "cia");
                if (profileData != null) {
                    // Toast.makeText(getContext(), "Prekės rastos: " + productsForList.size(), Toast.LENGTH_LONG).show();
                    //ProductController pc = new ProductController();
                    //al = pc.formatProductListForFarmer(productsForList);
                    UserController uc = new UserController();
                    String purpose = "MyResultsFragment";
                    Log.i("mano", profileData.get(0).toString());
                    firstName2_tv.setText(profileData.get(0).getFirstName());
                    lastName2_tv.setText(profileData.get(0).getLastName());
                    uc.loadImageForView(profileData.get(0).getId() + ".jpg", purpose);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            uc.loadImageForView(profileData.get(0).getId() + ".jpg", purpose);
                            //photo_iv.setMaxHeight(100);
                            //photo_iv.setMaxWidth(100);
                        }
                    }, 1000);   //1 second

                    Log.i("mano", "turejo priskirt");
                }
                else {
                }
            }});
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedTabPosition = tabs.getSelectedTabPosition();
                Log.i("mano", "pasirinko " + selectedTabPosition + "TAB'ą");
                if (selectedTabPosition==0) {
                    setVisibilityForAllTab(view);
                }
                else if (selectedTabPosition==1) {
                    setVisibilityForGraphTab(view);
                    if (FAData != null) {
                        noData.setVisibility(View.INVISIBLE);
                        ArrayList<Double> arr = new ArrayList<Double>();
                        int numberOfActivity = 0;
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
                                numberOfActivity++;
                                if (FAData.get(i).getBreakfastFood().equals("0")) {
                                    temp = temp + 10;
                                } else if (FAData.get(i).getBreakfastFood().equals("1")) {
                                    temp = temp + 7.5;
                                } else if (FAData.get(i).getBreakfastFood().equals("2")) {
                                    temp = temp + 5;
                                } else if (FAData.get(i).getBreakfastFood().equals("3")) {
                                    temp = temp + 2.5;
                                }

                                if (FAData.get(i).getLunchFood().equals("0")) {
                                    temp = temp + 10;
                                } else if (FAData.get(i).getLunchFood().equals("1")) {
                                    temp = temp + 7.5;
                                } else if (FAData.get(i).getLunchFood().equals("2")) {
                                    temp = temp + 5;
                                } else if (FAData.get(i).getLunchFood().equals("3")) {
                                    temp = temp + 2.5;
                                }

                                if (FAData.get(i).getDinnerFood().equals("0")) {
                                    temp = temp + 10;
                                } else if (FAData.get(i).getDinnerFood().equals("1")) {
                                    temp = temp + 7.5;
                                } else if (FAData.get(i).getDinnerFood().equals("2")) {
                                    temp = temp + 5;
                                } else if (FAData.get(i).getDinnerFood().equals("3")) {
                                    temp = temp + 2.5;
                                }
                                if (temp != 0) {
                                    temp = temp / 3;
                                }
                                arr.add(temp);
                            }
                        }
                        String[] dates = new String[]{ FAData.get(0).getDate(), FAData.get(1).getDate(), FAData.get(2).getDate(), FAData.get(3).getDate(), FAData.get(4).getDate(), FAData.get(5).getDate(), FAData.get(6).getDate(), ""};
                        drawFAgraph(view, arr, numberOfActivity, dates);
                    }
                }
                else if (selectedTabPosition==2) {
                    setVisibilityForGraphTab(view);
                    if (TAData != null) {
                        noData.setVisibility(View.INVISIBLE);
                        ArrayList<Double> arr = new ArrayList<Double>();
                        int numberOfActivity = 0;
                        Date today = new Date(System.currentTimeMillis());
                        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                        String todayStr = formatter.format(today);
                        Date dateToCheck = new Date();

                        for (int i = 0; i<TAData.size();i++) {
                            double temp = 0;
                            SustainableActionController sac = new SustainableActionController();
                            try {
                                dateToCheck = formatter.parse(TAData.get(i).getDate());
                            } catch (Exception e) {

                            }
                            if (sac.isDateInDates(dateToCheck, TAData.get(0).getDate(), todayStr)) {
                                numberOfActivity++;
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
                                    int passengers = Integer.parseInt(TAData.get(i).getCarPassengers());
                                    if (passengers>0) {
                                        temp = temp + 5;
                                    }
                                    else {
                                        temp = temp + 2.5;
                                    }
                                }
                                else if (!TAData.get(i).isWalking() && !TAData.get(i).isBicycle() && TAData.get(i).isPublicTransport() && TAData.get(i).isCar()) {
                                    int passengers = Integer.parseInt(TAData.get(i).getCarPassengers());
                                    if (passengers>0) {
                                        temp = temp + 6;
                                    }
                                    else {
                                        temp = temp + 3.5;
                                    }
                                }
                                else if ((TAData.get(i).isWalking() || TAData.get(i).isBicycle()) && TAData.get(i).isPublicTransport() && TAData.get(i).isCar()) {
                                    int passengers = Integer.parseInt(TAData.get(i).getCarPassengers());
                                    if (passengers>0) {
                                        temp = temp + 7;
                                    }
                                    else {
                                        temp = temp + 4.5;
                                    }
                                }
                                //////////////////////////////////
                                Log.i("mano", "temp: " + temp);
                                arr.add(temp);
                            }
                        }
                        String[] dates = new String[]{ TAData.get(0).getDate(), TAData.get(1).getDate(), TAData.get(2).getDate(), TAData.get(3).getDate(), TAData.get(4).getDate(), TAData.get(5).getDate(), TAData.get(6).getDate(), ""};

                        drawFAgraph(view, arr, numberOfActivity, dates);
                    }

                }
                else if (selectedTabPosition==3) {
                    setVisibilityForGraphTab(view);
                    if (EAData != null) {
                        noData.setVisibility(View.INVISIBLE);
                        ArrayList<Double> arr = new ArrayList<Double>();
                        int numberOfActivity = 0;
                        Date today = new Date(System.currentTimeMillis());
                        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                        String todayStr = formatter.format(today);
                        Date dateToCheck = new Date();

                        for (int i = 0; i<EAData.size();i++) {
                            double temp = 0;
                            SustainableActionController sac = new SustainableActionController();
                            try {
                                dateToCheck = formatter.parse(EAData.get(i).getDate());
                            } catch (Exception e) {

                            }
                            if (sac.isDateInDates(dateToCheck, EAData.get(0).getDate(), todayStr)) {
                                numberOfActivity++;

                                if (EAData.get(i).isNoWater()) {
                                    temp = temp + 10;
                                }
                                else if (EAData.get(i).isShower() && !EAData.get(i).isBath()) {
                                    String[] sArr = EAData.get(i).getShowerTime().split(":", 5);
                                    int m1 = Integer.parseInt(sArr[0]);
                                    //int s1 = Integer.parseInt(sArr[1]);
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
                                int devicesOff = Integer.parseInt(EAData.get(i).getDevicesOff());
                                if (devicesOff > 0 && devicesOff <= 5) {
                                    temp = temp + 5;
                                }
                                else if (devicesOff > 5 && devicesOff <= 8) {
                                    temp = temp + 7.5;
                                }
                                else if (devicesOff >10) {
                                    temp = temp + 10;
                                }
                                //////////////////////////////////
                                if (temp != 0) {
                                    temp = temp / 2;
                                }
                                arr.add(temp);
                            }
                        }
                        String[] dates = new String[]{ EAData.get(0).getDate(), EAData.get(1).getDate(), EAData.get(2).getDate(), EAData.get(3).getDate(), EAData.get(4).getDate(), EAData.get(5).getDate(), EAData.get(6).getDate(), ""};

                        drawFAgraph(view, arr, numberOfActivity, dates);
                    }

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        saListReturned = new BooVariable();
        saListReturned.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                if (saListReturned.isBoo()) {
                    Log.i("mano", "sa kiekis: " + saList.size() + "OBJ: " + saList.get(0).getCategory());
                    if (saList.get(0).getCategory().equals("Food")) {
                        FoodActionController fac = new FoodActionController();
                        fac.getFAForMyResults(userID, "MyResultsFragment");
                    }
                    else if (saList.get(0).getCategory().equals("Transport")) {
                        TransportActionController tac = new TransportActionController();
                        tac.getTAForMyResults(userID, "MyResultsFragment");
                    }
                    else if (saList.get(0).getCategory().equals("Energy")) {
                        EnergyActionController eac = new EnergyActionController();
                        eac.getEAForMyResults(userID, "MyResultsFragment");
                    }
                }
            }
        });

        foundFA = new IntVariable();
        foundFA.setListener(new IntVariable.ChangeListener() {
            @Override
            public void onChange() {
                Log.i("mano", "cia");

            }});
        foundEA = new IntVariable();
        foundEA.setListener(new IntVariable.ChangeListener() {
            @Override
            public void onChange() {
                Log.i("mano", "cia");
            }});

        foundTA = new IntVariable();
        foundTA.setListener(new IntVariable.ChangeListener() {
            @Override
            public void onChange() {
                Log.i("mano", "cia");

            }});

        return view;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        graph = (GraphView) view.findViewById(R.id.graph);
        graph.setVisibility(View.INVISIBLE);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        TabItem food_tab = view.findViewById(R.id.food_tab);
        TabItem all_tab = view.findViewById(R.id.all_tab);
        TabItem transport_tab = view.findViewById(R.id.transport_tab);
        TabItem energy_tab = view.findViewById(R.id.energy_tab);
        food_tv = view.findViewById(R.id.food_tv);
        transport2_tv = view.findViewById(R.id.transport2_tv);
        energy_tv = view.findViewById(R.id.energy_tv);
        badges_tv = view.findViewById(R.id.badges_tv);
        total_tv = view.findViewById(R.id.total_tv);
        firstName2_tv = view.findViewById(R.id.firstName2_tv);
        lastName2_tv = view.findViewById(R.id.lastName2_tv);
        image_iv = view.findViewById(R.id.image_iv);
        //firstName2_tv.setVisibility(View.GONE);
        //lastName2_tv.setVisibility(View.GONE);
        //image_iv.setVisibility(View.GONE);

        setVisibilityForAllTab(view);
    }
    public void setVisibilityForAllTab(View view) {
        noData.setVisibility(View.GONE);
        graph.setVisibility(View.GONE);
        food_tv.setVisibility(View.VISIBLE);
        transport2_tv.setVisibility(View.VISIBLE);
        energy_tv.setVisibility(View.VISIBLE);
        badges_tv.setVisibility(View.VISIBLE);
        total_tv.setVisibility(View.VISIBLE);
        //firstName2_tv.setVisibility(View.GONE);
    }
    public void setVisibilityForGraphTab(View view) {
        noData.setVisibility(View.VISIBLE);
        graph.setVisibility(View.GONE);
        food_tv.setVisibility(View.INVISIBLE);
        transport2_tv.setVisibility(View.INVISIBLE);
        energy_tv.setVisibility(View.INVISIBLE);
        badges_tv.setVisibility(View.INVISIBLE);
        total_tv.setVisibility(View.INVISIBLE);
    }
    public void getUsersSustainableActions() {
        SustainableActionController sac = new SustainableActionController();
        String purpose = "MyResultsFragment";
        sac.getUsersSustainableActions(userID, purpose);
    }
    public static void checkUsersSAFound(ArrayList<SustainableAction> sa) {
        Log.i("mano", "radom sa ACTIVITY:" + sa.size());
        saList = sa;
        saListReturned.setBoo(true);
        saListReturned.getListener().onChange();

    }
    public static void checkUsersSANotFound(ArrayList<SustainableAction> sa) {
        Log.i("mano", "neradom sa ACTIVITY:" + sa.size());
        saList = sa;
        saListReturned.setBoo(false);
        saListReturned.getListener().onChange();

    }
    public static void checkFANotFound(List<FoodAction> list) {
        Log.i("mano", "neradom FA..........................");
    }
    public static void checkFAFound(List<FoodAction> list) {
        Log.i("mano", "radom FA............" + list.size() + "...."  +  list.get(0).toString());
        FAData = (ArrayList<FoodAction>) list;
        if (!list.isEmpty()) {
            foundFA.setID(String.valueOf(list.size()));
            if (foundFA.getListener() != null) {
                foundFA.getListener().onChange();
                list = null;
            }
        }
        else {
            checkFANotFound(list);
        }
    }
    public static void checkEANotFound(List<EnergyAction> list) {
        Log.i("mano", "neradom EA..........................");
    }
    public static void checkEAFound(List<EnergyAction> list) {
        Log.i("mano", "radom EA............" + list.size() + "...."  +  list.get(0).toString());
        EAData = (ArrayList<EnergyAction>) list;
        if (!list.isEmpty()) {
            foundEA.setID(String.valueOf(list.size()));
            if (foundEA.getListener() != null) {
                foundEA.getListener().onChange();
                list = null;
            }
        }
        else {
            checkEANotFound(list);
        }
    }
    public static void checkTANotFound(List<TransportAction> list) {
        Log.i("mano", "neradom TA..........................");
    }
    public static void checkTAFound(List<TransportAction> list) {
        Log.i("mano", "radom TA............" + list.size() + "...."  +  list.get(0).toString());
        TAData = (ArrayList<TransportAction>) list;
        if (!list.isEmpty()) {
            foundTA.setID(String.valueOf(list.size()));
            if (foundTA.getListener() != null) {
                foundTA.getListener().onChange();
                list = null;
            }
        }
        else {
            checkTANotFound(list);
        }
    }
    public void drawFAgraph(View view, ArrayList<Double> arr, int numberOfActivity, String[] dates) {
        graph.setVisibility(View.VISIBLE);
        Log.i("mano", "onviewcreated graph: " + graph);
        try {
            /*
            LineGraphSeries<DataPoint> series = new LineGraphSeries < > (new DataPoint[] {
                    new DataPoint(1, fa1),
                    new DataPoint(2, fa2),
                    new DataPoint(3, fa3),
                    new DataPoint(4, fa4),
                    new DataPoint(5, fa5),
                    new DataPoint(6, fa6),
                    new DataPoint(7, fa7),
            });

             */
            DataPoint[] dp = new DataPoint[numberOfActivity];
            for (int i=0;i<numberOfActivity;i++) {
                dp[i] = new DataPoint((i+1), arr.get(i));
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries < > (dp);
            series.setDrawDataPoints(true);
            series.setColor(Color.BLACK);
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(10);
            series.setThickness(8);

            graph.addSeries(series);
            graph.getViewport().setMinX(1);
            graph.getViewport().setMaxX(8);
            graph.getViewport().setMinY(0.0);
            graph.getViewport().setMaxY(10.0);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setXAxisBoundsManual(true);

            //graph.setTitle("pavadinimas");
            graph.getGridLabelRenderer().setVerticalAxisTitle("vertical axis title");
            graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);
            graph.getGridLabelRenderer().setNumHorizontalLabels(8);
            graph.getGridLabelRenderer().setHorizontalAxisTitle("Axis");
            graph.getGridLabelRenderer().setTextSize(34);
            graph.getGridLabelRenderer().setGridColor(Color.BLUE);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);

            staticLabelsFormatter.setHorizontalLabels(dates);
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            GridLabelRenderer renderer = graph.getGridLabelRenderer();
            renderer.setHorizontalLabelsAngle(90);
            renderer.setLabelHorizontalHeight(190);

            graph.setVisibility(View.VISIBLE);
            Log.i("mano", "onviewcreated graph: TRY:" + graph.getSeries());
        } catch (IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.i("mano", "onviewcreated graph: CATCH:" + graph);
        }
    }
    public static void checkUserFound(List<User> list) {
        Log.i("mano", "radom " + list.size() + "...."  +  list.get(0).toString());
        profileData = (ArrayList<User>) list;
        if (!list.isEmpty()) {
            foundProfile.setID(list.get(0).getId());
            if (foundProfile.getListener() != null) {
                foundProfile.getListener().onChange();
                list = null;
            }
        }
        else {
            checkUserNotFound(list);
        }
    }

    public static void checkUserNotFound(List<User> list) {
        //errors = err;
        Log.i("mano", "neradom");
        //notFoundUser.getListener().onChange();
    }
    public static void checkPhotoReturned(Bitmap bmp) {
        Log.i("mano", "radom:" + photoReturned);
        bitmap = bmp;
        photoReturned.setBoo(true);
        photoReturned.getListener().onChange();

    }
}
