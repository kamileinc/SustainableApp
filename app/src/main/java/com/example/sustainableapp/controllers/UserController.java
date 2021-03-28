package com.example.sustainableapp.controllers;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.sustainableapp.models.Database;
import com.example.sustainableapp.models.User;
import com.example.sustainableapp.views.AllResultsFragment;
import com.example.sustainableapp.views.EditProfileFragment;
import com.example.sustainableapp.views.EnergyActionFragment;
import com.example.sustainableapp.views.FoodActionFragment;
import com.example.sustainableapp.views.LoginActivity;
import com.example.sustainableapp.views.MyResultsFragment;
import com.example.sustainableapp.views.ProfileFragment;
import com.example.sustainableapp.views.RegisterActivity;
import com.example.sustainableapp.views.TransportActionFragment;
import com.example.sustainableapp.views.UserActivity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserController extends Application {
    public void uploadPhoto(Bitmap bitmap, String userId) {
        Database db = new Database();
        if (bitmap != null) {
            db.uploadFile(bitmap, userId);
        }

    }
    public static void checkPhotoReturnedToView(String userID, Bitmap bmp, String purpose) {
        if (purpose.equals("editProfile")) {
            EditProfileFragment.checkPhotoReturned(bmp);
        }
        else if (purpose.equals("viewProfile")) {
            ProfileFragment.checkPhotoReturned(bmp);
        }
        else if (purpose.equals("MyResultsFragment")) {
            MyResultsFragment.checkPhotoReturned(bmp);
        }
        else if (purpose.equals("AllResults")) {
            AllResultsFragment.checkPhotoReturned(userID, bmp);
        }
    }
    public static void checkAllUsersFound(List<User> list) {
        AllResultsFragment.checkAllUsersFound(list);
    }
    public void loadImageForView(String userID, String imageName, String purpose) {
        Database db = new Database();
        db.getPhotoForView(userID, imageName, purpose);

    }
    public static void checkUserFound(List<User> list, ArrayList<String> errors, String activity) {
        if (activity.equals("login")) {
            LoginActivity.checkUserFound(list, errors);
        }
        else if (activity.equals("register")) {
            RegisterActivity.checkUserFound(list, errors);
        }
        else if (activity.equals("profile")) {
            ProfileFragment.checkUserFound(list);
        }
        else if (activity.equals("profileEdit")) {
            EditProfileFragment.checkUserFound(list);
        }
        else if (activity.equals("getUserID")) {
            RegisterActivity.checkUserIDFound(list, errors);
        }
        else if (activity.equals("foodAction")) {
            FoodActionFragment.checkUserFound(list);
        }
        else if (activity.equals("UserActivity")) {
            UserActivity.checkUserFound(list);
        }
        else if (activity.equals("MyResultsFragment")) {
            MyResultsFragment.checkUserFound(list);
        }
        else if (activity.equals("EAFragment")) {
            EnergyActionFragment.checkUserFound(list);
        }
        else if (activity.equals("TAFragment")) {
            TransportActionFragment.checkUserFound(list);
        }

    }
    public static void checkUserEdited() {
        EditProfileFragment.checkUserEdited();
    }

    public static void checkUserNotFound(List<User> list, ArrayList<String> errors, String activity) {
        if (activity.equals("login")) {
            LoginActivity.checkUserNotFound(list, errors);
        }
        else if (activity.equals("register")) {
            RegisterActivity.checkUserNotFound(list, errors);
        }
        else if (activity.equals("profile")) {
            ProfileFragment.checkUserNotFound(list);
        }
        else if (activity.equals("profileEdit")) {
            EditProfileFragment.checkUserNotFound(list);
        }
        else if (activity.equals("getUserID")) {
            RegisterActivity.checkUserIDFound(list, errors);
        }
        else if (activity.equals("foodAction")) {
            FoodActionFragment.checkUserNotFound(list);
        }
        else if (activity.equals("UserActivity")) {
            UserActivity.checkUserNotFound(list);
        }
        else if (activity.equals("MyResultsFragment")) {
            MyResultsFragment.checkUserNotFound(list);
        }
        else if (activity.equals("EAFragment")) {
            EnergyActionFragment.checkUserNotFound(list);
        }
        else if (activity.equals("TAFragment")) {
            TransportActionFragment.checkUserNotFound(list);
        }

    }

/*
    public boolean validateString(String s) {
        if (!s.equals("") && s.length()<= 100) {
            return true;
        }
        return false;
    }
    */
    public boolean validateHashedPass(String s) {
        if (!s.equals("") && s.length()<= 130) {
            return true;
        }
        return false;
    }
    public List<String> validateUser(User u, String pass2, String purpose) {
        ArrayList<String> errors = new ArrayList();
        errors.add(validateString(u.getFirstName()));
        errors.add(validateString(u.getLastName()));
        errors.add(validateString(u.getPhoto()));
        errors.add(validateString(u.getAddress()));
        // laikai
        errors.add(validateTimes(u.getBreakfastTime(), u.getLunchTime()));
        errors.add(validateTimes(u.getLunchTime(), u.getDinnerTime()));
        errors.add(validateSleepingTime(u.getWakingUpTime(), u.getSleepingTime()));
        //errors.add("");
        //errors.add("");
        //skaičiai
        errors.add(validateInt(u.getWorkingDayTrips()));
        errors.add(validateInt(u.getWeekendDayTrips()));
        errors.add(validateInt(u.getTakingShowerPerWeek()));
        errors.add(validateInt(u.getShowerTime()));
        errors.add(validateInt(u.getTakingBathPerWeek()));
        //slaptazodis
        if (!purpose.equals("edit")) {
            errors.add(validatePassword(u.getPassword(), pass2));
        /*
        for (int i = 0; i <al.size(); i++) {
            if (!validateString((String) al.get(i))) {
                return false;
            }
        }*/
            if (validateUsername(u.getUsername()).equals("")) {
                Database db = new Database();
                errors.add(validateUsername(u.getUsername()));
                db.findUserByLogin(u.getUsername(), errors, purpose);
            } else {
                errors.add(validateUsername(u.getUsername()));
            }
        }
        //username
        return errors;
    }
    public List<String> findUserByUsername(String userName, String purpose) {
        ArrayList<String> errors = new ArrayList();
        Database db = new Database();
        db.findUserByLogin(userName, errors, purpose);
        return errors;
    }
    public void getAllUsers() {
        Database db = new Database();
        db.findAllUsers();
    }
    private String validateString(String s) {
        if (s.isEmpty()) {
            return "Laukas negali būti tuščias";
        }
        else if (s.length() > 100) {
            return "Laukas negali turėti daugiau nei 100 simbolių";
        }
        else {
            return "";
        }
    }
    private String validateUsername(String s) {
        if (s.isEmpty()) {
            return "Laukas negali būti tuščias";
        }
        else if (s.length() > 100) {
            return "Laukas negali turėti daugiau nei 100 simbolių";
        }
        else if (s.length() < 6) {
            return "Laukas negali turėti mažiau nei 6 simbolius";
        }
        else {
            return "";
        }
    }
    private String validateInt(String s) {
        boolean numeric = true;
        try {
            int number = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            numeric = false;
        }

        if (s.isEmpty()) {
            return "Laukas negali būti tuščias";
        }
        else if (!numeric) {
            return "Laukas turi būti skaičius";
        }
        else if (s.length() > 100) {
            return "Laukas negali būti didesnis nei triženklis skaičius";
        }
        else {
            return "";
        }
    }
    private String validateTimes(String s, String s2) {
        String[] sArr = s.split(":", 3);
        int h1 = Integer.parseInt(sArr[0]);
        int m1 = Integer.parseInt(sArr[1]);
        String[] s2Arr = s2.split(":", 3);
        int h2 = Integer.parseInt(s2Arr[0]);
        int m2 = Integer.parseInt(s2Arr[1]);
        Log.i("laikas", "h1 = " + h1 + ", m1 = " + m1);
        Log.i("laikas", "h2 = " + h2 + ", m2 = " + m2);
        if (h2 <h1) {
            return "Laikas negali būt mažesnis už praeitą įvestą laiką";
        }
        else if (h2==h1) {
            if (m2 > m1) {
                return "Laikas negali būt mažesnis už praeitą įvestą laiką";
            }
            else if (m2 == m1) {
                return "Laikas negali sutapti su praeitu įvestu laiku";
            }
            else {
                return "";
            }
        }
        else {
            return "";
        }
    }
    public String addTime(String s, int minutes) {
        String d2Str = "0:0";
        if (!s.equals("")) {
            String[] sArr = s.split(":", 3);
            int h1 = Integer.parseInt(sArr[0]);
            int m1 = Integer.parseInt(sArr[1]);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date today = new Date(System.currentTimeMillis());
            String todayStr = formatter.format(today);

            d2Str = "0:0";
            try {
                Date d = formatter.parse(s);
                Calendar c = Calendar.getInstance();
                String beginDateStr = formatter.format(d);
                c.setTime(d);
                c.add(Calendar.MINUTE, minutes);
                Date d2 = c.getTime();
                d2Str = formatter.format(d2);
            } catch (Exception e) {

            }
            Log.i("LYGINAMAS laikas", "h1 = " + h1 + ", m1 = " + m1);
            Log.i("d2Str", d2Str);
        }
        return d2Str;
    }
    public boolean isUpcomingTime(String s) {

        String[] sArr = s.split(":", 3);
        int h1 = Integer.parseInt(sArr[0]);
        int m1 = Integer.parseInt(sArr[1]);
        SimpleDateFormat formatter= new SimpleDateFormat("HH:mm");
        Date today = new Date(System.currentTimeMillis());
        String todayStr = formatter.format(today);
        String[] s2Arr = todayStr.split(":", 3);
        int h2 = Integer.parseInt(s2Arr[0]);
        int m2 = Integer.parseInt(s2Arr[1]);
        Log.i("LYGINAMAS laikas", "h1 = " + h1 + ", m1 = " + m1);
        Log.i("DABAR laikas", "h2 = " + h2 + ", m2 = " + m2);
        if (h1>h2) {
            return true;
        }
        else if (h1==h2) {
            if (m1>m2) {
                return true;
            }
            else if (m1==m2) {
                return false;
            }
        }
        else {
            return false;
        }
        return false;
    }
    private String validateSleepingTime(String s, String s2) {
        String[] sArr = s.split(":", 3);
        int h1 = Integer.parseInt(sArr[0]);
        int m1 = Integer.parseInt(sArr[1]);
        String[] s2Arr = s2.split(":", 3);
        int h2 = Integer.parseInt(s2Arr[0]);
        int m2 = Integer.parseInt(s2Arr[1]);
        Log.i("laikas", "h1 = " + h1 + ", m1 = " + m1);
        Log.i("laikas", "h2 = " + h2 + ", m2 = " + m2);

        if (h2==h1 && m2 == m1) {
            return "Laikas negali sutapti su praeitu įvestu laiku";
        }
        else {
            return "";
        }
    }
    public String formatTime(String s) {
        String[] sArr = s.split(":", 3);
        int h1 = Integer.parseInt(sArr[0]);
        int m1 = Integer.parseInt(sArr[1]);
        String result = "";

        if (sArr[0].length()==1) {
            result = result + "0" + sArr[0] + ":";
        }
        else if (sArr[0].length()==2) {
            result = result + sArr[0] + ":";
        }
        if (sArr[1].length()==1) {
            result = result + "0" + sArr[1];
        }
        else if (sArr[1].length()==2) {
            result = result + sArr[1];
        }

        return result;

    }
    public ArrayList<Integer> getHourAndMinutes(String s) {
        ArrayList<Integer> arr = new ArrayList();
        String[] sArr = s.split(":", 3);
        int h1 = Integer.parseInt(sArr[0]);
        int m1 = Integer.parseInt(sArr[1]);
        arr.add(h1);
        arr.add(m1);
        return arr;

    }
    public boolean passwordWithSpecChar(String s) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        boolean passwordContainsSpecChar = m.find();
        Log.i("mano", "boolean (spec char): " + passwordContainsSpecChar);
        if (passwordContainsSpecChar) {
            return true;
        }
        return false;
    }
    public boolean passwordWithNumber(String s) {
        Pattern p = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        boolean passwordContainsNumber = m.find();
        Log.i("mano", "boolean (digit): " + passwordContainsNumber);
        if (passwordContainsNumber) {
            return true;
        }
        return false;
    }
    public boolean passwordWithLowerLetter(String s) {
        Pattern p = Pattern.compile("[a-z]");
        Matcher m = p.matcher(s);
        boolean passwordContainsLowerLetter= m.find();
        Log.i("mano", "boolean (lower letter): " + passwordContainsLowerLetter);
        if (passwordContainsLowerLetter) {
            return true;
        }
        return false;
    }
    public boolean passwordWithUpperLetter(String s) {
        Pattern p = Pattern.compile("[A-Z]");
        Matcher m = p.matcher(s);
        boolean passwordContainsUpperLetter= m.find();
        Log.i("mano", "boolean (upper letter): " + passwordContainsUpperLetter);
        if (passwordContainsUpperLetter) {
            return true;
        }
        return false;
    }

    public String validatePassword(String pass1, String pass2) {
            if (pass1.equals(pass2)) {
                Log.i("mano", "passwords match");
                if (pass1.length() >= 8) {
                    Log.i("mano", "password has 8 or more chars");
                    if (passwordWithSpecChar(pass1)) {
                        Log.i("mano", "password has a spec char");
                        if (passwordWithNumber(pass1)) {
                            Log.i("mano", "password has a number");
                            if (passwordWithLowerLetter(pass1)) {
                                Log.i("mano", "password has a lower letter");
                                if (passwordWithUpperLetter(pass1)) {
                                    Log.i("mano", "password has an upper letter");
                                    return "";
                                }
                                else {
                                    return "Slaptažodį turi sudaryti spec. simbolis, skaičius, mažoji raidė ir didžioji raidė";
                                }
                            }
                            else {
                                return "Slaptažodį turi sudaryti spec. simbolis, skaičius, mažoji raidė ir didžioji raidė";
                            }
                        }
                        else {
                            return "Slaptažodį turi sudaryti spec. simbolis, skaičius, mažoji raidė ir didžioji raidė";
                        }
                    }
                    else {
                        return "Slaptažodį turi sudaryti spec. simbolis, skaičius, mažoji raidė ir didžioji raidė";
                    }
                }
                else {
                    return "Slaptažodį turi sudaryti bent 8 simboliai";
                }
            }
            else {
                return "Slaptažodžiai nesutampa";
            }
    }

    public String hashingPassword(String s) {
        String hashedPass = "";
        byte[] salt = new byte[0];
        try {
            salt = getSalt();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        hashedPass = get_SHA_512_SecurePassword(s, salt);
        Log.i("mano", "hashed pass:" + hashedPass);
        return hashedPass;

    }
    private static String get_SHA_512_SecurePassword(String passwordToHash, byte[] salt)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }
    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        Log.i("mano", "salt: " +salt);
        String constantSalt = "[B@154c8bf";
        salt = constantSalt.getBytes();
        return salt;
    }

    /*
    public String hashingPassword(String passwordToHash, String salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
    */
    //REGISTER
    public boolean addUserToDB(ArrayList al) {
        Log.i("mano", "add user to db1");
            Database db = new Database();
            User u = new User(al.get(0).toString(), al.get(1).toString(),al.get(2).toString(), al.get(3).toString(), al.get(4).toString(), al.get(5).toString(),al.get(6).toString(), al.get(7).toString(),al.get(8).toString(),al.get(9).toString(),al.get(10).toString(),al.get(11).toString(),al.get(12).toString(),al.get(13).toString(),al.get(14).toString(),al.get(15).toString(),al.get(16).toString(),al.get(17).toString(),al.get(18).toString(),al.get(19).toString(), hashingPassword((String) al.get(20)), new ArrayList<Boolean>());
            if (db.saveUser(u)) {
                return true;
            }


        return false;
    }
    public void validateUserForAddingToDB(User u, String pass2) {
        //&& validatePassword(u.getPassword())
        String purpose = "register";
        List<String> errors = validateUser(u, pass2, purpose);
            Log.i("mano", "validated, username:" + u.getUsername());
    }
/*
    public boolean checkUserinDBByUsername(String username) {
        Database db = new Database();
        final String un = username;
        if (validateString(un)) {
            db.findUserByLogin(un);
            return true;
        }
        else {
            return false;
        }
    }
*/
    //login
    public ArrayList<String> validateLogin(String username, String password) {
        ArrayList<String> errors = new ArrayList();
        errors.add(validateString(username));
        errors.add(validateString(password));
        return errors;
    }
    public boolean checkUserinDB(String username, String password, ArrayList<String> errors) {
        Database db = new Database();
        final String un = username;
        final String p = password;
        Log.i("mano", "username: " + un + ", pass: " + password.length());
        Log.i("mano", "Check user in db2");
        db.findUserByLogin(un, p, errors);
        return true;


    }
    public boolean getProfile(String userId, String activity) {
        //Log.i("mano", "getprofiledata uc");
        Database db = new Database();
        final String id = userId;
        db.getProfileData(id, activity);
        return true;


    }
    public boolean getProfileForEdit(String userId) {
        //Log.i("mano", "getprofiledata uc");
        Database db = new Database();
        final String id = userId;
        db.getProfileDataForEdit(id);
        return true;


    }
    public boolean editUser(User u) {
        Database db = new Database();
        db.editUser(u);
        return true;


    }
    public boolean isLater(String s) {
        String[] sArr = s.split(":", 3);
        int h1 = Integer.parseInt(sArr[0]);
        int m1 = Integer.parseInt(sArr[1]);
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatterH= new SimpleDateFormat("HH");
        SimpleDateFormat formatterM= new SimpleDateFormat("mm");
        int h2 = Integer.parseInt(formatterH.format(currentTime));
        int m2 = Integer.parseInt(formatterM.format(currentTime));
        Log.i("laikas", "h1 = " + h1 + ", m1 = " + m1);
        Log.i("laikas", "h2 = " + h2 + ", m2 = " + m2);
        Log.i("laikas", "current time: h1 = " + currentTime);
        if (h2 >h1) {
            return true;
        }
        else if (h2==h1) {
            if (m2 > m1) {
                return true;
            }
            else if (m2 == m1) {
                return true;
            }
        }
        return false;
    }

}