package com.example.sustainableapp.views;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.example.sustainableapp.R;
import com.example.sustainableapp.controllers.UserController;
import com.example.sustainableapp.models.IntVariable;
import com.example.sustainableapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    String userID;
    private static IntVariable foundProfile;
    static ArrayList<User> profileData;
    TextView firstName_et, lastName_et, username_et, photo_et, address_et, diet_et, dietChange_et,
            breakfast_et, lunch_et, dinner_et, wakingUpTime_et, sleepingTime_et,transport_et, workingDayTrips_et,
            workingDayTransport_et, weekendTrips_et, weekendDayTransport_et, takingShowerPerWeek_et, showerTime_et, takingBathPerWeek_et;
    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Button change_b = (Button) view.findViewById(R.id.change_b);
        change_b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openEditProfileFragment(view);
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
                    Log.i("mano", profileData.get(0).toString());
                    firstName_et.setText(profileData.get(0).getFirstName());
                    lastName_et.setText(profileData.get(0).getLastName());
                    username_et.setText(profileData.get(0).getUsername());
                    photo_et.setText(profileData.get(0).getPhoto());
                    address_et.setText(profileData.get(0).getAddress());
                    diet_et.setText(profileData.get(0).getDiet());
                    dietChange_et.setText(profileData.get(0).getDietChange());
                    UserController uc = new UserController();
                    breakfast_et.setText(uc.formatTime(profileData.get(0).getBreakfastTime()));


                    lunch_et.setText(uc.formatTime(profileData.get(0).getLunchTime()));
                    dinner_et.setText(uc.formatTime(profileData.get(0).getDinnerTime()));
                    wakingUpTime_et.setText(uc.formatTime(profileData.get(0).getWakingUpTime()));
                    sleepingTime_et.setText(uc.formatTime(profileData.get(0).getSleepingTime()));
                    if (profileData.get(0).getTransport().equals("0")) {
                        transport_et.setText("-");
                    }
                    else if (profileData.get(0).getTransport().equals("1")) {
                        transport_et.setText("Automobilis");
                    }
                    else if (profileData.get(0).getTransport().equals("2")) {
                        transport_et.setText("Dviratis");
                    }
                    else if (profileData.get(0).getTransport().equals("4")) {
                        transport_et.setText("Automobilis ir dviratis");
                    }
                    workingDayTrips_et.setText(profileData.get(0).getWorkingDayTrips());
                    workingDayTransport_et.setText(profileData.get(0).getWorkingDayTransport());
                    weekendTrips_et.setText(profileData.get(0).getWeekendDayTrips());
                    weekendDayTransport_et.setText(profileData.get(0).getWeekendDayTransport());
                    takingShowerPerWeek_et.setText(profileData.get(0).getTakingShowerPerWeek());
                    showerTime_et.setText(profileData.get(0).getShowerTime());
                    takingBathPerWeek_et.setText(profileData.get(0).getTakingBathPerWeek());
                    Log.i("mano", "turejo priskirt");
                }
                else {
                }
            }});
        UserController uc = new UserController();
        uc.getProfile(userID);
        Log.i("mano", "getprofiledata fragment");
        return view;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        firstName_et = getView().findViewById(R.id.firstName_et);
        lastName_et = getView().findViewById(R.id.lastName_et);
        username_et = getView().findViewById(R.id.username_et);
        photo_et = getView().findViewById(R.id.photo_et);
        address_et = getView().findViewById(R.id.address_et);
        diet_et = getView().findViewById(R.id.diet_et);
        dietChange_et = getView().findViewById(R.id.dietChange_et);
        breakfast_et = getView().findViewById(R.id.breakfast_et);
        lunch_et = getView().findViewById(R.id.lunch_et);
        dinner_et = getView().findViewById(R.id.dinner_et);
        wakingUpTime_et = getView().findViewById(R.id.wakingUpTime_et);
        sleepingTime_et = getView().findViewById(R.id.sleepingTime_et);
        transport_et = getView().findViewById(R.id.transport_et);
        workingDayTrips_et = getView().findViewById(R.id.workingDayTrips_etn);
        workingDayTransport_et = getView().findViewById(R.id.workingDayTransport_et);
        weekendTrips_et = getView().findViewById(R.id.weekendTrips_etn);
        weekendDayTransport_et = getView().findViewById(R.id.weekendDayTransport_et);
        takingShowerPerWeek_et = getView().findViewById(R.id.takingShowerPerWeek_etn);
        showerTime_et = getView().findViewById(R.id.showerTime_etn);
        takingBathPerWeek_et = getView().findViewById(R.id.takingBathPerWeek_etn);


    }
    public void openEditProfileFragment(View view) {

        androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
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