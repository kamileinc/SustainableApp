package com.example.sustainableapp.views;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sustainableapp.R;
import com.example.sustainableapp.controllers.EnergyActionController;
import com.example.sustainableapp.controllers.FactController;
import com.example.sustainableapp.controllers.SustainableActionController;
import com.example.sustainableapp.controllers.UserController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.EnergyAction;
import com.example.sustainableapp.models.IntVariable;
import com.example.sustainableapp.models.SustainableAction;
import com.example.sustainableapp.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EnergyActionFragment extends Fragment {
    TextView min_tv, s_tv;
    EditText min_etn, s_etn, devices_etn;
    CheckBox shower_cb, bath_cb, noWater_cb;
    Button save_energy_action_b;
    String userID;
    private static IntVariable foundEA;
    static ArrayList<EnergyAction> EAData;
    private static BooVariable actionEdited;
    private static BooVariable badge3Edited;
    private static BooVariable badge0Edited;
    ArrayList<String> errors = new ArrayList<>();
    static ArrayList<SustainableAction> saList = new ArrayList<>();
    private static BooVariable saListReturned;
    SustainableAction sa = new SustainableAction();
    private static IntVariable foundProfile;
    static ArrayList<User> profileData;
    public EnergyActionFragment() {
        // Required empty public constructor
    }

    public static EnergyActionFragment newInstance(String param1, String param2) {
        EnergyActionFragment fragment = new EnergyActionFragment();
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
        View view = inflater.inflate(R.layout.fragment_energy_action, container, false);
        getUsersSustainableActions();
        UserController uc = new UserController();
        uc.getProfile(userID, "EAFragment");
        foundProfile = new IntVariable();
        foundProfile.setListener(new IntVariable.ChangeListener() {
            @Override
            public void onChange() {
                Log.i("mano", "cia");
                if (profileData != null) {
                }
                else {
                }
            }});
        actionEdited = new BooVariable();
        actionEdited.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                //toast
                Toast.makeText(view.getContext(),"Sėkmingai išsaugoti pakeitimai",Toast. LENGTH_SHORT).show();
                //ijungt menu komponenta is naujo
                getActivity().findViewById(R.id.ic_tasks).performClick();
            }});
        badge3Edited = new BooVariable();
        badge3Edited.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                //toast
                Toast.makeText(view.getContext(), "Valio! Surinkote maksimalų taškų skaičių būsto srityje pirmą kartą, todėl gaunate ženklelį!", Toast.LENGTH_LONG).show();
            }});

        badge0Edited = new BooVariable();
        badge0Edited.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                //toast
                Toast.makeText(view.getContext(), "Valio! Išsaugojote savo pirmąją užduotį, todėl gaunate ženklelį!", Toast.LENGTH_SHORT).show();
            }});

        shower_cb =(CheckBox) view.findViewById(R.id.shower_cb);
        shower_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shower_cb.isChecked()) {
                    setVisibleShowerFields(view);
                } else {
                    setInvisibleShowerFields(view);
                }
            }
                });
        noWater_cb =(CheckBox) view.findViewById(R.id.noWater_cb);
        noWater_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noWater_cb.isChecked()) {
                    setInvisibleShowerAndBathFields(view);
                } else {
                    setVisibleShowerAndBathFields(view);
                }
            }
        });
        save_energy_action_b = (Button) view.findViewById(R.id.save_energy_action_b);
        save_energy_action_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sa != null) {
                   // Log.i("mano", "DABARTINIS SA: min:  " + min_etn.getText());
                    String showerTime = "0:0";
                    String devices = "0";
                    boolean shower = shower_cb.isChecked();
                    boolean bath = bath_cb.isChecked();
                    if (noWater_cb.isChecked()) {
                        bath = false;
                        shower = false;
                    }
                    else {
                        if (shower_cb.isChecked()) {
                            if (!min_etn.getText().toString().equals("")) {
                                showerTime = min_etn.getText().toString();
                            } else {
                                showerTime = "0";
                            }
                            showerTime = showerTime + ":";
                            if (!s_etn.getText().toString().equals("")) {
                                showerTime = showerTime + s_etn.getText().toString();
                            } else {
                                showerTime = showerTime + "0";
                            }
                        }
                    }
                    if (devices_etn.getText().toString().length() != 0) {
                        devices = devices_etn.getText().toString();
                    }
                    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date(System.currentTimeMillis());
                    String dateStr = formatter.format(date);
                    EnergyAction ea = new EnergyAction(sa.getId(), sa.getCategory(), sa.getUserID(), sa.getDateBegin(), sa.getDateEnd(), dateStr, noWater_cb.isChecked(), shower, showerTime, bath, devices);
                    EnergyActionController eac = new EnergyActionController();
                    errors = (ArrayList<String>) eac.validateEA(ea);
                    int howManyErr = 0;
                    for (int i = 0; i < errors.size(); i++) {
                        if (!errors.get(i).equals("")) {
                            howManyErr = howManyErr + 1;
                        }
                    }
                    showErrors();
                    if (howManyErr == 0) {
                        //eac.addEnergyActionToDB(ea);
                        //UPDATE
                        Log.i("mano", "UPDATE, ERRORS: " + errors.get(0) +errors.get(1) +errors.get(2));
                        eac.updateEnergyActionInDB(ea);
                        eac.checkForBadge3(ea, profileData.get(0));

                    }
                }}
        });
        saListReturned = new BooVariable();
        saListReturned.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                if (saListReturned.isBoo()) {
                    Log.i("mano", "sa kiekis: " + saList.size() + "OBJ: " + saList.get(0));

                    //jei data atitinka dabartine, rodyt faktus
                    String beginDate = saList.get((saList.size()-1)).getDateBegin();
                    String endDate = saList.get((saList.size()-1)).getDateEnd();
                    SustainableActionController sac = new SustainableActionController();
                    if (sac.isTodayInDates(beginDate, endDate)) {
                        sa = saList.get((saList.size()-1));
                    }
                }

            }
        });
        EnergyActionController eac = new EnergyActionController();
        eac.getEAForEAFragment(userID, "EnergyActionFragment");
        foundEA = new IntVariable();
        foundEA.setListener(new IntVariable.ChangeListener() {
            @Override
            public void onChange() {
                Log.i("mano", "cia");
                if (EAData != null) {
                    String[] sArr = EAData.get(0).getShowerTime().split(":", 5);
                    //int m1 = Integer.parseInt(sArr[0]);
                    //int s1 = Integer.parseInt(sArr[1]);
                    min_etn.setText(sArr[0]);
                    s_etn.setText(sArr[1]);
                    if (EAData.get(0).isNoWater()) {
                        noWater_cb.setChecked(true);
                    }
                    if (noWater_cb.isChecked()) {
                        setInvisibleShowerAndBathFields(view);
                    }
                    if (EAData.get(0).isShower()) {
                        shower_cb.setChecked(true);
                        setVisibleShowerFields(view);
                    }
                    else {
                        shower_cb.setChecked(false);
                        setInvisibleShowerFields(view);
                    }
                    if (EAData.get(0).isBath()) {
                        bath_cb.setChecked(true);
                    }
                    else {
                        bath_cb.setChecked(false);
                    }
                    devices_etn.setText(EAData.get(0).getDevicesOff());
                }
                else {
                }
            }});
        return view;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        min_etn = getView().findViewById(R.id.min_etn);
        s_etn = getView().findViewById(R.id.s_etn);
        min_tv = getView().findViewById(R.id.min_tv);
        s_tv = getView().findViewById(R.id.s_tv);
        devices_etn = getView().findViewById(R.id.devices_etn);
        save_energy_action_b = getView().findViewById(R.id.save_energy_action_b);
        //shower_cb = getView().findViewById(R.id.shower_cb);
        noWater_cb = getView().findViewById(R.id.noWater_cb);
        bath_cb = getView().findViewById(R.id.bath_cb);
        setInvisibleShowerFields(view);
    }
    public void setInvisibleShowerAndBathFields(View view) {
        setInvisibleShowerFields(view);
        shower_cb.setVisibility(View.INVISIBLE);
        bath_cb.setVisibility(View.INVISIBLE);
    }
    public void setVisibleShowerAndBathFields(View view) {
        setVisibleShowerFields(view);
        shower_cb.setVisibility(View.VISIBLE);
        bath_cb.setVisibility(View.VISIBLE);
    }
    public void setVisibleShowerFields(View view) {
        min_etn.setVisibility(View.VISIBLE);
        s_etn.setVisibility(View.VISIBLE);
        min_tv.setVisibility(View.VISIBLE);
        s_tv.setVisibility(View.VISIBLE);
    }
    public void setInvisibleShowerFields(View view) {
        min_etn.setVisibility(View.INVISIBLE);
        s_etn.setVisibility(View.INVISIBLE);
        min_tv.setVisibility(View.INVISIBLE);
        s_tv.setVisibility(View.INVISIBLE);
    }
    public void getUsersSustainableActions() {
        SustainableActionController sac = new SustainableActionController();
        String purpose = "EnergyActionFragment";
        sac.getUsersSustainableActions(userID, purpose);
    }
    public static void checkUsersSAFound(ArrayList<SustainableAction> sa) {
        Log.i("mano", "radom:" + sa.size());
        saList = sa;
        saListReturned.setBoo(true);
        saListReturned.getListener().onChange();

    }
    public static void checkUsersSANotFound(ArrayList<SustainableAction> sa) {
        Log.i("mano", "neradom:" + sa.size());
        saList = sa;
        saListReturned.setBoo(false);
        saListReturned.getListener().onChange();

    }
    public void showErrors() {
        if (!errors.get(0).equals("")) {
            min_etn.setError(errors.get(0));
        }
        if (!errors.get(1).equals("")) {
            s_etn.setError(errors.get(1));
        }
        if (!errors.get(2).equals("")) {
            devices_etn.setError(errors.get(2));
        }
    }
    public static void checkEAEdited() {
        actionEdited.setBoo(true);
        if (actionEdited.getListener() != null) {
            actionEdited.getListener().onChange();
        }
    }
    public static void checkBadge3Edited() {
        badge3Edited.setBoo(true);
        if (badge3Edited.getListener() != null) {
            badge3Edited.getListener().onChange();
        }
    }
    public static void checkBadge0Edited() {
        badge0Edited.setBoo(true);
        if (badge0Edited.getListener() != null) {
            badge0Edited.getListener().onChange();
        }
    }
    public static void checkEANotFound(List<EnergyAction> list) {
        Log.i("mano", "neradom");
    }
    public static void checkEAFound(List<EnergyAction> list) {
        Log.i("mano", "radom " + list.size() + "...."  +  list.get(0).toString());
        EAData = (ArrayList<EnergyAction>) list;
        if (!list.isEmpty()) {
            foundEA.setID(list.get(0).getId());
            if (foundEA.getListener() != null) {
                foundEA.getListener().onChange();
                list = null;
            }
        }
        else {
            checkEANotFound(list);
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
}