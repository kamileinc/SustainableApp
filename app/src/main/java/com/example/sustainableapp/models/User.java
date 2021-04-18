package com.example.sustainableapp.models;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class User
{
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String photo;
    private String breakfastTime;
    private String lunchTime;
    private String dinnerTime;
    private String wakingUpTime;
    private String sleepingTime;
    private String password;
    private ArrayList<Boolean> badges;

    public User(){
    }

    public User(String id, String firstName, String lastName, String username, String photo, String breakfastTime, String lunchTime, String dinnerTime, String wakingUpTime, String sleepingTime, String password, ArrayList<Boolean> badges) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.photo = photo;
        this.breakfastTime = breakfastTime;
        this.lunchTime = lunchTime;
        this.dinnerTime = dinnerTime;
        this.wakingUpTime = wakingUpTime;
        this.sleepingTime = sleepingTime;
        this.password = password;
        this.badges = badges;
    }

    public User(String firstName, String lastName, String username, String photo, String breakfastTime, String lunchTime, String dinnerTime, String wakingUpTime, String sleepingTime, String password) {
        this.id = Long.toString(System.currentTimeMillis()/1000);
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.photo = photo;
        this.breakfastTime = breakfastTime;
        this.lunchTime = lunchTime;
        this.dinnerTime = dinnerTime;
        this.wakingUpTime = wakingUpTime;
        this.sleepingTime = sleepingTime;
        this.password = password;
        ArrayList<Boolean> arr = new ArrayList<>();
        arr.add(false);
        arr.add(false);
        arr.add(false);
        arr.add(false);
        this.badges = arr;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", photo='" + photo + '\'' +
                ", breakfastTime='" + breakfastTime + '\'' +
                ", lunchTime='" + lunchTime + '\'' +
                ", dinnerTime='" + dinnerTime + '\'' +
                ", wakingUpTime='" + wakingUpTime + '\'' +
                ", sleepingTime='" + sleepingTime + '\'' +
                ", password='" + password + '\'' +
                ", badges='" + badges + '\'' +
                '}';
    }

    public ArrayList<Boolean> getBadges() {
        return badges;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getUsername() {
        return username;
    }
    public String getPhoto() {
        return photo;
    }
    public String getBreakfastTime() {
        return breakfastTime;
    }
    public String getLunchTime() {
        return lunchTime;
    }
    public String getDinnerTime() {
        return dinnerTime;
    }
    public String getWakingUpTime() {
        return wakingUpTime;
    }
    public String getSleepingTime() {
        return sleepingTime;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}