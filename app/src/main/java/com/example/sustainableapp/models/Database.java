package com.example.sustainableapp.models;


import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database extends Application {
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    ArrayList userList = new ArrayList<>();
    ArrayList eaList = new ArrayList<>();
    ArrayList taList = new ArrayList<>();
    ArrayList faList = new ArrayList<>();
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
                        factList.add(f);
                        fireBaseCallback5.onCallback(factList);
                    }
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
    public void getEADataForEAFragment(final String eaID) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("energyAction");
            id = eaID;
            readData6(new FireBaseCallback6() {
                @Override
                public void onCallback(List<EnergyAction> list) {

                    if (list.isEmpty()){
                        EnergyActionController.checkEANotFound(list);
                        //UserController.checkUserNotFound(list);
                    }
                    else {
                        EnergyActionController.checkEAFound(list);
                        //UserController.checkUserFound(list);
                    }


                }
            });
        }
        catch(Error e) {
        }
    }
    public void getTADataForTAFragment(final String taID) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("transportAction");
            id = taID;
            readData7(new FireBaseCallback7() {
                @Override
                public void onCallback(List<TransportAction> list) {

                    if (list.isEmpty()){
                        TransportActionController.checkTANotFound(list);
                    }
                    else {
                        TransportActionController.checkTAFound(list);
                    }


                }
            });
        }
        catch(Error e) {
        }
    }
    public void getFADataForFAFragment(final String faID) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("foodAction");
            id = faID;
            readData8(new FireBaseCallback8() {
                @Override
                public void onCallback(List<FoodAction> list) {

                    if (list.isEmpty()){
                        FoodActionController.checkFANotFound(list);
                    }
                    else {
                        FoodActionController.checkFAFound(list);
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
    public void readData6(final FireBaseCallback6 fireBaseCallback6){
        ////////////////////////////////////////////////////////////////////////////////////energy action
        Query findProfile = reference.orderByChild("eaID").equalTo(id);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                        boolean shower = ea.isShower();
                        String showerTime = ea.getShowerTime();
                        boolean bath = ea.isBath();
                        String devicesOff = ea.getDevicesOff();
                        //String usernameFromDB = snapshot.child(username).child("username").getValue(String.class);
                        EnergyAction energyAction = new EnergyAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date, shower, showerTime, bath, devicesOff);

                        eaList = new ArrayList<>();
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
    public void readData7(final FireBaseCallback7 fireBaseCallback7){
        ////////////////////////////////////////////////////////////////////////////////////transport action
        Query findProfile = reference.orderByChild("taID").equalTo(id);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                        taList = new ArrayList<>();
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
    public void editData2(final FireBaseCallback2 fireBaseCallback2){
        Query query = reference.orderByChild("eaID").equalTo(ea);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot editableEA: snapshot.getChildren()){
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
