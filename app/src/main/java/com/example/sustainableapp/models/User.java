package com.example.sustainableapp.models;

import java.util.ArrayList;
import java.util.HashMap;

public class User
{

    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String photo;
    private String address;
    private String diet;
    private String dietChange;
    private String breakfastTime;
    private String lunchTime;
    private String dinnerTime;
    private String wakingUpTime;
    private String sleepingTime;
    private String transport;
    private String workingDayTrips;
    private String workingDayTransport;
    private String weekendDayTrips;
    private String weekendDayTransport;
    private String takingShowerPerWeek;
    private String showerTime;
    private String takingBathPerWeek;
    private String password;
    private ArrayList<Boolean> badges;

    public User(){
    }

    public User(String id, String firstName, String lastName, String username, String photo, String address, String diet, String dietChange, String breakfastTime, String lunchTime, String dinnerTime, String wakingUpTime, String sleepingTime, String transport, String workingDayTrips, String workingDayTransport, String weekendDayTrips, String weekendDayTransport, String takingShowerPerWeek, String showerTime, String takingBathPerWeek, String password, ArrayList<Boolean> badges) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.photo = photo;
        this.address = address;
        this.diet = diet;
        this.dietChange = dietChange;
        this.breakfastTime = breakfastTime;
        this.lunchTime = lunchTime;
        this.dinnerTime = dinnerTime;
        this.wakingUpTime = wakingUpTime;
        this.sleepingTime = sleepingTime;
        this.transport = transport;
        this.workingDayTrips = workingDayTrips;
        this.workingDayTransport = workingDayTransport;
        this.weekendDayTrips = weekendDayTrips;
        this.weekendDayTransport = weekendDayTransport;
        this.takingShowerPerWeek = takingShowerPerWeek;
        this.showerTime = showerTime;
        this.takingBathPerWeek = takingBathPerWeek;
        this.password = password;
        this.badges = badges;
    }

    public User(String firstName, String lastName, String username, String photo, String address, String diet, String dietChange, String breakfastTime, String lunchTime, String dinnerTime, String wakingUpTime, String sleepingTime, String transport, String workingDayTrips, String workingDayTransport, String weekendDayTrips, String weekendDayTransport, String takingShowerPerWeek, String showerTime, String takingBathPerWeek, String password, ArrayList<Boolean> badges) {
        this.id = Long.toString(System.currentTimeMillis()/1000);
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.photo = photo;
        this.address = address;
        this.diet = diet;
        this.dietChange = dietChange;
        this.breakfastTime = breakfastTime;
        this.lunchTime = lunchTime;
        this.dinnerTime = dinnerTime;
        this.wakingUpTime = wakingUpTime;
        this.sleepingTime = sleepingTime;
        this.transport = transport;
        this.workingDayTrips = workingDayTrips;
        this.workingDayTransport = workingDayTransport;
        this.weekendDayTrips = weekendDayTrips;
        this.weekendDayTransport = weekendDayTransport;
        this.takingShowerPerWeek = takingShowerPerWeek;
        this.showerTime = showerTime;
        this.takingBathPerWeek = takingBathPerWeek;
        this.password = password;
        ArrayList<Boolean> arr = new ArrayList<>();
        arr.add(false);
        arr.add(false);
        arr.add(false);
        arr.add(false);
        arr.add(false);
        arr.add(false);
        this.badges = arr;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", photo='" + photo + '\'' +
                ", address='" + address + '\'' +
                ", diet='" + diet + '\'' +
                ", dietChange='" + dietChange + '\'' +
                ", breakfastTime='" + breakfastTime + '\'' +
                ", lunchTime='" + lunchTime + '\'' +
                ", dinnerTime='" + dinnerTime + '\'' +
                ", wakingUpTime='" + wakingUpTime + '\'' +
                ", sleepingTime='" + sleepingTime + '\'' +
                ", transport='" + transport + '\'' +
                ", workingDayTrips='" + workingDayTrips + '\'' +
                ", workingDayTransport='" + workingDayTransport + '\'' +
                ", weekendDayTrips='" + weekendDayTrips + '\'' +
                ", weekendDayTransport='" + weekendDayTransport + '\'' +
                ", takingShowerPerWeek='" + takingShowerPerWeek + '\'' +
                ", showerTime='" + showerTime + '\'' +
                ", takingBathPerWeek='" + takingBathPerWeek + '\'' +
                ", password='" + password + '\'' +
                ", badges='" + badges + '\'' +
                '}';
    }

    public ArrayList<Boolean> getBadges() {
        return badges;
    }

    public void setBadges(ArrayList<Boolean> badges) {
        this.badges = badges;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getBreakfastTime() {
        return breakfastTime;
    }

    public void setBreakfastTime(String breakfastTime) {
        this.breakfastTime = breakfastTime;
    }

    public String getLunchTime() {
        return lunchTime;
    }

    public void setLunchTime(String lunchTime) {
        this.lunchTime = lunchTime;
    }

    public String getDinnerTime() {
        return dinnerTime;
    }

    public void setDinnerTime(String dinnerTime) {
        this.dinnerTime = dinnerTime;
    }

    public String getDietChange() {
        return dietChange;
    }

    public void setDietChange(String dietChange) {
        this.dietChange = dietChange;
    }

    public String getWakingUpTime() {
        return wakingUpTime;
    }

    public void setWakingUpTime(String gettingUpTime) {
        this.wakingUpTime = gettingUpTime;
    }

    public String getSleepingTime() {
        return sleepingTime;
    }

    public void setSleepingTime(String sleepingTime) {
        this.sleepingTime = sleepingTime;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getWorkingDayTrips() {
        return workingDayTrips;
    }

    public void setWorkingDayTrips(String workingDayTrips) {
        this.workingDayTrips = workingDayTrips;
    }

    public String getWorkingDayTransport() {
        return workingDayTransport;
    }

    public void setWorkingDayTransport(String workingDayTransport) {
        this.workingDayTransport = workingDayTransport;
    }

    public String getWeekendDayTrips() {
        return weekendDayTrips;
    }

    public void setWeekendDayTrips(String weekendDayTrips) {
        this.weekendDayTrips = weekendDayTrips;
    }

    public String getWeekendDayTransport() {
        return weekendDayTransport;
    }

    public void setWeekendDayTransport(String weekendDayTransport) {
        this.weekendDayTransport = weekendDayTransport;
    }

    public String getTakingShowerPerWeek() {
        return takingShowerPerWeek;
    }

    public void setTakingShowerPerWeek(String takingShowerPerWeek) {
        this.takingShowerPerWeek = takingShowerPerWeek;
    }

    public String getShowerTime() {
        return showerTime;
    }

    public void setShowerTime(String showerTime) {
        this.showerTime = showerTime;
    }

    public String getTakingBathPerWeek() {
        return takingBathPerWeek;
    }

    public void setTakingBathPerWeek(String takingBathPerWeek) {
        this.takingBathPerWeek = takingBathPerWeek;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
/*
    public User(String firstNameFromDB, String lastNameFromDB, String typeFromDB, String usernameFromDB, String telnumFromDB, String addressFromDB) {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public User(){

    }

    public User(String userFirstName, String userLastName, String userType,
                String username, String userTelNum, String userAddress, String userPassword)
    {
        this.userID = Long.toString(System.currentTimeMillis()/1000);
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userType = userType;
        this.username = username;
        this.userTelNum = userTelNum;
        this.userAddress = userAddress;
        this.userPassword = userPassword;

    }
    public User(String userID, String userFirstName, String userLastName, String userType,
                String username, String userTelNum, String userAddress, String userPassword)
    {
        this.userID = userID;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userType = userType;
        this.username = username;
        this.userTelNum = userTelNum;
        this.userAddress = userAddress;
        this.userPassword = userPassword;

    }
    */


}