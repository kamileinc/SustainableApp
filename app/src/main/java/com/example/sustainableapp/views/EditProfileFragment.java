package com.example.sustainableapp.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.sustainableapp.R;
import com.example.sustainableapp.controllers.UserController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.IntVariable;
import com.example.sustainableapp.models.User;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class EditProfileFragment extends Fragment {
    private static IntVariable foundProfile;
    private static BooVariable profileEdited;
    static ArrayList<User> profileData;
    private static Bitmap bitmap;
    private static Bitmap bitmapToUpload;
    ArrayList<String> errors;
    String userID;
    EditText firstName_et,lastName_et, username_et, photo_et;
    String breakfastTime, lunchTime, dinnerTime, wakingUpTime, sleepingTime;
    TimePicker breakfast_dp, lunch_dp, dinner_dp, wakingUp_dp, sleeping_dp;
    ImageView photo_iv;
    TextView lunchTime_tv, dinnerTime_tv, sleepingTime_tv;
    private static BooVariable photoReturned;
    int hour, minutes;
    ArrayList<Boolean> badges;
    public EditProfileFragment() {
    }
    public static EditProfileFragment newInstance() {
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
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        Button update_b = view.findViewById(R.id.update_b);
        update_b.setOnClickListener(v -> {
            uploadPhotoToFirebase(bitmapToUpload);
            String firstName = firstName_et.getText().toString();
            String lastName = lastName_et.getText().toString();
            String photo = userID + ".jpg";
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
            User userToCheck = new User(userID, firstName, lastName, "", photo, breakfastTime, lunchTime, dinnerTime, wakingUpTime, sleepingTime, "", badges);
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
                Toast.makeText(getApplicationContext(), "Duomenys sėkmingai išsaugoti", Toast.LENGTH_SHORT).show();
                uc.editUser(userToCheck);
            }
        });
        ImageButton image_b = view.findViewById(R.id.image_b);
        image_b.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Pasirinkite nuotrauką"), 1);
        });
        foundProfile = new IntVariable();
        foundProfile.setListener(() -> {
            if (profileData != null) {
                UserController uc = new UserController();
                String purpose = "editProfile";
                uc.loadImageForView(profileData.get(0).getId(), profileData.get(0).getId() + ".jpg", purpose);
                firstName_et.setText(profileData.get(0).getFirstName());
                lastName_et.setText(profileData.get(0).getLastName());
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
                badges = profileData.get(0).getBadges();
            }
        });
        profileEdited = new BooVariable();
        profileEdited.setListener(this::openProfileFragment);
        UserController uc = new UserController();
        uc.getProfileForEdit(userID);
        photoReturned = new BooVariable();
        photoReturned.setListener(() -> {
            if (photoReturned.isBoo()) {
                photo_iv.setImageBitmap(bitmap);
            }
        });
        return view;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        photo_iv = Objects.requireNonNull(getView()).findViewById(R.id.photo_iv);
        lunchTime_tv = getView().findViewById(R.id.lunchTime_tv);
        dinnerTime_tv = getView().findViewById(R.id.dinnerTime_tv);
        sleepingTime_tv = getView().findViewById(R.id.sleepingTime_tv);
        firstName_et = getView().findViewById(R.id.firstName_et);
        lastName_et = getView().findViewById(R.id.lastName_et);
        username_et = getView().findViewById(R.id.username_et);
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
    }
    public static void checkUserFound(List<User> list) {
        profileData = (ArrayList<User>) list;
        if (!list.isEmpty()) {
            foundProfile.setID(list.get(0).getId());
            if (foundProfile.getListener() != null) {
                foundProfile.getListener().onChange();
            }
        }
    }
    public static void checkUserEdited() {
        profileEdited.setBoo(true);
        if (profileEdited.getListener() != null) {
            profileEdited.getListener().onChange();
        }
    }
    public void openProfileFragment() {
        androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = null;
        if (fragmentManager != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
        }
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        fragment.setArguments(bundle);
        if (fragmentTransaction != null) {
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
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
            lunchTime_tv.setTextColor(Color.RED);
            String lunchTime = getString(R.string.lunchTime) + getString(R.string.semicolonAndEnter) + errors.get(3);
            lunchTime_tv.setText(lunchTime);
        }
        else {
            lunchTime_tv.setTextColor(Color.GRAY);
            lunchTime_tv.setText(getString(R.string.lunchTime));
        }
        if (!errors.get(4).equals("")) {
            dinnerTime_tv.setTextColor(Color.RED);
            String dinnerTime = getString(R.string.dinnerTime) + getString(R.string.semicolonAndEnter) + errors.get(4);
            dinnerTime_tv.setText(dinnerTime);
        }
        else {
            dinnerTime_tv.setTextColor(Color.GRAY);
            dinnerTime_tv.setText(getString(R.string.dinnerTime));
        }
        if (!errors.get(5).equals("")) {
            sleepingTime_tv.setTextColor(Color.RED);
            String sleepingTime = getString(R.string.sleepingTime) + getString(R.string.semicolonAndEnter) + errors.get(5);
            sleepingTime_tv.setText(sleepingTime);
        }
        else {
            sleepingTime_tv.setTextColor(Color.GRAY);
            sleepingTime_tv.setText(getString(R.string.sleepingTime));
        }

    }
    public static void checkPhotoReturned(Bitmap bmp) {
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
                InputStream inputStream = null;
                if (data != null) {
                    inputStream = Objects.requireNonNull(getContext()).getContentResolver().openInputStream(data.getData());
                }
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                photo_iv.setImageBitmap(bitmap);
                bitmapToUpload = bitmap;
            }
            catch(FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}