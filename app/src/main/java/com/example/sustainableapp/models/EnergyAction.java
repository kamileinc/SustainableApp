package com.example.sustainableapp.models;

import androidx.annotation.NonNull;

public class EnergyAction extends SustainableAction{
    private String eaID;
    private String date;
    private boolean noWater;
    private boolean shower;
    private String showerTime;
    private boolean bath;
    private int devicesOff;

    public EnergyAction() {
    }

    public EnergyAction(String id, String category, String userID, String dateBegin, String dateEnd, String eaID, String date, boolean noWater, boolean shower, String showerTime, boolean bath, int devicesOff) {
        super(id, category, userID, dateBegin, dateEnd);
        this.eaID = eaID;
        this.date = date;
        this.noWater = noWater;
        this.shower = shower;
        this.showerTime = showerTime;
        this.bath = bath;
        this.devicesOff = devicesOff;
    }

    public EnergyAction(String id, String category, String userID, String dateBegin, String dateEnd, String date, boolean noWater, boolean shower, String showerTime, boolean bath, int devicesOff) {
        super(id, category, userID, dateBegin, dateEnd);
        this.eaID = userID + date;
        this.date = date;
        this.noWater = noWater;
        this.shower = shower;
        this.showerTime = showerTime;
        this.bath = bath;
        this.devicesOff = devicesOff;
    }

    @NonNull
    @Override
    public String toString() {
        return "EnergyAction{" +
                "eaID='" + eaID + '\'' +
                ", date='" + date + '\'' +
                ", noWater='" + noWater + '\'' +
                ", shower=" + shower +
                ", showerTime='" + showerTime + '\'' +
                ", bath=" + bath +
                ", devicesOff='" + devicesOff + '\'' +
                '}';
    }
    public boolean isNoWater() {
        return noWater;
    }
    public String getEaID() {
        return eaID;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public boolean isShower() {
        return shower;
    }
    public String getShowerTime() {
        return showerTime;
    }
    public boolean isBath() {
        return bath;
    }
    public int getDevicesOff() {
        return devicesOff;
    }
}
