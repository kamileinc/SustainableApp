package com.example.sustainableapp.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.EGLConfig;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
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

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyResultsFragment extends Fragment {
    String userID;
    static ArrayList<SustainableAction> saList = new ArrayList<>();
    private static BooVariable saListReturned;
    static ArrayList<FoodAction> FAData  = null;
    static ArrayList<EnergyAction> EAData  = null;
    static ArrayList<TransportAction> TAData = null;
    private static IntVariable foundProfile;
    static ArrayList<User> profileData;
    private static Bitmap bitmap;
    private static BooVariable photoReturned;
    private static BooVariable FAPointsReturned;
    private static double FAPoints = 0;
    private static BooVariable EAPointsReturned;
    private static double EAPoints = 0;
    private static BooVariable TAPointsReturned;
    private static double TAPoints = 0;
    private static IntVariable allPointsReturned;
    private static double allPoints = 0;
    GraphView graph;
    TextView noData, food_tv, transport2_tv, energy_tv, badges_tv, total_tv, firstName2_tv, lastName2_tv, username2_tv, empty_tv;
    ImageView image_iv;
    LinearLayout linearLayout;
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
        FAData  = null;
        EAData  = null;
        TAData = null;
        getUsersSustainableActions();
        UserController uc = new UserController();
        uc.getProfile(userID, "MyResultsFragment");
        FoodActionController fac = new FoodActionController();
        fac.getAllFoodPoints(userID, "AllFA");
        EnergyActionController eac = new EnergyActionController();
        eac.getAllEnergyPoints(userID, "AllEA");
        TransportActionController tac = new TransportActionController();
        tac.getAllTransportPoints(userID, "AllTA");
        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
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
        FAPointsReturned = new BooVariable();
        FAPointsReturned.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {

                food_tv.setText("Mityba: "  + new DecimalFormat("##.##").format(FAPoints) + " taškai");
                //allPoints = allPoints + FAPoints;
                allPointsReturned.setNumber(allPointsReturned.getNumber()+1);
            }
        });
        EAPointsReturned = new BooVariable();
        EAPointsReturned.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {

                energy_tv.setText("Būstas: "  + new DecimalFormat("##.##").format(EAPoints) + " taškai");

                allPointsReturned.setNumber(allPointsReturned.getNumber()+1);
            }
        });
        TAPointsReturned = new BooVariable();
        TAPointsReturned.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {

                transport2_tv.setText("Transportas: "  + new DecimalFormat("##.##").format(TAPoints) + " taškai");
                //allPoints = allPoints + TAPoints;
                allPointsReturned.setNumber(allPointsReturned.getNumber()+1);
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
                    //firstName2_tv.setText(profileData.get(0).getFirstName());
                    username2_tv.setText(profileData.get(0).getUsername());
                    Log.i("mano", profileData.get(0).toString());
                    String badges = "dar neturite ženklelių";
                    if (foundProfile.getNumber()!=1) {
                        for (int i = 0; i < profileData.get(0).getBadges().size(); i++) {
                            if (profileData.get(0).getBadges().get(i) != false) {
                                Log.i("mano", "badge "+ i + ": " + profileData.get(0).getBadges().get(i));
                                linearLayout = view.findViewById(R.id.linearLayout2);
                                if (i == 0) {
                                    badges = "";
                                    String badgeText = "\n - Už pirmą užduoties išsaugojimą";
                                    drawBadge(view,1, badgeText);
                                }
                                if (i == 1) {
                                    badges = "";
                                    String badgeText = "\n - Už pirmą maksimalų taškų mitybos kategorijoje surinkimą";
                                    drawBadge(view,2, badgeText);

                                }
                                if (i == 2) {
                                    badges = "";
                                    String badgeText = "\n - Už pirmą maksimalų taškų transporto kategorijoje surinkimą";
                                    drawBadge(view,3, badgeText);
                                }
                                if (i == 3) {
                                    badges = "";
                                    String badgeText = "\n - Už pirmą maksimalų taškų būsto kategorijoje surinkimą";
                                    drawBadge(view,4, badgeText);
                                }


                            }
                        }
                        badges_tv.setText("Ženkleliai: " + badges);
                        foundProfile.setNumber(1);
                    }
                    /*
                    if (profileData.get(0).getBadges() != null) {
                        badges_tv.setText(profileData.get(0).getBadges().getOrDefault("id", ""));
                    }
                    */
                    //badges_tv.setText(profileData.get(0).getBadges().get(0));
                    uc.loadImageForView(profileData.get(0).getId(), profileData.get(0).getId() + ".jpg", purpose);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            uc.loadImageForView(profileData.get(0).getId(), profileData.get(0).getId() + ".jpg", purpose);
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
                        noData.setVisibility(View.GONE);
                        ArrayList<Double> arr = fac.FAPointsForGraph(FAData);
                        String[] dates = new String[]{ FAData.get(0).getDate(), FAData.get(1).getDate(), FAData.get(2).getDate(), FAData.get(3).getDate(), FAData.get(4).getDate(), FAData.get(5).getDate(), FAData.get(6).getDate(), ""};
                        drawFAgraph(view, arr, arr.size(), dates);
                    }
                    else {
                        empty_tv.setVisibility(View.VISIBLE);
                    }
                }
                else if (selectedTabPosition==2) {
                    setVisibilityForGraphTab(view);
                    if (TAData != null) {
                        noData.setVisibility(View.GONE);
                        ArrayList<Double> arr = tac.TAPointsForGraph(TAData);

                        String[] dates = new String[]{ TAData.get(0).getDate(), TAData.get(1).getDate(), TAData.get(2).getDate(), TAData.get(3).getDate(), TAData.get(4).getDate(), TAData.get(5).getDate(), TAData.get(6).getDate(), ""};

                        drawFAgraph(view, arr, arr.size(), dates);
                    }
                    else {
                        empty_tv.setVisibility(View.VISIBLE);
                    }

                }
                else if (selectedTabPosition==3) {
                    setVisibilityForGraphTab(view);
                    if (EAData != null) {
                        noData.setVisibility(View.GONE);
                        ArrayList<Double> arr = eac.EAPointsForGraph(EAData);

                        String[] dates = new String[]{ EAData.get(0).getDate(), EAData.get(1).getDate(), EAData.get(2).getDate(), EAData.get(3).getDate(), EAData.get(4).getDate(), EAData.get(5).getDate(), EAData.get(6).getDate(), ""};

                        drawFAgraph(view, arr, arr.size(), dates);
                    }
                    else {
                        empty_tv.setVisibility(View.VISIBLE);
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
                    for (int i = (saList.size()-1); i> (saList.size()-3); i--) {
                        try {
                            Log.i("mano", "for loop: " + i);
                            if (saList.get(i).getCategory().equals("Food")) {
                                FoodActionController fac = new FoodActionController();
                                fac.getFAForMyResults(userID, "MyResultsFragment");
                            } else if (saList.get(i).getCategory().equals("Transport")) {
                                TransportActionController tac = new TransportActionController();
                                tac.getTAForMyResults(userID, "MyResultsFragment");
                            } else if (saList.get(i).getCategory().equals("Energy")) {
                                EnergyActionController eac = new EnergyActionController();
                                eac.getEAForMyResults(userID, "MyResultsFragment");
                            }
                        }
                        catch (Exception e) {

                        }
                    }

                }
            }
        });

        allPointsReturned = new IntVariable();
        allPointsReturned.setListener(new IntVariable.ChangeListener() {
            @Override
            public void onChange() {
                if (allPointsReturned.getNumber()==3) {
                    allPoints = EAPoints + TAPoints + FAPoints;
                    total_tv.setText("Iš viso: "  + new DecimalFormat("##.##").format(allPoints) + " taškai");
                    allPoints = 0;
                    allPointsReturned.setNumber(0);
                }
            }});
        Button share_b = (Button) view.findViewById(R.id.share_b);
        share_b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.i("mano", "share button pressed");
                takeScreenshot();

            }
        });
        return view;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        graph = (GraphView) view.findViewById(R.id.graph);
        graph.setVisibility(View.INVISIBLE);
        food_tv = view.findViewById(R.id.food_tv);
        transport2_tv = view.findViewById(R.id.transport2_tv);
        energy_tv = view.findViewById(R.id.energy_tv);
        badges_tv = view.findViewById(R.id.badges_tv);
        total_tv = view.findViewById(R.id.total_tv);
        username2_tv = view.findViewById(R.id.username2_tv);
        image_iv = view.findViewById(R.id.image_iv);
        empty_tv = view.findViewById(R.id.empty_tv);
        linearLayout = view.findViewById(R.id.linearLayout2);

        setVisibilityForAllTab(view);
    }
    public void setVisibilityForAllTab(View view) {
        noData.setVisibility(View.GONE);
        empty_tv.setVisibility(View.GONE);
        graph.setVisibility(View.GONE);
        food_tv.setVisibility(View.VISIBLE);
        transport2_tv.setVisibility(View.VISIBLE);
        energy_tv.setVisibility(View.VISIBLE);
        badges_tv.setVisibility(View.VISIBLE);
        total_tv.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
    }
    public void setVisibilityForGraphTab(View view) {
        noData.setVisibility(View.VISIBLE);
        empty_tv.setVisibility(View.GONE);
        graph.setVisibility(View.GONE);
        food_tv.setVisibility(View.INVISIBLE);
        transport2_tv.setVisibility(View.INVISIBLE);
        energy_tv.setVisibility(View.INVISIBLE);
        badges_tv.setVisibility(View.INVISIBLE);
        total_tv.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);
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
    }
    public static void checkFAPoints(double points) {
        FAPoints = points;
        FAPointsReturned.setBoo(true);
        if (FAPointsReturned.getListener() != null) {
            FAPointsReturned.getListener().onChange();
        }
    }
    public static void checkEAPoints(double points) {
        EAPoints = points;
        EAPointsReturned.setBoo(true);
        if (EAPointsReturned.getListener() != null) {
            EAPointsReturned.getListener().onChange();
        }
    }

    public static void checkTAPoints(double points) {
        TAPoints = points;
        TAPointsReturned.setBoo(true);
        if (TAPointsReturned.getListener() != null) {
            TAPointsReturned.getListener().onChange();
        }
    }

    public static void checkEANotFound(List<EnergyAction> list) {
        Log.i("mano", "neradom EA..........................");
    }
    public static void checkEAFound(List<EnergyAction> list) {
        Log.i("mano", "radom EA............" + list.size() + "...."  +  list.get(0).toString());
        EAData = (ArrayList<EnergyAction>) list;
    }
    public static void checkTANotFound(List<TransportAction> list) {
        Log.i("mano", "neradom TA..........................");
    }
    public static void checkTAFound(List<TransportAction> list) {
        Log.i("mano", "radom TA............" + list.size() + "...."  +  list.get(0).toString());
        TAData = (ArrayList<TransportAction>) list;
    }
    public void drawFAgraph(View view, ArrayList<Double> arr, int numberOfActivity, String[] dates) {
        graph.setVisibility(View.VISIBLE);
        Log.i("mano", "onviewcreated graph: " + graph);
        try {
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
            graph.removeAllSeries();
            graph.addSeries(series);
            graph.getViewport().setMinX(1);
            graph.getViewport().setMaxX(8);
            graph.getViewport().setMinY(0.0);
            graph.getViewport().setMaxY(10.0);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getGridLabelRenderer().setVerticalAxisTitle("Taškai");
            graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);
            graph.getGridLabelRenderer().setNumHorizontalLabels(8);
            graph.getGridLabelRenderer().setHorizontalAxisTitle("Data");
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
           // Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getActivity().getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
            Log.i("mano", "share open screenshot");
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
    private void openScreenshot(File imageFile) {

        Log.i("mano", "share open screenshot pradzia");
        String filePath = imageFile.getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        ShareDialog shareDialog;
        FacebookSdk.sdkInitialize(getApplicationContext());
        shareDialog = new ShareDialog(this);

        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("Title")
                .setContentDescription(
                        "\"Body Of Test Post\"")
                .setContentUrl(Uri.parse("http://someurl.com/here"))
                .build();

        shareDialog.show(content);
        Log.i("mano", "share open screenshot pabaiga");

    }
    public void drawBadge(View view, int badgeNumber, String badgeText) {
        LinearLayout ll = new LinearLayout(view.getContext());
        ll.setOrientation(linearLayout.HORIZONTAL);
        ImageView imageview = new ImageView(view.getContext());
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(150, 150);
        if (badgeNumber==1) {
            imageview.setImageResource(R.drawable.badge1);
        }
        else if (badgeNumber==2) {
            imageview.setImageResource(R.drawable.badge2);
        }
        else if (badgeNumber==3) {
            imageview.setImageResource(R.drawable.badge3);
        }
        if (badgeNumber==4) {
            imageview.setImageResource(R.drawable.badge4);
        }
        imageview.setLayoutParams(params);
        TextView textView = new TextView(view.getContext());
        textView.setTextColor(Color.BLACK);
        textView.setText(badgeText);
        ll.addView(imageview);
        ll.addView(textView);
        linearLayout.addView(ll);

    }
}
