package com.example.sustainableapp.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.sustainableapp.R;
import com.example.sustainableapp.controllers.UserController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.IntVariable;
import com.example.sustainableapp.models.User;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment {
    private static IntVariable foundProfile;
    private static BooVariable profileEdited;
    static ArrayList<User> profileData;
    private static Bitmap bitmap;
    private static Bitmap bitmapToUpload;
    ArrayList<String> errors;
    String userID;
    EditText firstName_et,lastName_et, username_et, photo_et, address_et, workingDayTrips_etn, weekendTrips_etn, takingShowerPerWeek_etn, showerTime_etn, takingBathPerWeek_etn, password1_et, password2_et;
    CheckBox car_cb, bicycle_cb;
    Spinner dietS, dietChangeS, workingDayTransportS, weekendDayTransportS;
    String breakfastTime, lunchTime, dinnerTime, wakingUpTime, sleepingTime;
    TimePicker breakfast_dp, lunch_dp, dinner_dp, wakingUp_dp, sleeping_dp;
    ImageView photo_iv;
    TextView lunchTime_tv, dinnerTime_tv, sleepingTime_tv;
    private static BooVariable photoReturned;
    int hour, minutes;
    public EditProfileFragment() {
        // Required empty public constructor
    }

    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
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
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        Button update_b = (Button) view.findViewById(R.id.update_b);
        update_b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //validate and update data in db
                uploadPhotoToFirebase(bitmapToUpload);
                String firstName = firstName_et.getText().toString();
                String lastName = lastName_et.getText().toString();
                String photo = userID + ".jpg";
                String address = address_et.getText().toString();

                hour = breakfast_dp.getHour();
                minutes = breakfast_dp.getMinute();
                breakfastTime = hour + ":" + minutes;

                hour = lunch_dp.getHour();
                minutes = lunch_dp.getMinute();
                lunchTime = hour + ":" + minutes;

                hour = dinner_dp.getHour();
                minutes = dinner_dp.getMinute();
                dinnerTime = hour + ":" + minutes;

                hour = wakingUp_dp.getHour();
                minutes = wakingUp_dp.getMinute();
                wakingUpTime = hour + ":" + minutes;

                hour = sleeping_dp.getHour();
                minutes = sleeping_dp.getMinute();
                sleepingTime = hour + ":" + minutes;
                String transport = "0";
                boolean car = car_cb.isChecked();
                boolean bicycle = bicycle_cb.isChecked();
                if (car == true) {
                    transport = "1";
                }
                if (bicycle == true) {
                    transport = "2";
                }
                if (car == true && bicycle == true) {
                    transport = "4";
                }
                Log.i("mano", "transport: " + transport);
                Log.i("mano", "car: " + car);
                Log.i("mano", "bicycle: " + bicycle);
                String workingDayTrips = workingDayTrips_etn.getText().toString();
                String weekendDayTrips = weekendTrips_etn.getText().toString();
                String takingShowerPerWeek = takingShowerPerWeek_etn.getText().toString();
                String showerTime = showerTime_etn.getText().toString();
                String takingBathPerWeek = takingBathPerWeek_etn.getText().toString();

                //String password1 = password1_et.getText().toString();
                //String password2 = password2_et.getText().toString();

                String diet = dietS.getSelectedItem().toString();
                String dietChange = dietChangeS.getSelectedItem().toString();
                String workingDayTransport = workingDayTransportS.getSelectedItem().toString();
                String weekendDayTransport = weekendDayTransportS.getSelectedItem().toString();
                User userToCheck = new User(userID, firstName, lastName, "", photo, address,
                        diet, dietChange, breakfastTime, lunchTime, dinnerTime,
                        wakingUpTime, sleepingTime, transport, workingDayTrips, workingDayTransport,
                        weekendDayTrips, weekendDayTransport, takingShowerPerWeek, showerTime, takingBathPerWeek,
                        "", new ArrayList<Boolean>());
                UserController uc = new UserController();

                Log.i("mano", "user to check: " + userToCheck.toString());
                String purpose = "edit";
                errors = (ArrayList<String>) uc.validateUser(userToCheck, "", purpose);
                int howManyErr = 0;
                for (int i =0; i<errors.size(); i++){
                    if (!errors.get(i).equals("")) {
                        howManyErr = howManyErr + 1;
                    }
                }
                if (howManyErr>0) {
                    showErrors();
                }
                else {
                    Log.i("mano", "user to check: " + userToCheck.toString());
                    uc.editUser(userToCheck);
                }
            }
        });
        ImageButton image_b = (ImageButton) view.findViewById(R.id.image_b);
        image_b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Pasirinkite nuotrauką"), 1);

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
                    String purpose = "editProfile";
                    uc.loadImageForView(profileData.get(0).getId(), profileData.get(0).getId() + ".jpg", purpose);
                    Log.i("mano", profileData.get(0).toString());
                    firstName_et.setText(profileData.get(0).getFirstName());
                    lastName_et.setText(profileData.get(0).getLastName());
                    //photo_et.setText(profileData.get(0).getPhoto());
                    //photo_et.setText(userID + ".jpg");
                    address_et.setText(profileData.get(0).getAddress());
                    if (profileData.get(0).getDiet().equals("Visavalgis")) {
                        dietS.setSelection(0);
                    }
                    else if (profileData.get(0).getDiet().equals("Vegetaras")) {
                        dietS.setSelection(1);
                    }
                    else if (profileData.get(0).getDiet().equals("Veganas")) {
                        dietS.setSelection(2);
                    }
                    if (profileData.get(0).getDietChange().equals("Visavalgis")) {
                        dietChangeS.setSelection(0);
                    }
                    else if (profileData.get(0).getDietChange().equals("Vegetaras")) {
                        dietChangeS.setSelection(1);
                    }
                    else if (profileData.get(0).getDietChange().equals("Veganas")) {
                        dietChangeS.setSelection(2);
                    }

                    breakfast_dp.setHour(uc.getHourAndMinutes(profileData.get(0).getBreakfastTime()).get(0));
                    breakfast_dp.setMinute(uc.getHourAndMinutes(profileData.get(0).getBreakfastTime()).get(1));

                    lunch_dp.setHour(uc.getHourAndMinutes(profileData.get(0).getLunchTime()).get(0));
                    lunch_dp.setMinute(uc.getHourAndMinutes(profileData.get(0).getLunchTime()).get(1));

                    dinner_dp.setHour(uc.getHourAndMinutes(profileData.get(0).getDinnerTime()).get(0));
                    dinner_dp.setMinute(uc.getHourAndMinutes(profileData.get(0).getDinnerTime()).get(1));
                    wakingUp_dp.setHour(uc.getHourAndMinutes(profileData.get(0).getWakingUpTime()).get(0));
                    wakingUp_dp.setMinute(uc.getHourAndMinutes(profileData.get(0).getWakingUpTime()).get(1));
                    sleeping_dp.setHour(uc.getHourAndMinutes(profileData.get(0).getSleepingTime()).get(0));
                    sleeping_dp.setMinute(uc.getHourAndMinutes(profileData.get(0).getSleepingTime()).get(1));
                    //lunch_et.setText(uc.formatTime(profileData.get(0).getLunchTime()));
                   // dinner_et.setText(uc.formatTime(profileData.get(0).getDinnerTime()));
                   // wakingUpTime_et.setText(uc.formatTime(profileData.get(0).getWakingUpTime()));
                    //sleepingTime_et.setText(uc.formatTime(profileData.get(0).getSleepingTime()));
                    if (profileData.get(0).getTransport().equals("1")) {
                        car_cb.setChecked(true);
                    }
                    else if (profileData.get(0).getTransport().equals("2")) {
                        bicycle_cb.setChecked(true);
                    }
                    else if (profileData.get(0).getTransport().equals("4")) {
                        car_cb.setChecked(true);
                        bicycle_cb.setChecked(true);
                    }
                    workingDayTrips_etn.setText(profileData.get(0).getWorkingDayTrips());
                    if (profileData.get(0).getWorkingDayTransport().equals("Automobilis")) {
                        workingDayTransportS.setSelection(0);
                    }
                    else if (profileData.get(0).getWorkingDayTransport().equals("Viešasis transportas")) {
                        workingDayTransportS.setSelection(1);
                    }
                    else if (profileData.get(0).getWorkingDayTransport().equals("Dviratis")) {
                        workingDayTransportS.setSelection(2);
                    }
                    else if (profileData.get(0).getWorkingDayTransport().equals("Pėsčiomis")) {
                        workingDayTransportS.setSelection(3);
                    }
                    weekendTrips_etn.setText(profileData.get(0).getWeekendDayTrips());
                    if (profileData.get(0).getWeekendDayTransport().equals("Automobilis")) {
                        weekendDayTransportS.setSelection(0);
                    }
                    else if (profileData.get(0).getWeekendDayTransport().equals("Viešasis transportas")) {
                        weekendDayTransportS.setSelection(1);
                    }
                    else if (profileData.get(0).getWeekendDayTransport().equals("Dviratis")) {
                        weekendDayTransportS.setSelection(2);
                    }
                    else if (profileData.get(0).getWeekendDayTransport().equals("Pėsčiomis")) {
                        weekendDayTransportS.setSelection(3);
                    }
                    takingShowerPerWeek_etn.setText(profileData.get(0).getTakingShowerPerWeek());
                    showerTime_etn.setText(profileData.get(0).getShowerTime());
                    takingBathPerWeek_etn.setText(profileData.get(0).getTakingBathPerWeek());
                    Log.i("mano", "turejo priskirt");
                }
                else {
                }
            }});
        profileEdited = new BooVariable();
        profileEdited.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                openProfileFragment(view);
            }});
        UserController uc = new UserController();
        uc.getProfileForEdit(userID);
        photoReturned = new BooVariable();
        photoReturned.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {

                if (photoReturned.isBoo()) {
                    photo_iv.setImageBitmap(bitmap);
                   // photo_iv.setImageBitmap(Bitmap.createScaledBitmap(bitmap, photo_iv.getWidth(), photo_iv.getHeight(), false));

                }
            }
        });
        return view;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        photo_iv = getView().findViewById(R.id.photo_iv);
        lunchTime_tv = getView().findViewById(R.id.lunchTime_tv);
        dinnerTime_tv = getView().findViewById(R.id.dinnerTime_tv);
        sleepingTime_tv = getView().findViewById(R.id.sleepingTime_tv);
        firstName_et = getView().findViewById(R.id.firstName_et);
        lastName_et = getView().findViewById(R.id.lastName_et);
        username_et = getView().findViewById(R.id.username_et);
        //photo_et = getView().findViewById(R.id.photo_et);
        address_et = getView().findViewById(R.id.address_et);

        breakfast_dp = getView().findViewById(R.id.breakfast_dp);
        breakfast_dp.setIs24HourView(true);
        lunch_dp = getView().findViewById(R.id.lunch_dp);
        lunch_dp.setIs24HourView(true);
        dinner_dp = getView().findViewById(R.id.dinner_dp);
        dinner_dp.setIs24HourView(true);
        wakingUp_dp = getView().findViewById(R.id.wakingUpTime_dp);
        wakingUp_dp.setIs24HourView(true);
        sleeping_dp = getView().findViewById(R.id.sleepingTime_dp);
        sleeping_dp.setIs24HourView(true);

        car_cb = getView().findViewById(R.id.car_cb);
        bicycle_cb = getView().findViewById(R.id.bicycle_cb);

        workingDayTrips_etn = getView().findViewById(R.id.workingDayTrips_etn);
        weekendTrips_etn = getView().findViewById(R.id.weekendTrips_etn);
        takingShowerPerWeek_etn = getView().findViewById(R.id.takingShowerPerWeek_etn);
        showerTime_etn = getView().findViewById(R.id.showerTime_etn);
        takingBathPerWeek_etn = getView().findViewById(R.id.takingBathPerWeek_etn);

        password1_et = getView().findViewById(R.id.password1_et);
        password2_et = getView().findViewById(R.id.password2_et);

        dietS = getView().findViewById(R.id.diet_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
                R.array.diet_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dietS.setAdapter(adapter);
        dietChangeS = getView().findViewById(R.id.dietChange_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
                R.array.diet_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dietChangeS.setAdapter(adapter2);
        workingDayTransportS = getView().findViewById(R.id.workingDayTransport_spinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
                R.array.transport_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workingDayTransportS.setAdapter(adapter3);
        weekendDayTransportS = getView().findViewById(R.id.weekendDayTransport_spinner);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
                R.array.transport_array, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekendDayTransportS.setAdapter(adapter4);

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
    public static void checkUserEdited() {
        profileEdited.setBoo(true);
        if (profileEdited.getListener() != null) {
            profileEdited.getListener().onChange();
        }
    }

    public static void checkUserNotFound(List<User> list) {
        //errors = err;
        Log.i("mano", "neradom");
        //notFoundUser.getListener().onChange();
    }
    public void openProfileFragment(View view) {

        androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void showErrors() {
        if (!errors.get(0).equals("")) {
            firstName_et.setError(errors.get(0));
        }
        if (!errors.get(1).equals("")) {
            lastName_et.setError(errors.get(1));
        }
        if (!errors.get(2).equals("")) {
            photo_et.setError(errors.get(2));
        }
        if (!errors.get(3).equals("")) {
            address_et.setError(errors.get(3));
        }

        if (!errors.get(4).equals("")) {
            lunchTime_tv.setTextColor(Color.RED);
            lunchTime_tv.setText("Pietų laikas: \n" + errors.get(4));
        }
        else {
            lunchTime_tv.setTextColor(Color.GRAY);
            lunchTime_tv.setText("Pietų laikas");
        }
        if (!errors.get(5).equals("")) {
            dinnerTime_tv.setTextColor(Color.RED);
            dinnerTime_tv.setText("Vakarienės laikas: \n" + errors.get(5));
        }
        else {
            dinnerTime_tv.setTextColor(Color.GRAY);
            dinnerTime_tv.setText("Vakarienės laikas");
        }
        if (!errors.get(6).equals("")) {
            sleepingTime_tv.setTextColor(Color.RED);
            sleepingTime_tv.setText("Miego laikas: \n" + errors.get(6));
        }
        else {
            sleepingTime_tv.setTextColor(Color.GRAY);
            sleepingTime_tv.setText("Miego laikas");
        }
        if (!errors.get(7).equals("")) {
            workingDayTrips_etn.setError(errors.get(7));
        }
        if (!errors.get(8).equals("")) {
            weekendTrips_etn.setError(errors.get(8));
        }
        if (!errors.get(9).equals("")) {
            takingShowerPerWeek_etn.setError(errors.get(9));
        }
        if (!errors.get(10).equals("")) {
            showerTime_etn.setError(errors.get(10));
        }
        if (!errors.get(11).equals("")) {
            takingBathPerWeek_etn.setError(errors.get(11));
        }

    }
    public static void checkPhotoReturned(Bitmap bmp) {
        Log.i("mano", "radom:" + photoReturned);
        bitmap = bmp;
        photoReturned.setBoo(true);
        photoReturned.getListener().onChange();

    }
    public void uploadPhotoToFirebase(Bitmap bmp) {
        UserController uc = new UserController();
        uc.uploadPhoto(bmp, userID);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                photo_iv.setImageBitmap(bitmap);
                //photo_iv.setImageBitmap(Bitmap.createScaledBitmap(bitmap, photo_iv.getWidth(), photo_iv.getHeight(), false));
                bitmapToUpload = bitmap;
            }
            catch(FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}