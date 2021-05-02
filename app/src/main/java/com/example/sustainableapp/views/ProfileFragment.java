package com.example.sustainableapp.views;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.sustainableapp.R;
import com.example.sustainableapp.controllers.UserController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.IntVariable;
import com.example.sustainableapp.models.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    String userID;
    private static IntVariable foundProfile;
    static ArrayList<User> profileData;
    ImageView photo_iv;
    private static Bitmap bitmap;
    private static BooVariable photoReturned;
    TextView firstName_et, lastName_et, username_et, breakfast_et, lunch_et, dinner_et,
            wakingUpTime_et, sleepingTime_et;
    public ProfileFragment() {
    }
    public static ProfileFragment newInstance() {
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
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Button change_b = view.findViewById(R.id.change_b);
        change_b.setOnClickListener(v -> openEditProfileFragment());
        foundProfile = new IntVariable();
        foundProfile.setListener(() -> {
            if (profileData != null) {
                UserController uc = new UserController();
                String purpose = "viewProfile";
                firstName_et.setText(profileData.get(0).getFirstName());
                lastName_et.setText(profileData.get(0).getLastName());
                username_et.setText(profileData.get(0).getUsername());
                breakfast_et.setText(uc.formatTime(profileData.get(0).getBreakfastTime()));
                lunch_et.setText(uc.formatTime(profileData.get(0).getLunchTime()));
                dinner_et.setText(uc.formatTime(profileData.get(0).getDinnerTime()));
                wakingUpTime_et.setText(uc.formatTime(profileData.get(0).getWakingUpTime()));
                sleepingTime_et.setText(uc.formatTime(profileData.get(0).getSleepingTime()));
                uc.loadImageForView(profileData.get(0).getId(),profileData.get(0).getId() + ".jpg", purpose);
                Handler handler = new Handler();
                handler.postDelayed(() -> uc.loadImageForView(profileData.get(0).getId(),profileData.get(0).getId() + ".jpg", purpose), 1000);
            }
        });
        UserController uc = new UserController();
        uc.getProfile(userID, "profile");
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
        firstName_et = getView().findViewById(R.id.firstName_et);
        lastName_et = getView().findViewById(R.id.lastName_et);
        username_et = getView().findViewById(R.id.username_et);
        breakfast_et = getView().findViewById(R.id.breakfast_et);
        lunch_et = getView().findViewById(R.id.lunch_et);
        dinner_et = getView().findViewById(R.id.dinner_et);
        wakingUpTime_et = getView().findViewById(R.id.wakingUpTime_et);
        sleepingTime_et = getView().findViewById(R.id.sleepingTime_et);
    }
    public void openEditProfileFragment() {
        androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = null;
        if (fragmentManager != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
        }
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        fragment.setArguments(bundle);
        if (fragmentTransaction != null) {
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
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
    public static void checkPhotoReturned(Bitmap bmp) {
        bitmap = bmp;
        photoReturned.setBoo(true);
        photoReturned.getListener().onChange();

    }
}