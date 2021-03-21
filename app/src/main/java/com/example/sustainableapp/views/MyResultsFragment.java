package com.example.sustainableapp.views;

import android.content.Context;
import android.graphics.Color;
import android.opengl.EGLConfig;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sustainableapp.MainActivity;
import com.example.sustainableapp.R;
import com.example.sustainableapp.controllers.FoodActionController;
import com.example.sustainableapp.controllers.SustainableActionController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.FoodAction;
import com.example.sustainableapp.models.IntVariable;
import com.example.sustainableapp.models.SustainableAction;
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
    GraphView graph;
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

                    }
                    else if (saList.get(0).getCategory().equals("Energy")) {

                    }
                }
            }
        });

        foundFA = new IntVariable();
        foundFA.setListener(new IntVariable.ChangeListener() {
            @Override
            public void onChange() {
                Log.i("mano", "cia");
                if (FAData != null) {
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
                            temp = temp/3;
                            arr.add(temp);
                        }
                    }
                        drawFAgraph(view, arr, numberOfActivity);
                }
            }});

        return view;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        graph = (GraphView) view.findViewById(R.id.graph);
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
    public void drawFAgraph(View view, ArrayList<Double> arr, int numberOfActivity) {
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

            staticLabelsFormatter.setHorizontalLabels(new String[]{ FAData.get(0).getDate(), FAData.get(1).getDate(), FAData.get(2).getDate(), FAData.get(3).getDate(), FAData.get(4).getDate(), FAData.get(5).getDate(), FAData.get(6).getDate(), ""});
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
}
