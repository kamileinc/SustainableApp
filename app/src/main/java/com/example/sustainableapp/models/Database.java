package com.example.sustainableapp.models;


import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sustainableapp.controllers.EnergyActionController;
import com.example.sustainableapp.controllers.FactController;
import com.example.sustainableapp.controllers.FoodActionController;
import com.example.sustainableapp.controllers.SustainableActionController;
import com.example.sustainableapp.controllers.TransportActionController;
import com.example.sustainableapp.controllers.UserController;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Database extends Application {
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    ArrayList userList = new ArrayList<>();
    ArrayList<EnergyAction> eaList = new ArrayList<>();
    ArrayList<TransportAction> taList = new ArrayList<>();
    ArrayList<FoodAction> faList = new ArrayList<>();
    ArrayList saList = new ArrayList<>();
    ArrayList factList = new ArrayList<>();
    String u ="";
    String ea ="";
    EnergyAction eAction;
    String ta ="";
    TransportAction tAction;
    String fa ="";
    FoodAction fAction;
    String p = "";
    String id = "";
    String category = "";
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
    public void getPhotoForView(String userID, String imageName, String purpose) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl("gs://sustainableapp-e3fb1.appspot.com/images/" + imageName);
        final long ONE_MEGABYTE = 1024 * 1024;
        gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                UserController.checkPhotoReturnedToView(userID, bmp, purpose);
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
                        UserController.checkPhotoReturnedToView(userID, bmp, purpose);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            }
        });
    }

    public boolean addNewCategoryToDB(SustainableAction sa) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("sustainableAction");
            reference.child(sa.getId()).setValue(sa);
            return true;
        }
        catch(Error e) {
            return false;
        }
    }

    public void getUsersSustainableActions(String userID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("sustainableAction");
            id = userID;
            readData4(new FireBaseCallback3() {
                @Override
                public void onCallback(List<SustainableAction> list) {
                    if (!list.isEmpty()) {
                        Log.i("mano", "cia1");

                        SustainableActionController.checkUsersSAFound((ArrayList<SustainableAction>) list, purpose);
                    }
                    else {
                        Log.i("mano", "cia2");
                        SustainableActionController.checkUsersSANotFound((ArrayList<SustainableAction>) list, purpose);
                    }
                }
            });
        }
        catch(Error e) {
        }
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
    public boolean saveEA(EnergyAction ea) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("energyAction");
            reference.child(ea.getEaID()).setValue(ea);
            return true;
        }
        catch(Error e) {
            return false;
        }
    }
    public boolean saveTA(TransportAction ta) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("transportAction");
            reference.child(ta.getTaID()).setValue(ta);
            return true;
        }
        catch(Error e) {
            return false;
        }
    }
    public boolean saveFA(FoodAction fa) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("foodAction");
            reference.child(fa.getFaID()).setValue(fa);
            return true;
        }
        catch(Error e) {
            return false;
        }
    }
    /*
    public boolean saveFact(Fact f) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("facts");
            reference.child(f.getId()).setValue(f);
            return true;
        }
        catch(Error e) {
            return false;
        }
    }
    */
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

    public void findAllUsers() {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            //String purpose = "register";
            readDataUsers(new FireBaseCallback() {
                @Override
                public void onCallback(List<User> list) {
                //kazka daryti
                    UserController.checkAllUsersFound(list);
                }
            });
        }
        catch(Error e) {
        }
    }

    public void getFactsFromDB(String findCategory){
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("facts");
            category = findCategory;
            readData5(new FireBaseCallback5() {
                @Override
                public void onCallback(List<Fact> list) {
                    if (list.isEmpty()){
                        //UserController.checkUserNotFound(list, errors, purpose);///////////////////////////////////////////////////////////


                    }
                    else {
                        //UserController.checkUserFound(list, errors, purpose);//////////////////////////////////////////////////////////////
                        FactController.checkFactsFound((ArrayList<Fact>) list);
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
                        //User  user = ds.getValue(User.class);
                        String firstNameFromDB = ds.child("firstName").getValue(String.class);
                        String lastNameFromDB = ds.child("lastName").getValue(String.class);
                        String usernameFromDB = ds.child("username").getValue(String.class);
                        String photoFromDB = ds.child("photo").getValue(String.class);
                        String addressFromDB = ds.child("address").getValue(String.class);
                        String dietFromDB = ds.child("diet").getValue(String.class);
                        String dietChangeFromDB = ds.child("dietChange").getValue(String.class);
                        String breakfastTimeFromDB = ds.child("breakfastTime").getValue(String.class);
                        String lunchTimeFromDB = ds.child("lunchTime").getValue(String.class);
                        String dinnerTimeFromDB = ds.child("dinnerTime").getValue(String.class);
                        String wakingUpTimeFromDB = ds.child("wakingUpTime").getValue(String.class);
                        String sleepingTimeFromDB = ds.child("sleepingTime").getValue(String.class);
                        String transportFromDB = ds.child("transport").getValue(String.class);
                        String workingDayTripsFromDB = ds.child("workingDayTrips").getValue(String.class);
                        String workingDayTransportFromDB = ds.child("firstName").getValue(String.class);
                        String weekendDayTripsFromDB = ds.child("weekendDayTrips").getValue(String.class);
                        String weekendDayTransportFromDB = ds.child("weekendDayTransport").getValue(String.class);
                        String takingShowerPerWeekFromDB = ds.child("takingShowerPerWeek").getValue(String.class);
                        String showerTimeFromDB = ds.child("showerTime").getValue(String.class);
                        String takingBathPerWeekFromDB = ds.child("takingBathPerWeek").getValue(String.class);
                        GenericTypeIndicator<HashMap<Integer, Integer>> t = new GenericTypeIndicator<HashMap<Integer, Integer>>() {};

                        ArrayList<Boolean> badges = new ArrayList<>();
                        boolean boo = false;
                        Log.i("mano", "badges getchildrencount: " + ds.child("badges").getClass());
                        int j =0;
                        for (DataSnapshot ds2 : ds.getChildren()) {
                            Log.i("mano", usernameFromDB + " badge2 " + j + ": " + ds.child("badges/0").toString());
                            try {
                                boo = ds.child("badges/" + j).getValue(Boolean.class);
                                Log.i("mano", usernameFromDB + " badge3 " + j + ": " + boo);
                                badges.add(boo);
                            }
                            catch(Exception e) {
                            }
                            j++;
                        }
                        User us = new User(idFromDB, firstNameFromDB, lastNameFromDB, u, photoFromDB, addressFromDB,
                                dietFromDB, dietChangeFromDB, breakfastTimeFromDB, lunchTimeFromDB, dinnerTimeFromDB,
                                wakingUpTimeFromDB, sleepingTimeFromDB, transportFromDB, workingDayTripsFromDB, workingDayTransportFromDB,
                                weekendDayTripsFromDB, weekendDayTransportFromDB, takingShowerPerWeekFromDB, showerTimeFromDB, takingBathPerWeekFromDB, p, badges);
                        userList.add(us);
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
    public void readDataUsers(final FireBaseCallback fireBaseCallback){
        Query findUser = reference.orderByChild("username");
        findUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.i("mano", "Username found in db");
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String idFromDB = ds.getKey();

                        //User  user = ds.getValue(User.class);
                        String firstNameFromDB = ds.child("firstName").getValue(String.class);
                        String lastNameFromDB = ds.child("lastName").getValue(String.class);
                        String usernameFromDB = ds.child("username").getValue(String.class);
                        String photoFromDB = ds.child("photo").getValue(String.class);
                        String addressFromDB = ds.child("address").getValue(String.class);
                        String dietFromDB = ds.child("diet").getValue(String.class);
                        String dietChangeFromDB = ds.child("dietChange").getValue(String.class);
                        String breakfastTimeFromDB = ds.child("breakfastTime").getValue(String.class);
                        String lunchTimeFromDB = ds.child("lunchTime").getValue(String.class);
                        String dinnerTimeFromDB = ds.child("dinnerTime").getValue(String.class);
                        String wakingUpTimeFromDB = ds.child("wakingUpTime").getValue(String.class);
                        String sleepingTimeFromDB = ds.child("sleepingTime").getValue(String.class);
                        String transportFromDB = ds.child("transport").getValue(String.class);
                        String workingDayTripsFromDB = ds.child("workingDayTrips").getValue(String.class);
                        String workingDayTransportFromDB = ds.child("firstName").getValue(String.class);
                        String weekendDayTripsFromDB = ds.child("weekendDayTrips").getValue(String.class);
                        String weekendDayTransportFromDB = ds.child("weekendDayTransport").getValue(String.class);
                        String takingShowerPerWeekFromDB = ds.child("takingShowerPerWeek").getValue(String.class);
                        String showerTimeFromDB = ds.child("showerTime").getValue(String.class);
                        String takingBathPerWeekFromDB = ds.child("takingBathPerWeek").getValue(String.class);
                        GenericTypeIndicator<HashMap<Integer, Integer>> t = new GenericTypeIndicator<HashMap<Integer, Integer>>() {};

                        ArrayList<Boolean> badges = new ArrayList<>();
                        boolean boo = false;
                        Log.i("mano", "badges getchildrencount: " + ds.child("badges").getClass());
                        int j =0;
                        for (DataSnapshot ds2 : ds.getChildren()) {
                            Log.i("mano", usernameFromDB + " badge2 " + j + ": " + ds.child("badges/0").toString());
                            try {
                                boo = ds.child("badges/" + j).getValue(Boolean.class);
                                Log.i("mano", usernameFromDB + " badge3 " + j + ": " + boo);
                                badges.add(boo);
                            }
                            catch(Exception e) {
                            }
                            j++;
                        }
                            //Log.i("mano", usernameFromDB +" badge3 " + j + ": " + ds2.getValue(Boolean.class));
                            //HashMap<Integer, Integer> hm = ds2.child(Integer.toString(j)).getValue(t);
                        /*
                            try {
                                Log.i("mano", usernameFromDB + " badge " + j + ": " + hm.get(j));
                            }catch(Exception e) {

                            }
                            j++;
                        */
                        /*
                        for(int i = 0;i<ds.child("badges").getChildrenCount();i++){
                            //Boolean hm = ds.child("badges").getClass();
                            Log.i("mano", "badge " + i + " :" + boo);
                            badges.add(boo);
                        }
                        */


                        //ArrayList<Boolean> badges = ds.child("badges").getValue(ArrayList<Boolean>().class);

                        //String usernameFromDB = snapshot.child(username).child("username").getValue(String.class);

                        User us = new User(idFromDB, firstNameFromDB, lastNameFromDB, usernameFromDB, photoFromDB, addressFromDB,
                                dietFromDB, dietChangeFromDB, breakfastTimeFromDB, lunchTimeFromDB, dinnerTimeFromDB,
                                wakingUpTimeFromDB, sleepingTimeFromDB, transportFromDB, workingDayTripsFromDB, workingDayTransportFromDB,
                                weekendDayTripsFromDB, weekendDayTransportFromDB, takingShowerPerWeekFromDB, showerTimeFromDB, takingBathPerWeekFromDB, p, badges);

                        userList.add(us);

                    }
                    fireBaseCallback.onCallback(userList);
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
    public void readData5(final FireBaseCallback5 fireBaseCallback5){
        Query findFacts = reference.orderByChild("category").equalTo(category);
        findFacts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.i("mano", "Facts found in db");
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String idFromDB = ds.getKey();
                        Fact  f = ds.getValue(Fact.class);
                        Log.i("mano", f.toString());
                        factList.add(f);

                    }
                    fireBaseCallback5.onCallback(factList);
                }
                else {
                    Log.i("mano", "Facts NOT found in db");
                    fireBaseCallback5.onCallback(factList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface FireBaseCallback5 {
        void onCallback(List<Fact> list);
    }
    public interface FireBaseCallback {
        void onCallback(List<User> list);
    }
    public interface FireBaseCallback6 {
        void onCallback(List<EnergyAction> list);
    }
    public interface FireBaseCallback7 {
        void onCallback(List<TransportAction> list);
    }
    public interface FireBaseCallback8 {
        void onCallback(List<FoodAction> list);
    }
    public interface FireBaseCallback3 {
        void onCallback(List<SustainableAction> list);
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
                    Log.i("mano", "SNAPSHOT EXIST for:" + u);
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String idFromDB = ds.getKey();
                        User  user = ds.getValue(User.class);
                        // Log.i("mano", "" + userId);
                        // Log.i("mano", "" + user.getUserPassword());
                        //Log.i("mano", "" + passwordFromDB);
                        String passwordFromDB = ds.child("passsword").getValue(String.class);
                        if (user.getPassword().equals(p)) {
                            String firstNameFromDB = ds.child("firstName").getValue(String.class);
                            String lastNameFromDB = ds.child("lastName").getValue(String.class);
                            String usernameFromDB = ds.child("username").getValue(String.class);
                            String photoFromDB = ds.child("photo").getValue(String.class);
                            String addressFromDB = ds.child("address").getValue(String.class);
                            String dietFromDB = ds.child("diet").getValue(String.class);
                            String dietChangeFromDB = ds.child("dietChange").getValue(String.class);
                            String breakfastTimeFromDB = ds.child("breakfastTime").getValue(String.class);
                            String lunchTimeFromDB = ds.child("lunchTime").getValue(String.class);
                            String dinnerTimeFromDB = ds.child("dinnerTime").getValue(String.class);
                            String wakingUpTimeFromDB = ds.child("wakingUpTime").getValue(String.class);
                            String sleepingTimeFromDB = ds.child("sleepingTime").getValue(String.class);
                            String transportFromDB = ds.child("transport").getValue(String.class);
                            String workingDayTripsFromDB = ds.child("workingDayTrips").getValue(String.class);
                            String workingDayTransportFromDB = ds.child("firstName").getValue(String.class);
                            String weekendDayTripsFromDB = ds.child("weekendDayTrips").getValue(String.class);
                            String weekendDayTransportFromDB = ds.child("weekendDayTransport").getValue(String.class);
                            String takingShowerPerWeekFromDB = ds.child("takingShowerPerWeek").getValue(String.class);
                            String showerTimeFromDB = ds.child("showerTime").getValue(String.class);
                            String takingBathPerWeekFromDB = ds.child("takingBathPerWeek").getValue(String.class);
                            GenericTypeIndicator<HashMap<Integer, Integer>> t = new GenericTypeIndicator<HashMap<Integer, Integer>>() {};

                            ArrayList<Boolean> badges = new ArrayList<>();
                            boolean boo = false;
                            Log.i("mano", "badges getchildrencount: " + ds.child("badges").getClass());
                            int j =0;
                            for (DataSnapshot ds2 : ds.getChildren()) {
                                Log.i("mano", usernameFromDB + " badge2 " + j + ": " + ds.child("badges/0").toString());
                                try {
                                    boo = ds.child("badges/" + j).getValue(Boolean.class);
                                    Log.i("mano", usernameFromDB + " badge3 " + j + ": " + boo);
                                    badges.add(boo);
                                }
                                catch(Exception e) {
                                }
                                j++;
                            }

                            //String usernameFromDB = snapshot.child(username).child("username").getValue(String.class);

                            User us = new User(idFromDB, firstNameFromDB, lastNameFromDB, u, photoFromDB, addressFromDB,
                                    dietFromDB, dietChangeFromDB, breakfastTimeFromDB, lunchTimeFromDB, dinnerTimeFromDB,
                                    wakingUpTimeFromDB, sleepingTimeFromDB, transportFromDB, workingDayTripsFromDB, workingDayTransportFromDB,
                                    weekendDayTripsFromDB, weekendDayTransportFromDB, takingShowerPerWeekFromDB, showerTimeFromDB, takingBathPerWeekFromDB, p, badges);
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
    public void readData4(final FireBaseCallback3 fireBaseCallback3){
        Query findSA = reference.orderByChild("userID").equalTo(id);
        findSA.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.i("mano", "SNAPSHOT EXISTS");
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String idFromDB = ds.getKey();
                        SustainableAction  sa = ds.getValue(SustainableAction.class);
                        String categoryFromDB = sa.getCategory();
                        String dateBeginFromDB = sa.getDateBegin();
                        String dateEndFromDB = sa.getDateEnd();
                        SustainableAction sustainableAction = new SustainableAction(idFromDB, categoryFromDB, id, dateBeginFromDB, dateEndFromDB);
                        saList.add(sustainableAction);
                    }
                    fireBaseCallback3.onCallback(saList);
                }
                else {
                    Log.i("mano", "SNAPSHOT does not EXIST");
                    fireBaseCallback3.onCallback(saList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getProfileData(final String userID, String activity) {
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
                        UserController.checkUserNotFound(list, err, activity);
                        Log.i("mano", "profilis nerastas");
                    }
                    else {
                        Log.i("mano", "profilis rastas");
                        UserController.checkUserFound(list, err, activity);
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
    public void getEADataForEAFragment(final String eaID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("energyAction");
            id = eaID;
            readData6(new FireBaseCallback6() {
                @Override
                public void onCallback(List<EnergyAction> list) {

                    if (list.isEmpty()){
                        EnergyActionController.checkEANotFound(list, eaID, purpose);
                        //UserController.checkUserNotFound(list);
                    }
                    else {
                        EnergyActionController.checkEAFound(list, eaID, purpose);
                        //UserController.checkUserFound(list);
                    }


                }
            });
        }
        catch(Error e) {
        }
    }
    public void getAllEA(final String userID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("energyAction");
            u = userID;
            readData13(new FireBaseCallback6() {
                @Override
                public void onCallback(List<EnergyAction> list) {

                    if (list.isEmpty()){
                        EnergyActionController.checkEANotFound(list, userID, purpose);
                        //UserController.checkUserNotFound(list);
                    }
                    else {
                        EnergyActionController.checkEAFound(list, userID, purpose);
                        //UserController.checkUserFound(list);
                    }


                }
            });
        }
        catch(Error e) {
        }
    }
    public void getEADataForMyResults(final String userID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("energyAction");
            u = userID;
            readData11(new FireBaseCallback6() {
                @Override
                public void onCallback(List<EnergyAction> list) {

                    if (list.isEmpty()){
                        EnergyActionController.checkEANotFound(list, userID,  purpose);
                    }
                    else {
                        EnergyActionController.checkEAFound(list, userID, purpose);
                        //UserController.checkUserFound(list);
                    }


                }
            });
        }
        catch(Error e) {
        }
    }
    public void getTADataForTAFragment(final String taID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("transportAction");
            id = taID;
            readData7(new FireBaseCallback7() {
                @Override
                public void onCallback(List<TransportAction> list) {

                    if (list.isEmpty()){
                        TransportActionController.checkTANotFound(list, taID, purpose);
                    }
                    else {
                        TransportActionController.checkTAFound(list, taID, purpose);
                    }


                }
            });
        }
        catch(Error e) {
        }
    }
    public void getAllTA(final String userID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("transportAction");
            u = userID;
            readData14(new FireBaseCallback7() {
                @Override
                public void onCallback(List<TransportAction> list) {

                    if (list.isEmpty()){
                        TransportActionController.checkTANotFound(list, userID, purpose);
                    }
                    else {
                        TransportActionController.checkTAFound(list, userID,  purpose);
                    }


                }
            });
        }
        catch(Error e) {
        }
    }
    public void getTADataForMyResults(String userID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("transportAction");
            u = userID;
            readData10(new FireBaseCallback7() {
                @Override
                public void onCallback(List<TransportAction> list) {

                    if (list.isEmpty()){
                        TransportActionController.checkTANotFound(list, userID, purpose);
                    }
                    else {
                        TransportActionController.checkTAFound(list, userID, purpose);
                    }


                }
            });
        }
        catch(Error e) {
        }
    }
    public void getFADataForFAFragment(final String faID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("foodAction");
            id = faID;
            readData8(new FireBaseCallback8() {
                @Override
                public void onCallback(List<FoodAction> list) {

                    if (list.isEmpty()){
                        FoodActionController.checkFANotFound(list, faID, purpose);
                    }
                    else {
                        FoodActionController.checkFAFound(list, faID, purpose);
                    }


                }
            });
        }
        catch(Error e) {
        }
    }
    public void getAllFA(final String userID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("foodAction");
            u = userID;
            readData12(new FireBaseCallback8() {
                @Override
                public void onCallback(List<FoodAction> list) {

                    if (list.isEmpty()){
                        FoodActionController.checkFANotFound(list, userID, purpose);
                    }
                    else {
                        FoodActionController.checkFAFound(list, userID, purpose);
                    }


                }
            });
        }
        catch(Error e) {
        }
    }
    public void getFADataForMyResults(final String userID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("foodAction");
            u = userID;
            readData9(new FireBaseCallback8() {
                @Override
                public void onCallback(List<FoodAction> list) {

                    if (list.isEmpty()){
                        FoodActionController.checkFANotFound(list, userID, purpose);
                    }
                    else {
                        FoodActionController.checkFAFound(list, userID, purpose);
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
                        //User  user = ds.getValue(User.class);
                        String firstNameFromDB = ds.child("firstName").getValue(String.class);
                        String lastNameFromDB = ds.child("lastName").getValue(String.class);
                        String usernameFromDB = ds.child("username").getValue(String.class);
                        String photoFromDB = ds.child("photo").getValue(String.class);
                        String addressFromDB = ds.child("address").getValue(String.class);
                        String dietFromDB = ds.child("diet").getValue(String.class);
                        String dietChangeFromDB = ds.child("dietChange").getValue(String.class);
                        String breakfastTimeFromDB = ds.child("breakfastTime").getValue(String.class);
                        String lunchTimeFromDB = ds.child("lunchTime").getValue(String.class);
                        String dinnerTimeFromDB = ds.child("dinnerTime").getValue(String.class);
                        String wakingUpTimeFromDB = ds.child("wakingUpTime").getValue(String.class);
                        String sleepingTimeFromDB = ds.child("sleepingTime").getValue(String.class);
                        String transportFromDB = ds.child("transport").getValue(String.class);
                        String workingDayTripsFromDB = ds.child("workingDayTrips").getValue(String.class);
                        String workingDayTransportFromDB = ds.child("firstName").getValue(String.class);
                        String weekendDayTripsFromDB = ds.child("weekendDayTrips").getValue(String.class);
                        String weekendDayTransportFromDB = ds.child("weekendDayTransport").getValue(String.class);
                        String takingShowerPerWeekFromDB = ds.child("takingShowerPerWeek").getValue(String.class);
                        String showerTimeFromDB = ds.child("showerTime").getValue(String.class);
                        String takingBathPerWeekFromDB = ds.child("takingBathPerWeek").getValue(String.class);
                        GenericTypeIndicator<HashMap<Integer, Integer>> t = new GenericTypeIndicator<HashMap<Integer, Integer>>() {};

                        ArrayList<Boolean> badges = new ArrayList<>();
                        boolean boo = false;
                        Log.i("mano", "badges getchildrencount: " + ds.child("badges").getClass());
                        int j =0;
                        for (DataSnapshot ds2 : ds.getChildren()) {
                            Log.i("mano", usernameFromDB + " badge2 " + j + ": " + ds.child("badges/0").toString());
                            try {
                                boo = ds.child("badges/" + j).getValue(Boolean.class);
                                Log.i("mano", usernameFromDB + " badge3 " + j + ": " + boo);
                                badges.add(boo);
                            }
                            catch(Exception e) {
                            }
                            j++;
                        }
                        //String usernameFromDB = snapshot.child(username).child("username").getValue(String.class);

                        User us = new User(idFromDB, firstNameFromDB, lastNameFromDB, usernameFromDB, photoFromDB, addressFromDB,
                                dietFromDB, dietChangeFromDB, breakfastTimeFromDB, lunchTimeFromDB, dinnerTimeFromDB,
                                wakingUpTimeFromDB, sleepingTimeFromDB, transportFromDB, workingDayTripsFromDB, workingDayTransportFromDB,
                                weekendDayTripsFromDB, weekendDayTransportFromDB, takingShowerPerWeekFromDB, showerTimeFromDB, takingBathPerWeekFromDB, "", badges);
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
    public void readData6(final FireBaseCallback6 fireBaseCallback6){
        ////////////////////////////////////////////////////////////////////////////////////energy action
        Query findProfile = reference.orderByChild("eaID").equalTo(id);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eaList = new ArrayList<>();
                if (snapshot.exists()) {
                    //Log.i("mano", "snapshot exists");
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String eaIDFromDB = ds.getKey();
                        EnergyAction ea = ds.getValue(EnergyAction.class);
                        String saID = ea.getId();
                        String category = ea.getCategory();
                        String userID = ea.getUserID();
                        String dateBegin = ea.getDateBegin();
                        String dateEnd = ea.getDateEnd();
                        //id
                        String date = ea.getDate();
                        boolean noWater = ea.isNoWater();
                        boolean shower = ea.isShower();
                        String showerTime = ea.getShowerTime();
                        boolean bath = ea.isBath();
                        String devicesOff = ea.getDevicesOff();
                        //String usernameFromDB = snapshot.child(username).child("username").getValue(String.class);
                        EnergyAction energyAction = new EnergyAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date, noWater, shower, showerTime, bath, devicesOff);


                        eaList.add(energyAction);
                    }
                }
                else {
                }
                fireBaseCallback6.onCallback(eaList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void readData13(final FireBaseCallback6 fireBaseCallback6){
        ////////////////////////////////////////////////////////////////////////////////////energy action
        Query findProfile = reference.orderByChild("userID").equalTo(u);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eaList = new ArrayList<>();
                if (snapshot.exists()) {
                    //Log.i("mano", "snapshot exists");
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String eaIDFromDB = ds.getKey();
                        EnergyAction ea = ds.getValue(EnergyAction.class);
                        String saID = ea.getId();
                        String category = ea.getCategory();
                        String userID = ea.getUserID();
                        String dateBegin = ea.getDateBegin();
                        String dateEnd = ea.getDateEnd();
                        //id
                        String date = ea.getDate();
                        boolean noWater = ea.isNoWater();
                        boolean shower = ea.isShower();
                        String showerTime = ea.getShowerTime();
                        boolean bath = ea.isBath();
                        String devicesOff = ea.getDevicesOff();
                        //String usernameFromDB = snapshot.child(username).child("username").getValue(String.class);
                        EnergyAction energyAction = new EnergyAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date, noWater, shower, showerTime, bath, devicesOff);


                        eaList.add(energyAction);
                    }
                }
                else {
                }
                fireBaseCallback6.onCallback(eaList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void readData11(final FireBaseCallback6 fireBaseCallback6){
        ////////////////////////////////////////////////////////////////////////////////////energy action
        Query findProfile = reference.orderByChild("userID").equalTo(u);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eaList = new ArrayList<>();
                List<EnergyAction> tempArr = new ArrayList<>();
                if (snapshot.exists()) {
                    //Log.i("mano", "snapshot exists");
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String eaIDFromDB = ds.getKey();
                        EnergyAction ea = ds.getValue(EnergyAction.class);
                        String saID = ea.getId();
                        String category = ea.getCategory();
                        String userID = ea.getUserID();
                        String dateBegin = ea.getDateBegin();
                        String dateEnd = ea.getDateEnd();
                        //id
                        String date = ea.getDate();
                        boolean noWater = ea.isNoWater();
                        boolean shower = ea.isShower();
                        String showerTime = ea.getShowerTime();
                        boolean bath = ea.isBath();
                        String devicesOff = ea.getDevicesOff();
                        //String usernameFromDB = snapshot.child(username).child("username").getValue(String.class);
                        EnergyAction energyAction = new EnergyAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date, noWater, shower, showerTime, bath, devicesOff);


                        eaList.add(energyAction);
                    }
                    tempArr = new ArrayList<>();
                    for (int i = eaList.size()-7; i<eaList.size(); i++) {
                        Log.i("mano", "temparr: " + i);
                        tempArr.add(eaList.get(i));
                    }
                }
                else {
                }
                fireBaseCallback6.onCallback(tempArr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void readData7(final FireBaseCallback7 fireBaseCallback7){
        ////////////////////////////////////////////////////////////////////////////////////transport action
        Query findProfile = reference.orderByChild("taID").equalTo(id);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                taList = new ArrayList<>();
                if (snapshot.exists()) {
                    Log.i("mano", "snapshot exists FOR TA.........................");
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String eaIDFromDB = ds.getKey();
                        TransportAction ta = ds.getValue(TransportAction.class);
                        String saID = ta.getId();
                        String category = ta.getCategory();
                        String userID = ta.getUserID();
                        String dateBegin = ta.getDateBegin();
                        String dateEnd = ta.getDateEnd();
                        //id
                        String date = ta.getDate();
                        boolean noTravelling = ta.isNoTravelling();
                        boolean walking = ta.isWalking();
                        String walkingKm = ta.getWalkingKM();
                        boolean bicycle = ta.isBicycle();
                        String bicycleKm = ta.getBicycleKM();
                        boolean publicTransport = ta.isPublicTransport();
                        String publicTransportKm = ta.getPublicTransportKM();
                        boolean car = ta.isCar();
                        String carKm = ta.getCarKM();
                        String carPassengersKm = ta.getCarPassengersKM();
                        String carPassengers = ta.getCarPassengers();

                        //String usernameFromDB = snapshot.child(username).child("username").getValue(String.class);
                        TransportAction transportAction = new TransportAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date,
                                noTravelling, walking, walkingKm,bicycle, bicycleKm, publicTransport, publicTransportKm,car,carKm,carPassengersKm,carPassengers);

                        taList.add(transportAction);
                    }
                }
                else {
                }
                fireBaseCallback7.onCallback(taList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void readData14(final FireBaseCallback7 fireBaseCallback7){
        ////////////////////////////////////////////////////////////////////////////////////transport action
        Query findProfile = reference.orderByChild("userID").equalTo(u);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                taList = new ArrayList<>();
                if (snapshot.exists()) {
                    Log.i("mano", "snapshot exists FOR TA.........................");
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String eaIDFromDB = ds.getKey();
                        TransportAction ta = ds.getValue(TransportAction.class);
                        String saID = ta.getId();
                        String category = ta.getCategory();
                        String userID = ta.getUserID();
                        String dateBegin = ta.getDateBegin();
                        String dateEnd = ta.getDateEnd();
                        //id
                        String date = ta.getDate();
                        boolean noTravelling = ta.isNoTravelling();
                        boolean walking = ta.isWalking();
                        String walkingKm = ta.getWalkingKM();
                        boolean bicycle = ta.isBicycle();
                        String bicycleKm = ta.getBicycleKM();
                        boolean publicTransport = ta.isPublicTransport();
                        String publicTransportKm = ta.getPublicTransportKM();
                        boolean car = ta.isCar();
                        String carKm = ta.getCarKM();
                        String carPassengersKm = ta.getCarPassengersKM();
                        String carPassengers = ta.getCarPassengers();

                        //String usernameFromDB = snapshot.child(username).child("username").getValue(String.class);
                        TransportAction transportAction = new TransportAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date,
                                noTravelling, walking, walkingKm,bicycle, bicycleKm, publicTransport, publicTransportKm,car,carKm,carPassengersKm,carPassengers);

                        taList.add(transportAction);
                    }
                }
                else {
                }
                fireBaseCallback7.onCallback(taList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void readData10(final FireBaseCallback7 fireBaseCallback7){
        ////////////////////////////////////////////////////////////////////////////////////transport action
        Query findProfile = reference.orderByChild("userID").equalTo(u);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                taList = new ArrayList<>();
                List<TransportAction> tempArr = new ArrayList<>();
                if (snapshot.exists()) {
                    Log.i("mano", "snapshot exists FOR TA.........................");
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String eaIDFromDB = ds.getKey();
                        TransportAction ta = ds.getValue(TransportAction.class);
                        String saID = ta.getId();
                        String category = ta.getCategory();
                        String userID = ta.getUserID();
                        String dateBegin = ta.getDateBegin();
                        String dateEnd = ta.getDateEnd();
                        //id
                        String date = ta.getDate();
                        boolean noTravelling = ta.isNoTravelling();
                        boolean walking = ta.isWalking();
                        String walkingKm = ta.getWalkingKM();
                        boolean bicycle = ta.isBicycle();
                        String bicycleKm = ta.getBicycleKM();
                        boolean publicTransport = ta.isPublicTransport();
                        String publicTransportKm = ta.getPublicTransportKM();
                        boolean car = ta.isCar();
                        String carKm = ta.getCarKM();
                        String carPassengersKm = ta.getCarPassengersKM();
                        String carPassengers = ta.getCarPassengers();

                        //String usernameFromDB = snapshot.child(username).child("username").getValue(String.class);
                        TransportAction transportAction = new TransportAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date,
                                noTravelling, walking, walkingKm,bicycle, bicycleKm, publicTransport, publicTransportKm,car,carKm,carPassengersKm,carPassengers);

                        taList.add(transportAction);
                    }
                    tempArr = new ArrayList<>();
                    for (int i = taList.size()-7; i<taList.size(); i++) {
                        Log.i("mano", "temparr: " + i);
                        tempArr.add(taList.get(i));
                    }
                }
                else {
                }
                fireBaseCallback7.onCallback(tempArr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void readData8(final FireBaseCallback8 fireBaseCallback8){
        ////////////////////////////////////////////////////////////////////////////////////FOOD action
        Query findProfile = reference.orderByChild("faID").equalTo(id);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.i("mano", "snapshot exists FOR FA.........................");
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String eaIDFromDB = ds.getKey();
                        FoodAction fa = ds.getValue(FoodAction.class);
                        String saID = fa.getId();
                        String category = fa.getCategory();
                        String userID = fa.getUserID();
                        String dateBegin = fa.getDateBegin();
                        String dateEnd = fa.getDateEnd();
                        //id
                        String date = fa.getDate();
                        String breakfastFood = fa.getBreakfastFood();
                        String lunchFood = fa.getLunchFood();
                        String dinnerFood = fa.getDinnerFood();

                        FoodAction foodAction = new FoodAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date, breakfastFood, lunchFood, dinnerFood );

                        faList = new ArrayList<>();
                        faList.add(foodAction);
                    }
                }
                else {
                }
                fireBaseCallback8.onCallback(faList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void readData12(final FireBaseCallback8 fireBaseCallback8){
        ////////////////////////////////////////////////////////////////////////////////////FOOD action
        Query findProfile = reference.orderByChild("userID").equalTo(u);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                faList = new ArrayList<>();
                if (snapshot.exists()) {
                    Log.i("mano", "snapshot exists FOR FA.........................");
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String eaIDFromDB = ds.getKey();
                        FoodAction fa = ds.getValue(FoodAction.class);
                        String saID = fa.getId();
                        String category = fa.getCategory();
                        String userID = fa.getUserID();
                        String dateBegin = fa.getDateBegin();
                        String dateEnd = fa.getDateEnd();
                        //id
                        String date = fa.getDate();
                        String breakfastFood = fa.getBreakfastFood();
                        String lunchFood = fa.getLunchFood();
                        String dinnerFood = fa.getDinnerFood();

                        FoodAction foodAction = new FoodAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date, breakfastFood, lunchFood, dinnerFood );


                        faList.add(foodAction);
                    }
                }
                else {
                }
                fireBaseCallback8.onCallback(faList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void readData9(final FireBaseCallback8 fireBaseCallback8){
        ////////////////////////////////////////////////////////////////////////////////////FOOD action
        Query findFA = reference.orderByChild("userID").equalTo(u);
        findFA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                faList = new ArrayList<>();
                List<FoodAction> tempArr = new ArrayList<>();
                if (snapshot.exists()) {
                    Log.i("mano", "snapshot exists FOR FA.........................");
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        Log.i("mano", "snapshot exists FOR FA.........................CHILD");
                        String eaIDFromDB = ds.getKey();
                        FoodAction fa = ds.getValue(FoodAction.class);
                        String saID = fa.getId();
                        String category = fa.getCategory();
                        String userID = fa.getUserID();
                        String dateBegin = fa.getDateBegin();
                        String dateEnd = fa.getDateEnd();
                        //id
                        String date = fa.getDate();
                        String breakfastFood = fa.getBreakfastFood();
                        String lunchFood = fa.getLunchFood();
                        String dinnerFood = fa.getDinnerFood();

                        FoodAction foodAction = new FoodAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date, breakfastFood, lunchFood, dinnerFood );


                        faList.add(foodAction);
                    }
                    tempArr = new ArrayList<>();
                    for (int i = faList.size()-7; i<faList.size(); i++) {
                        Log.i("mano", "temparr: " + i);
                        tempArr.add(faList.get(i));
                    }
                }
                else {
                }
                fireBaseCallback8.onCallback(tempArr);
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
    public void editBadge3(User user) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            us = user;
            editData5(new FireBaseCallback2() {
                @Override
                public void onCallback() {
                    EnergyActionController.checkBadge3Edited();
                }
            });
        }
        catch(Error e) {
        }
    }
    public void editBadge1(User user) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            us = user;
            editData6(new FireBaseCallback2() {
                @Override
                public void onCallback() {
                    FoodActionController.checkBadge1Edited();
                }
            });
        }
        catch(Error e) {
        }
    }
    public void editBadge2(User user) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            us = user;
            editData7(new FireBaseCallback2() {
                @Override
                public void onCallback() {
                    TransportActionController.checkBadge2Edited();
                }
            });
        }
        catch(Error e) {
        }
    }
    public void editBadge0(User user, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            us = user;
            editData8(new FireBaseCallback2() {
                @Override
                public void onCallback() {
                    if (purpose.equals("EAController")) {
                        EnergyActionController.checkBadge0Edited();
                    }
                    else if (purpose.equals("FAController")) {
                        FoodActionController.checkBadge0Edited();
                    }
                    else if (purpose.equals("TAController")) {
                        TransportActionController.checkBadge0Edited();
                    }


                }
            });
        }
        catch(Error e) {
        }
    }
    public void editEA(EnergyAction ea1) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("energyAction");
            ea = ea1.getEaID();
            eAction = ea1;
            editData2(new FireBaseCallback2() {
                @Override
                public void onCallback() {
                    EnergyActionController.checkEAEdited();
                }
            });
        }
        catch(Error e) {
        }
    }
    public void editFA(FoodAction fa1) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("foodAction");
            fa = fa1.getFaID();
            fAction = fa1;
            editData4(new FireBaseCallback2() {
                @Override
                public void onCallback() {
                    FoodActionController.checkFAEdited();
                }
            });
        }
        catch(Error e) {
        }
    }
    public void editTA(TransportAction ta1) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("transportAction");
            ta = ta1.getTaID();
            tAction = ta1;
            editData3(new FireBaseCallback2() {
                @Override
                public void onCallback() {
                    TransportActionController.checkTAEdited();
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
    public void editData5(final FireBaseCallback2 fireBaseCallback2){
        Query query = reference.orderByChild("id").equalTo(us.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot editableUser: snapshot.getChildren()){
                    // broccoli.getRef().child("price").setValue(20);
                    editableUser.getRef().child("badges/3").setValue(true);

                }
                fireBaseCallback2.onCallback();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void editData6(final FireBaseCallback2 fireBaseCallback2){
        Query query = reference.orderByChild("id").equalTo(us.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot editableUser: snapshot.getChildren()){
                    // broccoli.getRef().child("price").setValue(20);
                    editableUser.getRef().child("badges/1").setValue(true);

                }
                fireBaseCallback2.onCallback();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void editData7(final FireBaseCallback2 fireBaseCallback2){
        Query query = reference.orderByChild("id").equalTo(us.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot editableUser: snapshot.getChildren()){
                    // broccoli.getRef().child("price").setValue(20);
                    editableUser.getRef().child("badges/2").setValue(true);

                }
                fireBaseCallback2.onCallback();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void editData8(final FireBaseCallback2 fireBaseCallback2){
        Query query = reference.orderByChild("id").equalTo(us.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot editableUser: snapshot.getChildren()){
                    // broccoli.getRef().child("price").setValue(20);
                    editableUser.getRef().child("badges/0").setValue(true);

                }
                fireBaseCallback2.onCallback();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void editData2(final FireBaseCallback2 fireBaseCallback2){
        Query query = reference.orderByChild("eaID").equalTo(ea);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot editableEA: snapshot.getChildren()){
                    editableEA.getRef().child("noWater").setValue(eAction.isNoWater());
                    editableEA.getRef().child("shower").setValue(eAction.isShower());
                    editableEA.getRef().child("showerTime").setValue(eAction.getShowerTime());
                    editableEA.getRef().child("bath").setValue(eAction.isBath());
                    editableEA.getRef().child("devicesOff").setValue(eAction.getDevicesOff());
                }
                fireBaseCallback2.onCallback();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void editData4(final FireBaseCallback2 fireBaseCallback2){
        Query query = reference.orderByChild("faID").equalTo(fa);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot editableEA: snapshot.getChildren()){
                    editableEA.getRef().child("breakfastFood").setValue(fAction.getBreakfastFood());
                    editableEA.getRef().child("lunchFood").setValue(fAction.getLunchFood());
                    editableEA.getRef().child("dinnerFood").setValue(fAction.getDinnerFood());
                }
                fireBaseCallback2.onCallback();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void editData3(final FireBaseCallback2 fireBaseCallback2){
        Query query = reference.orderByChild("taID").equalTo(ta);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot editableTA: snapshot.getChildren()){
                    editableTA.getRef().child("noTravelling").setValue(tAction.isNoTravelling());
                    editableTA.getRef().child("walking").setValue(tAction.isWalking());
                    editableTA.getRef().child("walkingKM").setValue(tAction.getWalkingKM());
                    editableTA.getRef().child("bicycle").setValue(tAction.isBicycle());
                    editableTA.getRef().child("bicycleKM").setValue(tAction.getBicycleKM());
                    editableTA.getRef().child("publicTransport").setValue(tAction.isPublicTransport());
                    editableTA.getRef().child("publicTransportKM").setValue(tAction.getPublicTransportKM());
                    editableTA.getRef().child("car").setValue(tAction.isCar());
                    editableTA.getRef().child("carKM").setValue(tAction.getCarKM());
                    editableTA.getRef().child("carPassengersKM").setValue(tAction.getCarPassengersKM());
                    editableTA.getRef().child("carPassengers").setValue(tAction.getCarPassengers());

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
