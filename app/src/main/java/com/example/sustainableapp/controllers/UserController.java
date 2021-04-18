package com.example.sustainableapp.controllers;

import android.annotation.SuppressLint;
import android.app.Application;
import android.graphics.Bitmap;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        switch (purpose) {
            case "editProfile":
                EditProfileFragment.checkPhotoReturned(bmp);
                break;
            case "viewProfile":
                ProfileFragment.checkPhotoReturned(bmp);
                break;
            case "MyResultsFragment":
                MyResultsFragment.checkPhotoReturned(bmp);
                break;
            case "AllResults":
                AllResultsFragment.checkPhotoReturned(userID, bmp);
                break;
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
        switch (activity) {
            case "login":
                LoginActivity.checkUserFound(list);
                break;
            case "register":
                RegisterActivity.checkUserFound(list, errors);
                break;
            case "profile":
                ProfileFragment.checkUserFound(list);
                break;
            case "profileEdit":
                EditProfileFragment.checkUserFound(list);
                break;
            case "getUserID":
                RegisterActivity.checkUserIDFound(list);
                break;
            case "foodAction":
                FoodActionFragment.checkUserFound(list);
                break;
            case "UserActivity":
                UserActivity.checkUserFound(list);
                break;
            case "MyResultsFragment":
                MyResultsFragment.checkUserFound(list);
                break;
            case "EAFragment":
                EnergyActionFragment.checkUserFound(list);
                break;
            case "TAFragment":
                TransportActionFragment.checkUserFound(list);
                break;
        }
    }
    public static void checkUserEdited() {
        EditProfileFragment.checkUserEdited();
    }
    public static void checkUserNotFound(List<User> list, ArrayList<String> errors, String activity) {
        switch (activity) {
            case "login":
                LoginActivity.checkUserNotFound();
                break;
            case "register":
                RegisterActivity.checkUserNotFound(errors);
                break;
            case "getUserID":
                RegisterActivity.checkUserIDFound(list);
                break;

        }
    }
    public List<String> validateUser(User u, String pass2, String purpose) {
        ArrayList<String> errors = new ArrayList<>();
        errors.add(validateString(u.getFirstName()));
        errors.add(validateString(u.getLastName()));
        errors.add(validateString(u.getPhoto()));
        errors.add(validateTimes(u.getBreakfastTime(), u.getLunchTime()));
        errors.add(validateTimes(u.getLunchTime(), u.getDinnerTime()));
        errors.add(validateSleepingTime(u.getWakingUpTime(), u.getSleepingTime()));
        if (!purpose.equals("edit")) {
            errors.add(validatePassword(u.getPassword(), pass2));
            if (validateUsername(u.getUsername()).equals("")) {
                Database db = new Database();
                errors.add(validateUsername(u.getUsername()));
                db.findUserByLogin(u.getUsername(), errors, purpose);
            } else {
                errors.add(validateUsername(u.getUsername()));
            }
        }
        return errors;
    }
    public void findUserByUsername(String userName, String purpose) {
        ArrayList<String> errors = new ArrayList<>();
        Database db = new Database();
        db.findUserByLogin(userName, errors, purpose);
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
    private String validateTimes(String s, String s2) {
        String[] sArr = s.split(":", 3);
        int h1 = Integer.parseInt(sArr[0]);
        int m1 = Integer.parseInt(sArr[1]);
        String[] s2Arr = s2.split(":", 3);
        int h2 = Integer.parseInt(s2Arr[0]);
        int m2 = Integer.parseInt(s2Arr[1]);
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
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            d2Str = "0:0";
            try {
                Date d = formatter.parse(s);
                Calendar c = Calendar.getInstance();
                if (d != null) {
                    c.setTime(d);
                }
                c.add(Calendar.MINUTE, minutes);
                Date d2 = c.getTime();
                d2Str = formatter.format(d2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return d2Str;
    }
    public boolean isUpcomingTime(String s) {
        String[] sArr = s.split(":", 3);
        int h1 = Integer.parseInt(sArr[0]);
        int m1 = Integer.parseInt(sArr[1]);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("HH:mm");
        Date today = new Date(System.currentTimeMillis());
        String todayStr = formatter.format(today);
        String[] s2Arr = todayStr.split(":", 3);
        int h2 = Integer.parseInt(s2Arr[0]);
        int m2 = Integer.parseInt(s2Arr[1]);
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
        if (h2==h1 && m2 == m1) {
            return "Laikas negali sutapti su praeitu įvestu laiku";
        }
        else {
            return "";
        }
    }
    public String formatTime(String s) {
        String[] sArr = s.split(":", 3);
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
        ArrayList<Integer> arr = new ArrayList<>();
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
        return m.find();
    }
    public boolean passwordWithNumber(String s) {
        Pattern p = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        return m.find();
    }
    public boolean passwordWithLowerLetter(String s) {
        Pattern p = Pattern.compile("[a-z]");
        Matcher m = p.matcher(s);
        return m.find();
    }
    public boolean passwordWithUpperLetter(String s) {
        Pattern p = Pattern.compile("[A-Z]");
        Matcher m = p.matcher(s);
        return m.find();
    }

    public String validatePassword(String pass1, String pass2) {
            if (pass1.equals(pass2)) {
                if (pass1.length() >= 8) {
                    if (passwordWithSpecChar(pass1)) {
                        if (passwordWithNumber(pass1)) {
                            if (passwordWithLowerLetter(pass1)) {
                                if (passwordWithUpperLetter(pass1)) {
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
        String hashedPass;
        byte[] salt = new byte[0];
        try {
            salt = getSalt();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        hashedPass = get_SHA_512_SecurePassword(s, salt);
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
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
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
        String constantSalt = "[B@154c8bf";
        salt = constantSalt.getBytes();
        return salt;
    }

    public boolean addUserToDB(User u) {
        Database db = new Database();
        u.setPassword(hashingPassword(u.getPassword()));
        return db.saveUser(u);
    }
    public void validateUserForAddingToDB(User u, String pass2) {
        String purpose = "register";
        validateUser(u, pass2, purpose);
    }
    public ArrayList<String> validateLogin(String username, String password) {
        ArrayList<String> errors = new ArrayList<>();
        errors.add(validateString(username));
        errors.add(validateString(password));
        return errors;
    }
    public void checkUserinDB(String username, String password, ArrayList<String> errors) {
        Database db = new Database();
        db.findUserByLogin(username, password, errors);
    }
    public void getProfile(String userId, String activity) {
        Database db = new Database();
        db.getProfileData(userId, activity);
    }
    public void getProfileForEdit(String userId) {
        Database db = new Database();
        db.getProfileDataForEdit(userId);
    }
    public void editUser(User u) {
        Database db = new Database();
        db.editUser(u);
    }
    public boolean isLater(String s) {
        String[] sArr = s.split(":", 3);
        int h1 = Integer.parseInt(sArr[0]);
        int m1 = Integer.parseInt(sArr[1]);
        Date currentTime = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatterH= new SimpleDateFormat("HH");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatterM= new SimpleDateFormat("mm");
        int h2 = Integer.parseInt(formatterH.format(currentTime));
        int m2 = Integer.parseInt(formatterM.format(currentTime));
        if (h2 >h1) {
            return true;
        }
        else if (h2==h1) {
            if (m2 > m1) {
                return true;
            }
            else return m2 == m1;
        }
        return false;
    }
}