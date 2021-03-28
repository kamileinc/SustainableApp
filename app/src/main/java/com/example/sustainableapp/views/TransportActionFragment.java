package com.example.sustainableapp.views;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sustainableapp.R;
import com.example.sustainableapp.controllers.EnergyActionController;
import com.example.sustainableapp.controllers.SustainableActionController;
import com.example.sustainableapp.controllers.TransportActionController;
import com.example.sustainableapp.controllers.UserController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.EnergyAction;
import com.example.sustainableapp.models.IntVariable;
import com.example.sustainableapp.models.SustainableAction;
import com.example.sustainableapp.models.TransportAction;
import com.example.sustainableapp.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransportActionFragment extends Fragment {
    String userID;
    CheckBox noTravelling_cb, walking_cb, bicycling_cb, publicTransport_cb, drivingCar_cb;
    EditText walking_etn, bicycling_etn, publicTransport_etn, drivingCar_etn, givingALift_etn, people_etn;
    TextView walkingKm_tv, bicyclingKm_tv, publicTransportKm_tv,drivingCarKm_tv, drivingCar_tv, drivingCar_tv2, drivingCarKm_tv2, drivingCarPeople_tv;
    static ArrayList<SustainableAction> saList = new ArrayList<>();
    private static BooVariable saListReturned;
    SustainableAction sa = new SustainableAction();
    private static IntVariable foundTA;
    static ArrayList<TransportAction> TAData;
    private static BooVariable actionEdited;
    ArrayList<String> errors = new ArrayList<>();
    private static IntVariable foundProfile;
    static ArrayList<User> profileData;
    private static BooVariable badge2Edited;
    private static BooVariable badge0Edited;
    public TransportActionFragment() {
        // Required empty public constructor
    }

    public static TransportActionFragment newInstance(String param1, String param2) {
        TransportActionFragment fragment = new TransportActionFragment();
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
        View view = inflater.inflate(R.layout.fragment_transport_action, container, false);
        getUsersSustainableActions();
        TransportActionController tac = new TransportActionController();
        tac.getTAForTAFragment(userID, "TransportActionFragment");
        UserController uc = new UserController();
        uc.getProfile(userID, "TAFragment");
        badge0Edited = new BooVariable();
        badge0Edited.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                //toast
                Toast.makeText(view.getContext(), "Valio! Išsaugojote savo pirmąją užduotį, todėl gaunate ženklelį!", Toast.LENGTH_SHORT).show();
            }});

        badge2Edited = new BooVariable();
        badge2Edited.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                //toast
                Toast.makeText(view.getContext(), "Valio! Surinkote maksimalų taškų skaičių transporto srityje pirmą kartą, todėl gaunate ženklelį!", Toast.LENGTH_LONG).show();
            }});
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
        foundTA = new IntVariable();
        foundTA.setListener(new IntVariable.ChangeListener() {
            @Override
            public void onChange() {
                Log.i("mano", "cia");
                if (TAData != null) {
                    if (TAData.get(0).isNoTravelling()) {
                        noTravelling_cb.setChecked(true);
                        setInvisibleTravellingFields(view);
                    } else {
                        noTravelling_cb.setChecked(false);
                        setVisibleEveryField(view);
                        if (TAData.get(0).isWalking()) {
                            walking_cb.setChecked(true);
                            setVisibleWalkingFields(view);
                            walking_etn.setText(TAData.get(0).getWalkingKM());
                        }
                        else {
                            walking_cb.setChecked(false);
                            setInvisibleWalkingFields(view);
                        }
                        if (TAData.get(0).isBicycle()) {
                            bicycling_cb.setChecked(true);
                            setVisibleBicyclingFields(view);
                            bicycling_etn.setText(TAData.get(0).getBicycleKM());
                        }
                        else {
                            bicycling_cb.setChecked(false);
                            setInvisibleBicyclingFields(view);
                        }
                        if (TAData.get(0).isPublicTransport()) {
                            publicTransport_cb.setChecked(true);
                            setVisiblePublicTransportFields(view);
                            publicTransport_etn.setText(TAData.get(0).getPublicTransportKM());
                        }
                        else {
                            publicTransport_cb.setChecked(false);
                            setInvisiblePublicTransportFields(view);
                        }
                        if (TAData.get(0).isCar()) {
                            drivingCar_cb.setChecked(true);
                            setVisibleDrivingCarFields(view);
                            drivingCar_etn.setText(TAData.get(0).getCarKM());
                            givingALift_etn.setText(TAData.get(0).getCarPassengersKM());
                            people_etn.setText(TAData.get(0).getCarPassengers());
                        }
                        else {
                            drivingCar_cb.setChecked(false);
                            setInvisibleDrivingCarFields(view);
                        }
                    }
                }
                else {
                }
            }});
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
        noTravelling_cb =(CheckBox) view.findViewById(R.id.noTravelling_cb);
        noTravelling_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noTravelling_cb.isChecked()) {
                    setInvisibleTravellingFields(view);
                } else {
                    setVisibleEveryField(view);
                    if (walking_cb.isChecked()) {
                        setVisibleWalkingFields(view);
                    }
                    else {
                        setInvisibleWalkingFields(view);
                    }
                    if (bicycling_cb.isChecked()) {
                        setVisibleBicyclingFields(view);
                    }
                    else {
                        setInvisibleBicyclingFields(view);
                    }
                    if (publicTransport_cb.isChecked()) {
                        setVisiblePublicTransportFields(view);
                    }
                    else {
                        setInvisiblePublicTransportFields(view);
                    }
                    if (drivingCar_cb.isChecked()) {
                        setVisibleDrivingCarFields(view);
                    }
                    else {
                        setInvisibleDrivingCarFields(view);
                    }
                }
            }
        });
        walking_cb =(CheckBox) view.findViewById(R.id.walking_cb);
        walking_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walking_cb.isChecked()) {
                    setVisibleWalkingFields(view);
                } else {
                    setInvisibleWalkingFields(view);

                }
            }
        });
        bicycling_cb =(CheckBox) view.findViewById(R.id.bicycling_cb);
        bicycling_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bicycling_cb.isChecked()) {
                    setVisibleBicyclingFields(view);
                } else {
                    setInvisibleBicyclingFields(view);

                }
            }
        });
        publicTransport_cb =(CheckBox) view.findViewById(R.id.publicTransport_cb);
        publicTransport_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (publicTransport_cb.isChecked()) {
                    setVisiblePublicTransportFields(view);
                } else {
                    setInvisiblePublicTransportFields(view);

                }
            }
        });
        drivingCar_cb =(CheckBox) view.findViewById(R.id.drivingCar_cb);
        drivingCar_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drivingCar_cb.isChecked()) {
                    setVisibleDrivingCarFields(view);
                } else {
                    setInvisibleDrivingCarFields(view);

                }
            }
        });
        Button saveTA_b = (Button) view.findViewById(R.id.saveTA_b);
        saveTA_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sa != null) {
                    String walkingKm = "0";
                    String bicyclingKm = "0";
                    String publicTransportKm = "0";
                    String drivingCarKm = "0";
                    String givingALiftKm = "0";
                    String passengersInCar = "0";
                    if (!noTravelling_cb.isChecked()) {
                        if (walking_cb.isChecked()) {

                            walkingKm = walking_etn.getText().toString();
                            if (walkingKm.equals("")) {
                                walkingKm = "0";
                            }
                        }
                        if (bicycling_cb.isChecked()) {
                            bicyclingKm = bicycling_etn.getText().toString();
                            if (bicyclingKm.equals("")) {
                                bicyclingKm = "0";
                            }
                        }
                        if (publicTransport_cb.isChecked()) {
                            publicTransportKm = publicTransport_etn.getText().toString();
                            if (publicTransportKm.equals("")) {
                                publicTransportKm = "0";
                            }
                        }
                        if (drivingCar_cb.isChecked()) {
                            drivingCarKm = drivingCar_etn.getText().toString();
                            givingALiftKm = givingALift_etn.getText().toString();
                            passengersInCar = people_etn.getText().toString();
                            if (drivingCarKm.equals("")) {
                                drivingCarKm = "0";
                            }
                            if (givingALiftKm.equals("")) {
                                givingALiftKm = "0";
                            }
                            if (passengersInCar.equals("")) {
                                passengersInCar = "0";
                            }
                        }
                    }

                    //////////////////////////////
                    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date(System.currentTimeMillis());
                    String dateStr = formatter.format(date);
                    //EnergyAction ea = new EnergyAction(sa.getId(), sa.getCategory(), sa.getUserID(), sa.getDateBegin(), sa.getDateEnd(), dateStr, shower_cb.isChecked(), showerTime, bath_cb.isChecked(), devices);
                    TransportAction ta = new TransportAction(sa.getId(), sa.getCategory(), sa.getUserID(), sa.getDateBegin(), sa.getDateEnd(), dateStr, noTravelling_cb.isChecked(), walking_cb.isChecked(), walkingKm, bicycling_cb.isChecked(), bicyclingKm, publicTransport_cb.isChecked(), publicTransportKm, drivingCar_cb.isChecked(), drivingCarKm, givingALiftKm, passengersInCar);

                    TransportActionController tac = new TransportActionController();
                    if (noTravelling_cb.isChecked()) {
                        errors = new ArrayList<String>();
                    }
                    else {
                        errors = (ArrayList<String>) tac.validateTA(ta);
                    }
                    int howManyErr = 0;
                    for (int i = 0; i < errors.size(); i++) {
                        if (!errors.get(i).equals("")) {
                            howManyErr = howManyErr + 1;
                        }
                    }
                    if (!noTravelling_cb.isChecked()) {
                        showErrors();
                    }
                    if (howManyErr == 0) {
                        //eac.addEnergyActionToDB(ea);
                        //UPDATE
                        //Log.i("mano", "UPDATE, ERRORS: " + errors.get(0) +errors.get(1) +errors.get(2));
                        tac.updateTransportActionInDB(ta);
                        tac.checkForBadge2(ta, profileData.get(0));

                    }
                }}
        });
        return view;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        noTravelling_cb = getView().findViewById(R.id.noTravelling_cb);
        walking_cb = getView().findViewById(R.id.walking_cb);
        bicycling_cb = getView().findViewById(R.id.bicycling_cb);
        publicTransport_cb = getView().findViewById(R.id.publicTransport_cb);
        drivingCar_cb = getView().findViewById(R.id.drivingCar_cb);

        walking_etn = getView().findViewById(R.id.walking_etn);
        bicycling_etn = getView().findViewById(R.id.bicycling_etn);
        publicTransport_etn = getView().findViewById(R.id.publicTransport_etn);
        drivingCar_etn = getView().findViewById(R.id.drivingCar_etn);
        givingALift_etn = getView().findViewById(R.id.givingALift_etn);
        people_etn = getView().findViewById(R.id.people_etn);

        walkingKm_tv = getView().findViewById(R.id.walkingKm_tv);
        bicyclingKm_tv = getView().findViewById(R.id.bicyclingKm_tv);
        publicTransportKm_tv = getView().findViewById(R.id.publicTransportKm_tv);
        drivingCarKm_tv = getView().findViewById(R.id.drivingCarKm_tv);
        drivingCar_tv = getView().findViewById(R.id.drivingCar_tv);
        drivingCar_tv2 = getView().findViewById(R.id.drivingCar_tv2);
        drivingCarKm_tv2 = getView().findViewById(R.id.drivingCarKm_tv2);
        drivingCarPeople_tv = getView().findViewById(R.id.drivingCarPeople_tv);
        setInvisibleETandTVFields(view);
        setInvisibleTravellingFields(view);
    }
    public void setVisibleEveryField(View view) {
        noTravelling_cb.setVisibility(View.VISIBLE);
        walking_cb.setVisibility(View.VISIBLE);
        bicycling_cb.setVisibility(View.VISIBLE);
        publicTransport_cb.setVisibility(View.VISIBLE);
        drivingCar_cb.setVisibility(View.VISIBLE);
        walking_etn.setVisibility(View.VISIBLE);
        bicycling_etn.setVisibility(View.VISIBLE);
        publicTransport_etn.setVisibility(View.VISIBLE);
        drivingCar_etn.setVisibility(View.VISIBLE);
        givingALift_etn.setVisibility(View.VISIBLE);
        people_etn.setVisibility(View.VISIBLE);
        walkingKm_tv.setVisibility(View.VISIBLE);
        bicyclingKm_tv.setVisibility(View.VISIBLE);
        publicTransportKm_tv.setVisibility(View.VISIBLE);
        drivingCarKm_tv.setVisibility(View.VISIBLE);
        drivingCar_tv.setVisibility(View.VISIBLE);
        drivingCar_tv2.setVisibility(View.VISIBLE);
        drivingCarKm_tv2.setVisibility(View.VISIBLE);
        drivingCarPeople_tv.setVisibility(View.VISIBLE);
    }
    public void setInvisibleTravellingFields(View view) {
        noTravelling_cb.setVisibility(View.VISIBLE);
        walking_cb.setVisibility(View.INVISIBLE);
        bicycling_cb.setVisibility(View.INVISIBLE);
        publicTransport_cb.setVisibility(View.INVISIBLE);
        drivingCar_cb.setVisibility(View.INVISIBLE);
        walking_etn.setVisibility(View.INVISIBLE);
        bicycling_etn.setVisibility(View.INVISIBLE);
        publicTransport_etn.setVisibility(View.INVISIBLE);
        drivingCar_etn.setVisibility(View.INVISIBLE);
        givingALift_etn.setVisibility(View.INVISIBLE);
        people_etn.setVisibility(View.INVISIBLE);
        walkingKm_tv.setVisibility(View.INVISIBLE);
        bicyclingKm_tv.setVisibility(View.INVISIBLE);
        publicTransportKm_tv.setVisibility(View.INVISIBLE);
        drivingCarKm_tv.setVisibility(View.INVISIBLE);
        drivingCar_tv.setVisibility(View.INVISIBLE);
        drivingCar_tv2.setVisibility(View.INVISIBLE);
        drivingCarKm_tv2.setVisibility(View.INVISIBLE);
        drivingCarPeople_tv.setVisibility(View.INVISIBLE);
    }
    public void setVisibleWalkingFields(View view) {
        walking_etn.setVisibility(View.VISIBLE);
        walkingKm_tv.setVisibility(View.VISIBLE);
    }
    public void setInvisibleWalkingFields(View view) {
        walking_etn.setVisibility(View.INVISIBLE);
        walkingKm_tv.setVisibility(View.INVISIBLE);
    }
    public void setVisibleBicyclingFields(View view) {
        bicycling_etn.setVisibility(View.VISIBLE);
        bicyclingKm_tv.setVisibility(View.VISIBLE);
    }
    public void setInvisibleBicyclingFields(View view) {
        bicycling_etn.setVisibility(View.INVISIBLE);
        bicyclingKm_tv.setVisibility(View.INVISIBLE);
    }
    public void setVisiblePublicTransportFields(View view) {
        publicTransport_etn.setVisibility(View.VISIBLE);
        publicTransportKm_tv.setVisibility(View.VISIBLE);
    }
    public void setInvisiblePublicTransportFields(View view) {
        publicTransport_etn.setVisibility(View.INVISIBLE);
        publicTransportKm_tv.setVisibility(View.INVISIBLE);
    }
    public void setVisibleDrivingCarFields(View view) {
        drivingCar_etn.setVisibility(View.VISIBLE);
        givingALift_etn.setVisibility(View.VISIBLE);
        people_etn.setVisibility(View.VISIBLE);
        drivingCarKm_tv.setVisibility(View.VISIBLE);
        drivingCar_tv.setVisibility(View.VISIBLE);
        drivingCar_tv2.setVisibility(View.VISIBLE);
        drivingCarKm_tv2.setVisibility(View.VISIBLE);
        drivingCarPeople_tv.setVisibility(View.VISIBLE);
    }
    public void setInvisibleDrivingCarFields(View view) {
        drivingCar_etn.setVisibility(View.INVISIBLE);
        givingALift_etn.setVisibility(View.INVISIBLE);
        people_etn.setVisibility(View.INVISIBLE);
        drivingCarKm_tv.setVisibility(View.INVISIBLE);
        drivingCar_tv.setVisibility(View.INVISIBLE);
        drivingCar_tv2.setVisibility(View.INVISIBLE);
        drivingCarKm_tv2.setVisibility(View.INVISIBLE);
        drivingCarPeople_tv.setVisibility(View.INVISIBLE);
    }
    public void setInvisibleETandTVFields(View view) {
        walking_etn.setVisibility(View.INVISIBLE);
        bicycling_etn.setVisibility(View.INVISIBLE);
        publicTransport_etn.setVisibility(View.INVISIBLE);
        drivingCar_etn.setVisibility(View.INVISIBLE);
        givingALift_etn.setVisibility(View.INVISIBLE);
        people_etn.setVisibility(View.INVISIBLE);
        walkingKm_tv.setVisibility(View.INVISIBLE);
        bicyclingKm_tv.setVisibility(View.INVISIBLE);
        publicTransportKm_tv.setVisibility(View.INVISIBLE);
        drivingCarKm_tv.setVisibility(View.INVISIBLE);
        drivingCar_tv.setVisibility(View.INVISIBLE);
        drivingCar_tv2.setVisibility(View.INVISIBLE);
        drivingCarKm_tv2.setVisibility(View.INVISIBLE);
        drivingCarPeople_tv.setVisibility(View.INVISIBLE);
    }
    public void getUsersSustainableActions() {
        SustainableActionController sac = new SustainableActionController();
        String purpose = "TransportActionFragment";
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
    public static void checkTANotFound(List<TransportAction> list) {
        Log.i("mano", "neradom TA..........................");
    }
    public static void checkTAFound(List<TransportAction> list) {
        Log.i("mano", "radom TA............" + list.size() + "...."  +  list.get(0).toString());
        TAData = (ArrayList<TransportAction>) list;
        if (!list.isEmpty()) {
            foundTA.setID(list.get(0).getId());
            if (foundTA.getListener() != null) {
                foundTA.getListener().onChange();
                list = null;
            }
        }
        else {
            checkTANotFound(list);
        }
    }
    public void showErrors() {
        if (!errors.get(0).equals("")) {
            walking_etn.setError(errors.get(0));
        }
        if (!errors.get(1).equals("")) {
            bicycling_etn.setError(errors.get(1));
        }
        if (!errors.get(2).equals("")) {
            publicTransport_etn.setError(errors.get(2));
        }
        if (!errors.get(3).equals("")) {
            drivingCar_etn.setError(errors.get(3));
        }
        if (!errors.get(4).equals("")) {
            givingALift_etn.setError(errors.get(4));
        }
        if (!errors.get(5).equals("")) {
            people_etn.setError(errors.get(5));
        }
    }
    public static void checkTAEdited() {
        actionEdited.setBoo(true);
        if (actionEdited.getListener() != null) {
            actionEdited.getListener().onChange();
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
    public static void checkBadge2Edited() {
        badge2Edited.setBoo(true);
        if (badge2Edited.getListener() != null) {
            badge2Edited.getListener().onChange();
        }
    }
    public static void checkBadge0Edited() {
        badge0Edited.setBoo(true);
        if (badge0Edited.getListener() != null) {
            badge0Edited.getListener().onChange();
        }
    }
}