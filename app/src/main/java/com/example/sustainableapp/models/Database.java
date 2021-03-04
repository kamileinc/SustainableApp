package com.example.sustainableapp.models;


import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sustainableapp.controllers.UserController;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Database extends Application {
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    ArrayList userList = new ArrayList<>();
    String u ="";
    String p = "";
    //REGISTER
    public boolean saveUser(User u) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            reference.child(u.getId()).setValue(u);

            return true;
        }
        catch(Error e) {
            return false;
        }
    }
    //find user by username
    public void findUserByLogin(final String username, ArrayList<String> errors) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            u = username;
            String activity = "register";
            readData(new FireBaseCallback() {
                @Override
                public void onCallback(List<User> list) {
                    if (list.isEmpty()){
                        UserController.checkUserNotFound(list, errors, activity);
                    }
                    else {
                        UserController.checkUserFound(list, errors, activity);
                    }
                }
            });
        }
        catch(Error e) {
        }
    }

    public void readData(final FireBaseCallback fireBaseCallback){
        Query findUser = reference.orderByChild("username").equalTo(u);
        findUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.i("mano", "Username found in db");
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String idFromDB = ds.getKey();
                        User  user = ds.getValue(User.class);
                        userList.add(user);
                        fireBaseCallback.onCallback(userList);
                    }
                }
                else {
                    Log.i("mano", "Username NOT found in db");
                    fireBaseCallback.onCallback(userList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface FireBaseCallback {
        void onCallback(List<User> list);
    }
    //LOGIN
    public void findUserByLogin(final String username, final String password, ArrayList<String> errors) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            u = username;
            p = password;
            String activity = "login";
            readData2(new FireBaseCallback() {
                @Override
                public void onCallback(List<User> list) {
                    if (list.isEmpty()){
                        Log.i("mano", "vartotojas nerastas");

                        UserController.checkUserNotFound(list, errors, activity);
                    }
                    else {
                        Log.i("mano", "" + list.get(0).toString());
                        UserController.checkUserFound(list, errors, activity);
                    }
                }
            });
        }
        catch(Error e) {
        }
    }

    public void readData2(final FireBaseCallback fireBaseCallback){
        Query findUser = reference.orderByChild("username").equalTo(u);
        findUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.i("mano", "SNAPSHOT EXIST");
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String idFromDB = ds.getKey();
                        User  user = ds.getValue(User.class);
                        // Log.i("mano", "" + userId);
                        // Log.i("mano", "" + user.getUserPassword());
                        //Log.i("mano", "" + passwordFromDB);
                        if (user.getPassword().equals(p)) {
                            String firstNameFromDB = user.getFirstName();
                            String lastNameFromDB = user.getLastName();
                            String photoFromDB = user.getPhoto();
                            String addressFromDB = user.getAddress();
                            String dietFromDB = user.getDiet();
                            String dietChangeFromDB = user.getDietChange();
                            String breakfastTimeFromDB = user.getBreakfastTime();
                            String lunchTimeFromDB = user.getLunchTime();
                            String dinnerTimeFromDB = user.getDinnerTime();
                            String wakingUpTimeFromDB = user.getWakingUpTime();
                            String sleepingTimeFromDB = user.getSleepingTime();
                            String transportFromDB = user.getTransport();
                            String workingDayTripsFromDB = user.getWorkingDayTrips();
                            String workingDayTransportFromDB = user.getWorkingDayTransport();
                            String weekendDayTripsFromDB = user.getWeekendDayTrips();
                            String weekendDayTransportFromDB = user.getWeekendDayTransport();
                            String takingShowerPerWeekFromDB = user.getTakingShowerPerWeek();
                            String showerTimeFromDB = user.getShowerTime();
                            String takingBathPerWeekFromDB = user.getTakingBathPerWeek();

                            //String usernameFromDB = snapshot.child(username).child("username").getValue(String.class);

                            User us = new User(idFromDB, firstNameFromDB, lastNameFromDB, u, photoFromDB, addressFromDB,
                                    dietFromDB, dietChangeFromDB, breakfastTimeFromDB, lunchTimeFromDB, dinnerTimeFromDB,
                                    wakingUpTimeFromDB, sleepingTimeFromDB, transportFromDB, workingDayTripsFromDB, workingDayTransportFromDB,
                                    weekendDayTripsFromDB, weekendDayTransportFromDB, takingShowerPerWeekFromDB, showerTimeFromDB, takingBathPerWeekFromDB, p);
                            // Log.i("mano", "" + us.toString());
                            userList = new ArrayList<>();
                            userList.add(us);
                            //myCallback.onCallback(u);
                            //userList.add(u);
                            // Toast.makeText(getApplicationContext(),"RASTAS",Toast. LENGTH_LONG).show();
                            fireBaseCallback.onCallback(userList);
                        } else {
                            // Toast.makeText(getApplicationContext(),"nerastas..............",Toast. LENGTH_LONG).show();
                            fireBaseCallback.onCallback(userList);
                        }
                    }
                }
                else {
                    fireBaseCallback.onCallback(userList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
