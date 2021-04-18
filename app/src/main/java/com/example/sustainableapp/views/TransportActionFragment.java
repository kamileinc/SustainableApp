package com.example.sustainableapp.views;


import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sustainableapp.R;
import com.example.sustainableapp.controllers.SustainableActionController;
import com.example.sustainableapp.controllers.TransportActionController;
import com.example.sustainableapp.controllers.UserController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.IntVariable;
import com.example.sustainableapp.models.SustainableAction;
import com.example.sustainableapp.models.TransportAction;
import com.example.sustainableapp.models.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    static ArrayList<User> profileData;
    private static BooVariable badge2Edited;
    private static BooVariable badge0Edited;
    public TransportActionFragment() {
    }
    public static TransportActionFragment newInstance() {
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
        }
    }

    @SuppressLint("SetTextI18n")
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
        badge0Edited.setListener(() -> UserActivity.sendNotification(view.getContext(), "3", "Ženklelis", "Valio! Išsaugojote savo pirmąją užduotį, todėl gaunate ženklelį!", false));

        badge2Edited = new BooVariable();
        badge2Edited.setListener(() -> UserActivity.sendNotification(view.getContext(), "3", "Ženklelis", "Valio! Surinkote maksimalų taškų skaičių transporto srityje pirmą kartą, todėl gaunate ženklelį!", false));

        actionEdited = new BooVariable();
        actionEdited.setListener(() -> {
            Toast.makeText(view.getContext(),"Sėkmingai išsaugoti pakeitimai",Toast. LENGTH_SHORT).show();
            Objects.requireNonNull(getActivity()).findViewById(R.id.ic_tasks).performClick();
        });
        foundTA = new IntVariable();
        foundTA.setListener(() -> {
            if (TAData != null) {
                if (TAData.get(0).isNoTravelling()) {
                    noTravelling_cb.setChecked(true);
                    setInvisibleTravellingFields();
                } else {
                    noTravelling_cb.setChecked(false);
                    setVisibleEveryField();
                    if (TAData.get(0).isWalking()) {
                        walking_cb.setChecked(true);
                        setVisibleWalkingFields();
                        walking_etn.setText(Integer.toString(TAData.get(0).getWalkingKM()));
                    }
                    else {
                        walking_cb.setChecked(false);
                        setInvisibleWalkingFields();
                    }
                    if (TAData.get(0).isBicycle()) {
                        bicycling_cb.setChecked(true);
                        setVisibleBicyclingFields();
                        bicycling_etn.setText(Integer.toString(TAData.get(0).getBicycleKM()));
                    }
                    else {
                        bicycling_cb.setChecked(false);
                        setInvisibleBicyclingFields();
                    }
                    if (TAData.get(0).isPublicTransport()) {
                        publicTransport_cb.setChecked(true);
                        setVisiblePublicTransportFields();
                        publicTransport_etn.setText(Integer.toString(TAData.get(0).getPublicTransportKM()));
                    }
                    else {
                        publicTransport_cb.setChecked(false);
                        setInvisiblePublicTransportFields();
                    }
                    if (TAData.get(0).isCar()) {
                        drivingCar_cb.setChecked(true);
                        setVisibleDrivingCarFields();
                        drivingCar_etn.setText(Integer.toString(TAData.get(0).getCarKM()));
                        givingALift_etn.setText(Integer.toString(TAData.get(0).getCarPassengersKM()));
                        people_etn.setText(Integer.toString(TAData.get(0).getCarPassengers()));
                    }
                    else {
                        drivingCar_cb.setChecked(false);
                        setInvisibleDrivingCarFields();
                    }
                }
            }
        });
        saListReturned = new BooVariable();
        saListReturned.setListener(() -> {
            if (saListReturned.isBoo()) {
                String beginDate = saList.get((saList.size()-1)).getDateBegin();
                String endDate = saList.get((saList.size()-1)).getDateEnd();
                SustainableActionController sac = new SustainableActionController();
                if (sac.isTodayInDates(beginDate, endDate)) {
                    sa = saList.get((saList.size()-1));
                }
            }

        });
        noTravelling_cb = view.findViewById(R.id.noTravelling_cb);
        noTravelling_cb.setOnClickListener(v -> {
            if (noTravelling_cb.isChecked()) {
                setInvisibleTravellingFields();
            } else {
                setVisibleEveryField();
                if (walking_cb.isChecked()) {
                    setVisibleWalkingFields();
                }
                else {
                    setInvisibleWalkingFields();
                }
                if (bicycling_cb.isChecked()) {
                    setVisibleBicyclingFields();
                }
                else {
                    setInvisibleBicyclingFields();
                }
                if (publicTransport_cb.isChecked()) {
                    setVisiblePublicTransportFields();
                }
                else {
                    setInvisiblePublicTransportFields();
                }
                if (drivingCar_cb.isChecked()) {
                    setVisibleDrivingCarFields();
                }
                else {
                    setInvisibleDrivingCarFields();
                }
            }
        });
        walking_cb = view.findViewById(R.id.walking_cb);
        walking_cb.setOnClickListener(v -> {
            if (walking_cb.isChecked()) {
                setVisibleWalkingFields();
            } else {
                setInvisibleWalkingFields();

            }
        });
        bicycling_cb = view.findViewById(R.id.bicycling_cb);
        bicycling_cb.setOnClickListener(v -> {
            if (bicycling_cb.isChecked()) {
                setVisibleBicyclingFields();
            } else {
                setInvisibleBicyclingFields();

            }
        });
        publicTransport_cb = view.findViewById(R.id.publicTransport_cb);
        publicTransport_cb.setOnClickListener(v -> {
            if (publicTransport_cb.isChecked()) {
                setVisiblePublicTransportFields();
            } else {
                setInvisiblePublicTransportFields();

            }
        });
        drivingCar_cb = view.findViewById(R.id.drivingCar_cb);
        drivingCar_cb.setOnClickListener(v -> {
            if (drivingCar_cb.isChecked()) {
                setVisibleDrivingCarFields();
            } else {
                setInvisibleDrivingCarFields();

            }
        });
        Button saveTA_b = view.findViewById(R.id.saveTA_b);
        saveTA_b.setOnClickListener(v -> {
            if (sa != null) {
                int walkingKm = 0;
                int bicyclingKm = 0;
                int publicTransportKm = 0;
                int drivingCarKm = 0;
                int givingALiftKm = 0;
                int passengersInCar = 0;
                if (!noTravelling_cb.isChecked()) {
                    if (walking_cb.isChecked()) {
                        try {
                            walkingKm = Integer.parseInt(walking_etn.getText().toString());
                        }
                        catch(Exception ignored) {
                        }
                    }
                    if (bicycling_cb.isChecked()) {
                        try {
                        bicyclingKm = Integer.parseInt(bicycling_etn.getText().toString());
                        }
                        catch(Exception ignored) {
                        }
                    }
                    if (publicTransport_cb.isChecked()) {
                        try {
                        publicTransportKm = Integer.parseInt(publicTransport_etn.getText().toString());
                    }
                        catch(Exception ignored) {
                        }
                    }
                    if (drivingCar_cb.isChecked()) {
                        try {
                            drivingCarKm = Integer.parseInt(drivingCar_etn.getText().toString());
                        }
                        catch(Exception ignored) {
                        }
                        try {
                            givingALiftKm = Integer.parseInt(givingALift_etn.getText().toString());
                        }
                        catch(Exception e) {
                            givingALiftKm = 0;
                        }
                        try {
                            passengersInCar = Integer.parseInt(people_etn.getText().toString());
                        }
                        catch(Exception e) {
                            passengersInCar = 0;
                }
                    }
                }
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(System.currentTimeMillis());
                String dateStr = formatter.format(date);
                TransportAction ta = new TransportAction(sa.getId(), sa.getCategory(), sa.getUserID(), sa.getDateBegin(), sa.getDateEnd(), dateStr, noTravelling_cb.isChecked(), walking_cb.isChecked(), walkingKm, bicycling_cb.isChecked(), bicyclingKm, publicTransport_cb.isChecked(), publicTransportKm, drivingCar_cb.isChecked(), drivingCarKm, givingALiftKm, passengersInCar);
                TransportActionController tac1 = new TransportActionController();
                if (noTravelling_cb.isChecked()) {
                    errors = new ArrayList<>();
                }
                else {
                    errors = (ArrayList<String>) tac1.validateTA(ta);
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
                    tac1.updateTransportActionInDB(ta);
                    tac1.checkForBadge2(ta, profileData.get(0));
                }
            }});
        return view;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        noTravelling_cb = Objects.requireNonNull(getView()).findViewById(R.id.noTravelling_cb);
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
        setInvisibleETandTVFields();
        setInvisibleTravellingFields();
    }
    public void setVisibleEveryField() {
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
    public void setInvisibleTravellingFields() {
        noTravelling_cb.setVisibility(View.VISIBLE);
        walking_cb.setVisibility(View.GONE);
        bicycling_cb.setVisibility(View.GONE);
        publicTransport_cb.setVisibility(View.GONE);
        drivingCar_cb.setVisibility(View.GONE);
        walking_etn.setVisibility(View.GONE);
        bicycling_etn.setVisibility(View.GONE);
        publicTransport_etn.setVisibility(View.GONE);
        drivingCar_etn.setVisibility(View.GONE);
        givingALift_etn.setVisibility(View.GONE);
        people_etn.setVisibility(View.GONE);
        walkingKm_tv.setVisibility(View.GONE);
        bicyclingKm_tv.setVisibility(View.GONE);
        publicTransportKm_tv.setVisibility(View.GONE);
        drivingCarKm_tv.setVisibility(View.GONE);
        drivingCar_tv.setVisibility(View.GONE);
        drivingCar_tv2.setVisibility(View.GONE);
        drivingCarKm_tv2.setVisibility(View.GONE);
        drivingCarPeople_tv.setVisibility(View.GONE);
    }
    public void setVisibleWalkingFields() {
        walking_etn.setVisibility(View.VISIBLE);
        walkingKm_tv.setVisibility(View.VISIBLE);
    }
    public void setInvisibleWalkingFields() {
        walking_etn.setVisibility(View.GONE);
        walkingKm_tv.setVisibility(View.GONE);
    }
    public void setVisibleBicyclingFields() {
        bicycling_etn.setVisibility(View.VISIBLE);
        bicyclingKm_tv.setVisibility(View.VISIBLE);
    }
    public void setInvisibleBicyclingFields() {
        bicycling_etn.setVisibility(View.GONE);
        bicyclingKm_tv.setVisibility(View.GONE);
    }
    public void setVisiblePublicTransportFields() {
        publicTransport_etn.setVisibility(View.VISIBLE);
        publicTransportKm_tv.setVisibility(View.VISIBLE);
    }
    public void setInvisiblePublicTransportFields() {
        publicTransport_etn.setVisibility(View.GONE);
        publicTransportKm_tv.setVisibility(View.GONE);
    }
    public void setVisibleDrivingCarFields() {
        drivingCar_etn.setVisibility(View.VISIBLE);
        givingALift_etn.setVisibility(View.VISIBLE);
        people_etn.setVisibility(View.VISIBLE);
        drivingCarKm_tv.setVisibility(View.VISIBLE);
        drivingCar_tv.setVisibility(View.VISIBLE);
        drivingCar_tv2.setVisibility(View.VISIBLE);
        drivingCarKm_tv2.setVisibility(View.VISIBLE);
        drivingCarPeople_tv.setVisibility(View.VISIBLE);
    }
    public void setInvisibleDrivingCarFields() {
        drivingCar_etn.setVisibility(View.GONE);
        givingALift_etn.setVisibility(View.GONE);
        people_etn.setVisibility(View.GONE);
        drivingCarKm_tv.setVisibility(View.GONE);
        drivingCar_tv.setVisibility(View.GONE);
        drivingCar_tv2.setVisibility(View.GONE);
        drivingCarKm_tv2.setVisibility(View.GONE);
        drivingCarPeople_tv.setVisibility(View.GONE);
    }
    public void setInvisibleETandTVFields() {
        walking_etn.setVisibility(View.GONE);
        bicycling_etn.setVisibility(View.GONE);
        publicTransport_etn.setVisibility(View.GONE);
        drivingCar_etn.setVisibility(View.GONE);
        givingALift_etn.setVisibility(View.GONE);
        people_etn.setVisibility(View.GONE);
        walkingKm_tv.setVisibility(View.GONE);
        bicyclingKm_tv.setVisibility(View.GONE);
        publicTransportKm_tv.setVisibility(View.GONE);
        drivingCarKm_tv.setVisibility(View.GONE);
        drivingCar_tv.setVisibility(View.GONE);
        drivingCar_tv2.setVisibility(View.GONE);
        drivingCarKm_tv2.setVisibility(View.GONE);
        drivingCarPeople_tv.setVisibility(View.GONE);
    }
    public void getUsersSustainableActions() {
        SustainableActionController sac = new SustainableActionController();
        String purpose = "TransportActionFragment";
        sac.getUsersSustainableActions(userID, purpose);
    }
    public static void checkUsersSAFound(ArrayList<SustainableAction> sa) {
        saList = sa;
        saListReturned.setBoo(true);
        saListReturned.getListener().onChange();
    }
    public static void checkUsersSANotFound(ArrayList<SustainableAction> sa) {
        saList = sa;
        saListReturned.setBoo(false);
        saListReturned.getListener().onChange();
    }
    public static void checkTANotFound() {
    }
    public static void checkTAFound(List<TransportAction> list) {
        TAData = (ArrayList<TransportAction>) list;
        if (!list.isEmpty()) {
            foundTA.setID(list.get(0).getId());
            if (foundTA.getListener() != null) {
                foundTA.getListener().onChange();
            }
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
        profileData = (ArrayList<User>) list;
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