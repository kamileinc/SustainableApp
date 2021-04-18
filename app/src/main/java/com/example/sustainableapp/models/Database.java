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
    ArrayList<User> userList = new ArrayList<>();
    ArrayList<EnergyAction> eaList = new ArrayList<>();
    ArrayList<TransportAction> taList = new ArrayList<>();
    ArrayList<FoodAction> faList = new ArrayList<>();
    ArrayList<SustainableAction> saList = new ArrayList<>();
    ArrayList<Fact> factList = new ArrayList<>();
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
        uploadTask.addOnFailureListener(exception -> {
        }).addOnSuccessListener(taskSnapshot -> {
        });
    }
    public void getPhotoForView(String userID, String imageName, String purpose) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl("gs://sustainableapp-e3fb1.appspot.com/images/" + imageName);
        final long ONE_MEGABYTE = 1024 * 1024;
        gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            UserController.checkPhotoReturnedToView(userID, bmp, purpose);
        }).addOnFailureListener(exception -> {
            FirebaseStorage storage1 = FirebaseStorage.getInstance();
            StorageReference gsReference1 = storage1.getReferenceFromUrl("gs://sustainableapp-e3fb1.appspot.com/nophoto.png");
            final long ONE_MEGABYTE1 = 1024 * 1024;
            gsReference1.getBytes(ONE_MEGABYTE1).addOnSuccessListener(bytes -> {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                UserController.checkPhotoReturnedToView(userID, bmp, purpose);
            }).addOnFailureListener(exception1 -> {
            });
        });
    }
    public void addNewCategoryToDB(SustainableAction sa) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("sustainableAction");
            reference.child(sa.getId()).setValue(sa);
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }

    public void getUsersSustainableActions(String userID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("sustainableAction");
            id = userID;
            readData4(list -> {
                if (!list.isEmpty()) {
                    SustainableActionController.checkUsersSAFound((ArrayList<SustainableAction>) list, purpose);
                }
                else {
                    SustainableActionController.checkUsersSANotFound((ArrayList<SustainableAction>) list, purpose);
                }
            });
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
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
    public void saveEA(EnergyAction ea) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("energyAction");
            reference.child(ea.getEaID()).setValue(ea);
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void saveTA(TransportAction ta) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("transportAction");
            reference.child(ta.getTaID()).setValue(ta);
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void saveFA(FoodAction fa) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("foodAction");
            reference.child(fa.getFaID()).setValue(fa);
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void findUserByLogin(final String username, ArrayList<String> errors, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            u = username;
            readData(list -> {
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
            });
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }

    public void findAllUsers() {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            readDataUsers(UserController::checkAllUsersFound);
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void getFactsFromDB(String findCategory){
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("facts");
            category = findCategory;
            readData5(list -> {
                if (!list.isEmpty()) {
                    FactController.checkFactsFound((ArrayList<Fact>) list);
                }
            });
        }
        catch(Error e) {
            e.printStackTrace();
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
                        String firstNameFromDB = ds.child("firstName").getValue(String.class);
                        String lastNameFromDB = ds.child("lastName").getValue(String.class);
                        String photoFromDB = ds.child("photo").getValue(String.class);
                        String breakfastTimeFromDB = ds.child("breakfastTime").getValue(String.class);
                        String lunchTimeFromDB = ds.child("lunchTime").getValue(String.class);
                        String dinnerTimeFromDB = ds.child("dinnerTime").getValue(String.class);
                        String wakingUpTimeFromDB = ds.child("wakingUpTime").getValue(String.class);
                        String sleepingTimeFromDB = ds.child("sleepingTime").getValue(String.class);
                        ArrayList<Boolean> badges = new ArrayList<>();
                        boolean boo;
                        int j =0;
                        for (DataSnapshot ignored : ds.getChildren()) {
                            try {
                                boo = ds.child("badges/" + j).getValue(Boolean.class);
                                badges.add(boo);
                            }
                            catch(Exception e) {
                                e.printStackTrace();
                            }
                            j++;
                        }
                        User us = new User(idFromDB, firstNameFromDB, lastNameFromDB, u, photoFromDB, breakfastTimeFromDB, lunchTimeFromDB, dinnerTimeFromDB,
                                wakingUpTimeFromDB, sleepingTimeFromDB, p, badges);
                        userList.add(us);
                        fireBaseCallback.onCallback(userList);
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
    public void readDataUsers(final FireBaseCallback fireBaseCallback){
        Query findUser = reference.orderByChild("username");
        findUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.i("mano", "Username found in db");
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String idFromDB = ds.getKey();
                        String firstNameFromDB = ds.child("firstName").getValue(String.class);
                        String lastNameFromDB = ds.child("lastName").getValue(String.class);
                        String usernameFromDB = ds.child("username").getValue(String.class);
                        String photoFromDB = ds.child("photo").getValue(String.class);
                        String breakfastTimeFromDB = ds.child("breakfastTime").getValue(String.class);
                        String lunchTimeFromDB = ds.child("lunchTime").getValue(String.class);
                        String dinnerTimeFromDB = ds.child("dinnerTime").getValue(String.class);
                        String wakingUpTimeFromDB = ds.child("wakingUpTime").getValue(String.class);
                        String sleepingTimeFromDB = ds.child("sleepingTime").getValue(String.class);
                        ArrayList<Boolean> badges = new ArrayList<>();
                        boolean boo;
                        int j =0;
                        for (DataSnapshot ignored : ds.getChildren()) {
                            try {
                                boo = ds.child("badges/" + j).getValue(Boolean.class);
                                badges.add(boo);
                            }
                            catch(Exception e) {
                                e.printStackTrace();
                            }
                            j++;
                        }
                        User us = new User(idFromDB, firstNameFromDB, lastNameFromDB, usernameFromDB, photoFromDB, breakfastTimeFromDB, lunchTimeFromDB, dinnerTimeFromDB,
                                wakingUpTimeFromDB, sleepingTimeFromDB, p, badges);

                        userList.add(us);
                    }
                }
                fireBaseCallback.onCallback(userList);
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
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        //String idFromDB = ds.getKey();
                        Fact  f = ds.getValue(Fact.class);
                        factList.add(f);
                    }
                }
                fireBaseCallback5.onCallback(factList);
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
    public void findUserByLogin(final String username, final String password, ArrayList<String> errors) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            u = username;
            p = password;
            String activity = "login";
            readData2(list -> {
                if (list.isEmpty()){
                    UserController.checkUserNotFound(list, errors, activity);
                }
                else {
                    UserController.checkUserFound(list, errors, activity);
                }
            });
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void readData2(final FireBaseCallback fireBaseCallback){
        Query findUser = reference.orderByChild("username").equalTo(u);
        findUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String idFromDB = ds.getKey();
                        User  user = ds.getValue(User.class);
                        if (user.getPassword().equals(p)) {
                            String firstNameFromDB = ds.child("firstName").getValue(String.class);
                            String lastNameFromDB = ds.child("lastName").getValue(String.class);
                            String photoFromDB = ds.child("photo").getValue(String.class);
                            String breakfastTimeFromDB = ds.child("breakfastTime").getValue(String.class);
                            String lunchTimeFromDB = ds.child("lunchTime").getValue(String.class);
                            String dinnerTimeFromDB = ds.child("dinnerTime").getValue(String.class);
                            String wakingUpTimeFromDB = ds.child("wakingUpTime").getValue(String.class);
                            String sleepingTimeFromDB = ds.child("sleepingTime").getValue(String.class);
                            ArrayList<Boolean> badges = new ArrayList<>();
                            boolean boo;
                            int j =0;
                            for (DataSnapshot ignored : ds.getChildren()) {
                                try {
                                    boo = ds.child("badges/" + j).getValue(Boolean.class);
                                    badges.add(boo);
                                }
                                catch(Exception e) {
                                    e.printStackTrace();
                                }
                                j++;
                            }
                            User us = new User(idFromDB, firstNameFromDB, lastNameFromDB, u, photoFromDB, breakfastTimeFromDB, lunchTimeFromDB, dinnerTimeFromDB,
                                    wakingUpTimeFromDB, sleepingTimeFromDB, p, badges);
                            userList = new ArrayList<>();
                            userList.add(us);
                        }
                        fireBaseCallback.onCallback(userList);
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
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String idFromDB = ds.getKey();
                        SustainableAction  sa = ds.getValue(SustainableAction.class);
                        String categoryFromDB = sa.getCategory();
                        String dateBeginFromDB = sa.getDateBegin();
                        String dateEndFromDB = sa.getDateEnd();
                        SustainableAction sustainableAction = new SustainableAction(idFromDB, categoryFromDB, id, dateBeginFromDB, dateEndFromDB);
                        saList.add(sustainableAction);
                    }
                }
                fireBaseCallback3.onCallback(saList);
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
            id = userID;
            ArrayList<String> err= null;
            readData3(list -> {
                if (list.isEmpty()){
                    UserController.checkUserNotFound(list, err, activity);
                }
                else {
                    UserController.checkUserFound(list, err, activity);
                }
            });
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void getProfileDataForEdit(final String userID) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            id = userID;
            ArrayList<String> err= null;
            readData3(list -> {
                if (list.isEmpty()){
                    UserController.checkUserNotFound(list, err, "profileEdit");
                }
                else {
                    UserController.checkUserFound(list, err, "profileEdit");
                }
            });
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void getEADataForEAFragment(final String eaID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("energyAction");
            id = eaID;
            readData6(list -> {
                if (list.isEmpty()){
                    EnergyActionController.checkEANotFound(eaID, purpose);
                }
                else {
                    EnergyActionController.checkEAFound(list, eaID, purpose);
                }
            });
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void getAllEA(final String userID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("energyAction");
            u = userID;
            readData13(list -> {
                if (list.isEmpty()){
                    EnergyActionController.checkEANotFound(userID, purpose);
                }
                else {
                    EnergyActionController.checkEAFound(list, userID, purpose);
                }
            });
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void getEADataForMyResults(final String userID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("energyAction");
            u = userID;
            readData11(list -> {
                if (list.isEmpty()){
                    EnergyActionController.checkEANotFound(userID,  purpose);
                }
                else {
                    EnergyActionController.checkEAFound(list, userID, purpose);
                }
            });
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void getTADataForTAFragment(final String taID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("transportAction");
            id = taID;
            readData7(list -> {
                if (list.isEmpty()){
                    TransportActionController.checkTANotFound(taID, purpose);
                }
                else {
                    TransportActionController.checkTAFound(list, taID, purpose);
                }
            });
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void getAllTA(final String userID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("transportAction");
            u = userID;
            readData14(list -> {
                if (list.isEmpty()){
                    TransportActionController.checkTANotFound(userID, purpose);
                }
                else {
                    TransportActionController.checkTAFound(list, userID,  purpose);
                }
            });
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void getTADataForMyResults(String userID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("transportAction");
            u = userID;
            readData10(list -> {
                if (list.isEmpty()){
                    TransportActionController.checkTANotFound(userID, purpose);
                }
                else {
                    TransportActionController.checkTAFound(list, userID, purpose);
                }
            });
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void getFADataForFAFragment(final String faID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("foodAction");
            id = faID;
            readData8(list -> {
                if (list.isEmpty()){
                    FoodActionController.checkFANotFound(faID, purpose);
                }
                else {
                    FoodActionController.checkFAFound(list, faID, purpose);
                }
            });
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void getAllFA(final String userID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("foodAction");
            u = userID;
            readData12(list -> {
                if (list.isEmpty()){
                    FoodActionController.checkFANotFound(userID, purpose);
                }
                else {
                    FoodActionController.checkFAFound(list, userID, purpose);
                }
            });
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void getFADataForMyResults(final String userID, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("foodAction");
            u = userID;
            readData9(list -> {
                if (list.isEmpty()){
                    FoodActionController.checkFANotFound(userID, purpose);
                }
                else {
                    FoodActionController.checkFAFound(list, userID, purpose);
                }
            });
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void readData3(final FireBaseCallback fireBaseCallback){
        Query findProfile = reference.orderByChild("id").equalTo(id);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String idFromDB = ds.getKey();
                        String firstNameFromDB = ds.child("firstName").getValue(String.class);
                        String lastNameFromDB = ds.child("lastName").getValue(String.class);
                        String usernameFromDB = ds.child("username").getValue(String.class);
                        String photoFromDB = ds.child("photo").getValue(String.class);
                        String breakfastTimeFromDB = ds.child("breakfastTime").getValue(String.class);
                        String lunchTimeFromDB = ds.child("lunchTime").getValue(String.class);
                        String dinnerTimeFromDB = ds.child("dinnerTime").getValue(String.class);
                        String wakingUpTimeFromDB = ds.child("wakingUpTime").getValue(String.class);
                        String sleepingTimeFromDB = ds.child("sleepingTime").getValue(String.class);
                        ArrayList<Boolean> badges = new ArrayList<>();
                        boolean boo;
                        int j =0;
                        for (DataSnapshot ignored : ds.getChildren()) {
                            try {
                                boo = ds.child("badges/" + j).getValue(Boolean.class);
                                badges.add(boo);
                            }
                            catch(Exception e) {
                                e.printStackTrace();
                            }
                            j++;
                        }
                        User us = new User(idFromDB, firstNameFromDB, lastNameFromDB, usernameFromDB, photoFromDB, breakfastTimeFromDB, lunchTimeFromDB, dinnerTimeFromDB,
                                wakingUpTimeFromDB, sleepingTimeFromDB, "", badges);
                        userList = new ArrayList<>();
                        userList.add(us);
                    }
                }
                fireBaseCallback.onCallback(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void readData6(final FireBaseCallback6 fireBaseCallback6){
        Query findProfile = reference.orderByChild("eaID").equalTo(id);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eaList = new ArrayList<>();
                if (snapshot.exists()) {
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String eaIDFromDB = ds.getKey();
                        EnergyAction ea = ds.getValue(EnergyAction.class);
                        String saID = ea.getId();
                        String category = ea.getCategory();
                        String userID = ea.getUserID();
                        String dateBegin = ea.getDateBegin();
                        String dateEnd = ea.getDateEnd();
                        String date = ea.getDate();
                        boolean noWater = ea.isNoWater();
                        boolean shower = ea.isShower();
                        String showerTime = ea.getShowerTime();
                        boolean bath = ea.isBath();
                        int devicesOff = ea.getDevicesOff();
                        EnergyAction energyAction = new EnergyAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date, noWater, shower, showerTime, bath, devicesOff);
                        eaList.add(energyAction);
                    }
                }
                fireBaseCallback6.onCallback(eaList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void readData13(final FireBaseCallback6 fireBaseCallback6){
        Query findProfile = reference.orderByChild("userID").equalTo(u);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eaList = new ArrayList<>();
                if (snapshot.exists()) {
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String eaIDFromDB = ds.getKey();
                        EnergyAction ea = ds.getValue(EnergyAction.class);
                        String saID = ea.getId();
                        String category = ea.getCategory();
                        String userID = ea.getUserID();
                        String dateBegin = ea.getDateBegin();
                        String dateEnd = ea.getDateEnd();
                        String date = ea.getDate();
                        boolean noWater = ea.isNoWater();
                        boolean shower = ea.isShower();
                        String showerTime = ea.getShowerTime();
                        boolean bath = ea.isBath();
                        int devicesOff = ea.getDevicesOff();
                        EnergyAction energyAction = new EnergyAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date, noWater, shower, showerTime, bath, devicesOff);
                        eaList.add(energyAction);
                    }
                }
                fireBaseCallback6.onCallback(eaList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void readData11(final FireBaseCallback6 fireBaseCallback6){
        Query findProfile = reference.orderByChild("userID").equalTo(u);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eaList = new ArrayList<>();
                List<EnergyAction> tempArr = new ArrayList<>();
                if (snapshot.exists()) {
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String eaIDFromDB = ds.getKey();
                        EnergyAction ea = ds.getValue(EnergyAction.class);
                        String saID = ea.getId();
                        String category = ea.getCategory();
                        String userID = ea.getUserID();
                        String dateBegin = ea.getDateBegin();
                        String dateEnd = ea.getDateEnd();
                        String date = ea.getDate();
                        boolean noWater = ea.isNoWater();
                        boolean shower = ea.isShower();
                        String showerTime = ea.getShowerTime();
                        boolean bath = ea.isBath();
                        int devicesOff = ea.getDevicesOff();
                        EnergyAction energyAction = new EnergyAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date, noWater, shower, showerTime, bath, devicesOff);
                        eaList.add(energyAction);
                    }
                    tempArr = new ArrayList<>();
                    for (int i = eaList.size()-7; i<eaList.size(); i++) {
                        tempArr.add(eaList.get(i));
                    }
                }
                fireBaseCallback6.onCallback(tempArr);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void readData7(final FireBaseCallback7 fireBaseCallback7){
        Query findProfile = reference.orderByChild("taID").equalTo(id);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                taList = new ArrayList<>();
                if (snapshot.exists()) {
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String eaIDFromDB = ds.getKey();
                        TransportAction ta = ds.getValue(TransportAction.class);
                        String saID = ta.getId();
                        String category = ta.getCategory();
                        String userID = ta.getUserID();
                        String dateBegin = ta.getDateBegin();
                        String dateEnd = ta.getDateEnd();
                        String date = ta.getDate();
                        boolean noTravelling = ta.isNoTravelling();
                        boolean walking = ta.isWalking();
                        int walkingKm = ta.getWalkingKM();
                        boolean bicycle = ta.isBicycle();
                        int bicycleKm = ta.getBicycleKM();
                        boolean publicTransport = ta.isPublicTransport();
                        int publicTransportKm = ta.getPublicTransportKM();
                        boolean car = ta.isCar();
                        int carKm = ta.getCarKM();
                        int carPassengersKm = ta.getCarPassengersKM();
                        int carPassengers = ta.getCarPassengers();
                        TransportAction transportAction = new TransportAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date,
                                noTravelling, walking, walkingKm,bicycle, bicycleKm, publicTransport, publicTransportKm,car,carKm,carPassengersKm,carPassengers);
                        taList.add(transportAction);
                    }
                }
                fireBaseCallback7.onCallback(taList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void readData14(final FireBaseCallback7 fireBaseCallback7){
        Query findProfile = reference.orderByChild("userID").equalTo(u);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                taList = new ArrayList<>();
                if (snapshot.exists()) {
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String eaIDFromDB = ds.getKey();
                        TransportAction ta = ds.getValue(TransportAction.class);
                        String saID = ta.getId();
                        String category = ta.getCategory();
                        String userID = ta.getUserID();
                        String dateBegin = ta.getDateBegin();
                        String dateEnd = ta.getDateEnd();
                        String date = ta.getDate();
                        boolean noTravelling = ta.isNoTravelling();
                        boolean walking = ta.isWalking();
                        int walkingKm = ta.getWalkingKM();
                        boolean bicycle = ta.isBicycle();
                        int bicycleKm = ta.getBicycleKM();
                        boolean publicTransport = ta.isPublicTransport();
                        int publicTransportKm = ta.getPublicTransportKM();
                        boolean car = ta.isCar();
                        int carKm = ta.getCarKM();
                        int carPassengersKm = ta.getCarPassengersKM();
                        int carPassengers = ta.getCarPassengers();
                        TransportAction transportAction = new TransportAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date,
                                noTravelling, walking, walkingKm,bicycle, bicycleKm, publicTransport, publicTransportKm,car,carKm,carPassengersKm,carPassengers);
                        taList.add(transportAction);
                    }
                }
                fireBaseCallback7.onCallback(taList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void readData10(final FireBaseCallback7 fireBaseCallback7){
        Query findProfile = reference.orderByChild("userID").equalTo(u);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                taList = new ArrayList<>();
                List<TransportAction> tempArr = new ArrayList<>();
                if (snapshot.exists()) {
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String eaIDFromDB = ds.getKey();
                        TransportAction ta = ds.getValue(TransportAction.class);
                        String saID = ta.getId();
                        String category = ta.getCategory();
                        String userID = ta.getUserID();
                        String dateBegin = ta.getDateBegin();
                        String dateEnd = ta.getDateEnd();
                        String date = ta.getDate();
                        boolean noTravelling = ta.isNoTravelling();
                        boolean walking = ta.isWalking();
                        int walkingKm = ta.getWalkingKM();
                        boolean bicycle = ta.isBicycle();
                        int bicycleKm = ta.getBicycleKM();
                        boolean publicTransport = ta.isPublicTransport();
                        int publicTransportKm = ta.getPublicTransportKM();
                        boolean car = ta.isCar();
                        int carKm = ta.getCarKM();
                        int carPassengersKm = ta.getCarPassengersKM();
                        int carPassengers = ta.getCarPassengers();
                        TransportAction transportAction = new TransportAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date,
                                noTravelling, walking, walkingKm,bicycle, bicycleKm, publicTransport, publicTransportKm,car,carKm,carPassengersKm,carPassengers);
                        taList.add(transportAction);
                    }
                    tempArr = new ArrayList<>();
                    for (int i = taList.size()-7; i<taList.size(); i++) {
                        tempArr.add(taList.get(i));
                    }
                }
                fireBaseCallback7.onCallback(tempArr);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void readData8(final FireBaseCallback8 fireBaseCallback8){
        Query findProfile = reference.orderByChild("faID").equalTo(id);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String eaIDFromDB = ds.getKey();
                        FoodAction fa = ds.getValue(FoodAction.class);
                        String saID = fa.getId();
                        String category = fa.getCategory();
                        String userID = fa.getUserID();
                        String dateBegin = fa.getDateBegin();
                        String dateEnd = fa.getDateEnd();
                        String date = fa.getDate();
                        int breakfastFood = fa.getBreakfastFood();
                        int lunchFood = fa.getLunchFood();
                        int dinnerFood = fa.getDinnerFood();
                        FoodAction foodAction = new FoodAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date, breakfastFood, lunchFood, dinnerFood );
                        faList = new ArrayList<>();
                        faList.add(foodAction);
                    }
                }
                fireBaseCallback8.onCallback(faList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void readData12(final FireBaseCallback8 fireBaseCallback8){
        Query findProfile = reference.orderByChild("userID").equalTo(u);
        findProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                faList = new ArrayList<>();
                if (snapshot.exists()) {
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String eaIDFromDB = ds.getKey();
                        FoodAction fa = ds.getValue(FoodAction.class);
                        String saID = fa.getId();
                        String category = fa.getCategory();
                        String userID = fa.getUserID();
                        String dateBegin = fa.getDateBegin();
                        String dateEnd = fa.getDateEnd();
                        String date = fa.getDate();
                        int breakfastFood = fa.getBreakfastFood();
                        int lunchFood = fa.getLunchFood();
                        int dinnerFood = fa.getDinnerFood();
                        FoodAction foodAction = new FoodAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date, breakfastFood, lunchFood, dinnerFood );
                        faList.add(foodAction);
                    }
                }
                fireBaseCallback8.onCallback(faList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void readData9(final FireBaseCallback8 fireBaseCallback8){
        Query findFA = reference.orderByChild("userID").equalTo(u);
        findFA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                faList = new ArrayList<>();
                List<FoodAction> tempArr = new ArrayList<>();
                if (snapshot.exists()) {
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String eaIDFromDB = ds.getKey();
                        FoodAction fa = ds.getValue(FoodAction.class);
                        String saID = fa.getId();
                        String category = fa.getCategory();
                        String userID = fa.getUserID();
                        String dateBegin = fa.getDateBegin();
                        String dateEnd = fa.getDateEnd();
                        String date = fa.getDate();
                        int breakfastFood = fa.getBreakfastFood();
                        int lunchFood = fa.getLunchFood();
                        int dinnerFood = fa.getDinnerFood();
                        FoodAction foodAction = new FoodAction(saID, category, userID, dateBegin, dateEnd, eaIDFromDB, date, breakfastFood, lunchFood, dinnerFood );
                        faList.add(foodAction);
                    }
                    tempArr = new ArrayList<>();
                    for (int i = faList.size()-7; i<faList.size(); i++) {
                        tempArr.add(faList.get(i));
                    }
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
            editData(UserController::checkUserEdited);
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void editBadge3(User user) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            us = user;
            editData5(EnergyActionController::checkBadge3Edited);
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void editBadge1(User user) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            us = user;
            editData6(FoodActionController::checkBadge1Edited);
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void editBadge2(User user) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            us = user;
            editData7(TransportActionController::checkBadge2Edited);
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void editBadge0(User user, String purpose) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            us = user;
            editData8(() -> {
                switch (purpose) {
                    case "EAController":
                        EnergyActionController.checkBadge0Edited();
                        break;
                    case "FAController":
                        FoodActionController.checkBadge0Edited();
                        break;
                    case "TAController":
                        TransportActionController.checkBadge0Edited();
                        break;
                }
            });
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void editEA(EnergyAction ea1) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("energyAction");
            ea = ea1.getEaID();
            eAction = ea1;
            editData2(EnergyActionController::checkEAEdited);
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void editFA(FoodAction fa1) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("foodAction");
            fa = fa1.getFaID();
            fAction = fa1;
            editData4(FoodActionController::checkFAEdited);
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void editTA(TransportAction ta1) {
        try {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("transportAction");
            ta = ta1.getTaID();
            tAction = ta1;
            editData3(TransportActionController::checkTAEdited);
        }
        catch(Error e) {
            e.printStackTrace();
        }
    }
    public void editData(final FireBaseCallback2 fireBaseCallback2){
        Query query = reference.orderByChild("id").equalTo(us.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot editableUser: snapshot.getChildren()){
                    editableUser.getRef().child("firstName").setValue(us.getFirstName());
                    editableUser.getRef().child("lastName").setValue(us.getLastName());
                    editableUser.getRef().child("photo").setValue(us.getPhoto());
                    editableUser.getRef().child("breakfastTime").setValue(us.getBreakfastTime());
                    editableUser.getRef().child("lunchTime").setValue(us.getLunchTime());
                    editableUser.getRef().child("dinnerTime").setValue(us.getDinnerTime());
                    editableUser.getRef().child("wakingUpTime").setValue(us.getWakingUpTime());
                    editableUser.getRef().child("sleepingTime").setValue(us.getSleepingTime());
                }
                fireBaseCallback2.onCallback();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void editData5(final FireBaseCallback2 fireBaseCallback2){
        Query query = reference.orderByChild("id").equalTo(us.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot editableUser: snapshot.getChildren()){
                    editableUser.getRef().child("badges/3").setValue(true);
                }
                fireBaseCallback2.onCallback();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void editData6(final FireBaseCallback2 fireBaseCallback2){
        Query query = reference.orderByChild("id").equalTo(us.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot editableUser: snapshot.getChildren()){
                    editableUser.getRef().child("badges/1").setValue(true);
                }
                fireBaseCallback2.onCallback();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void editData7(final FireBaseCallback2 fireBaseCallback2){
        Query query = reference.orderByChild("id").equalTo(us.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot editableUser: snapshot.getChildren()){
                    editableUser.getRef().child("badges/2").setValue(true);
                }
                fireBaseCallback2.onCallback();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void editData8(final FireBaseCallback2 fireBaseCallback2){
        Query query = reference.orderByChild("id").equalTo(us.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot editableUser: snapshot.getChildren()){
                    editableUser.getRef().child("badges/0").setValue(true);
                }
                fireBaseCallback2.onCallback();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void editData2(final FireBaseCallback2 fireBaseCallback2){
        Query query = reference.orderByChild("eaID").equalTo(ea);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void editData4(final FireBaseCallback2 fireBaseCallback2){
        Query query = reference.orderByChild("faID").equalTo(fa);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot editableEA: snapshot.getChildren()){
                    editableEA.getRef().child("breakfastFood").setValue(fAction.getBreakfastFood());
                    editableEA.getRef().child("lunchFood").setValue(fAction.getLunchFood());
                    editableEA.getRef().child("dinnerFood").setValue(fAction.getDinnerFood());
                }
                fireBaseCallback2.onCallback();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void editData3(final FireBaseCallback2 fireBaseCallback2){
        Query query = reference.orderByChild("taID").equalTo(ta);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public interface FireBaseCallback2 {
        void onCallback();
    }
}
