package com.example.sustainableapp.models;


import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sustainableapp.controllers.UserController;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Database extends Application {
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    ArrayList userList = new ArrayList<>();
    String u ="";
    String p = "";
    String id = "";
    User us;
    public void uploadFile(Bitmap bitmap, String userId) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://sustainableapp-e3fb1.appspot.com");
        StorageReference mountainImagesRef = storageRef.child("images/" + userId +  ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.i("downloadUrl-->", "Nay");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //sendMsg("" + downloadUrl, 2);
                Log.i("downloadUrl-->", "yay");
            }
        });

    }
    public void getPhotoForView(String imageName, String purpose) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl("gs://sustainableapp-e3fb1.appspot.com/images/" + imageName);
        final long ONE_MEGABYTE = 1024 * 1024;
        gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                UserController.checkPhotoReturnedToView(bmp, purpose);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference gsReference = storage.getReferenceFromUrl("gs://sustainableapp-e3fb1.appspot.com/nophoto.png");
                final long ONE_MEGABYTE = 1024 * 1024;
                gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        UserController.checkPhotoReturnedToView(bmp, purpose);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            }
        });
    }
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
    public void findUserByLogin(final String username, ArrayList<String> errors, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            u = username;
            //String purpose = "register";
            readData(new FireBaseCallback() {
                @Override
                public void onCallback(List<User> list) {
                    if (list.isEmpty()){
                        if (purpose.equals("register")) {
                            UserController.checkUserNotFound(list, errors, purpose);
                        }
                        else if (purpose.equals("getUserID")) {
                            UserController.checkUserNotFound(list, errors, purpose);
                        }
                    }
                    else {
                        if (purpose.equals("register")) {
                            UserController.checkUserFound(list, errors, purpose);
                        }
                        else if (purpose.equals("getUserID")) {
                            UserController.checkUserFound(list, errors, purpose);
                        }
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
    public void getProfileData(final String userID) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            //u = username;
            //p = password;
            id = userID;
            ArrayList<String> err= null;
            //Log.i("mano", "getprofiledata database");
            readData3(new FireBaseCallback() {
                @Override
                public void onCallback(List<User> list) {
                    if (list.isEmpty()){
                        UserController.checkUserNotFound(list, err, "profile");
                        Log.i("mano", "profilis nerastas");
                    }
                    else {
                        Log.i("mano", "profilis rastas");
                        UserController.checkUserFound(list, err, "profile");
                    }
                }
            });
        }
        catch(Error e) {
        }
    }
    public void getProfileDataForEdit(final String userID) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            //u = username;
            //p = password;
            id = userID;
            ArrayList<String> err= null;
            //Log.i("mano", "getprofiledata database");
            readData3(new FireBaseCallback() {
                @Override
                public void onCallback(List<User> list) {
                    if (list.isEmpty()){
                        UserController.checkUserNotFound(list, err, "profileEdit");
                        Log.i("mano", "profilis nerastas");
                    }
                    else {
                        Log.i("mano", "profilis rastas");
                        UserController.checkUserFound(list, err, "profileEdit");
                    }
                }
            });
        }
        catch(Error e) {
        }
    }
    public void readData3(final FireBaseCallback fireBaseCallback){
        //Log.i("mano", "getprofiledata readdata3: " + id);
        Query findProfile = reference.orderByChild("id").equalTo(id);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //Log.i("mano", "snapshot exists");
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String idFromDB = ds.getKey();
                        User  user = ds.getValue(User.class);
                        String firstNameFromDB = user.getFirstName();
                        String lastNameFromDB = user.getLastName();
                        String usernameFromDB = user.getUsername();
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

                        User us = new User(idFromDB, firstNameFromDB, lastNameFromDB, usernameFromDB, photoFromDB, addressFromDB,
                                dietFromDB, dietChangeFromDB, breakfastTimeFromDB, lunchTimeFromDB, dinnerTimeFromDB,
                                wakingUpTimeFromDB, sleepingTimeFromDB, transportFromDB, workingDayTripsFromDB, workingDayTransportFromDB,
                                weekendDayTripsFromDB, weekendDayTransportFromDB, takingShowerPerWeekFromDB, showerTimeFromDB, takingBathPerWeekFromDB, "");
                        // Log.i("mano", "" + us.toString());
                        userList = new ArrayList<>();
                        userList.add(us);
                    }
                }
                else {
                    //Log.i("mano", "snapshot DOES NOT exist");
                }
                fireBaseCallback.onCallback(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void editUser(User user) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            us = user;
            editData(new FireBaseCallback2() {
                @Override
                public void onCallback() {
                    UserController.checkUserEdited();
                }
            });
        }
        catch(Error e) {
        }
    }
    public void editData(final FireBaseCallback2 fireBaseCallback2){
        Query query = reference.orderByChild("id").equalTo(us.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot editableUser: snapshot.getChildren()){
                    // broccoli.getRef().child("price").setValue(20);
                    editableUser.getRef().child("firstName").setValue(us.getFirstName());
                    editableUser.getRef().child("lastName").setValue(us.getLastName());
                    editableUser.getRef().child("photo").setValue(us.getPhoto());
                    editableUser.getRef().child("diet").setValue(us.getDiet());
                    editableUser.getRef().child("dietChange").setValue(us.getDietChange());
                    editableUser.getRef().child("breakfastTime").setValue(us.getBreakfastTime());
                    editableUser.getRef().child("lunchTime").setValue(us.getLunchTime());
                    editableUser.getRef().child("dinnerTime").setValue(us.getDinnerTime());
                    editableUser.getRef().child("wakingUpTime").setValue(us.getWakingUpTime());
                    editableUser.getRef().child("sleepingTime").setValue(us.getSleepingTime());
                    editableUser.getRef().child("transport").setValue(us.getTransport());
                    editableUser.getRef().child("workingDayTrips").setValue(us.getWorkingDayTrips());
                    editableUser.getRef().child("workingDayTransport").setValue(us.getWorkingDayTransport());
                    editableUser.getRef().child("weekendDayTrips").setValue(us.getWeekendDayTrips());
                    editableUser.getRef().child("weekendDayTransport").setValue(us.getWeekendDayTransport());
                    editableUser.getRef().child("takingShowerPerWeek").setValue(us.getTakingShowerPerWeek());
                    editableUser.getRef().child("showerTime").setValue(us.getShowerTime());
                    editableUser.getRef().child("takingBathPerWeek").setValue(us.getTakingBathPerWeek());
                }
                fireBaseCallback2.onCallback();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public interface FireBaseCallback2 {
        void onCallback();
    }
}
