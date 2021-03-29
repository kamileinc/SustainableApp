package com.example.sustainableapp.models;

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
       //this.id = Long.toString(System.currentTimeMillis()/1000);
        this.eaID = userID + date;
        this.date = date;
        this.noWater = noWater;
        this.shower = shower;
        this.showerTime = showerTime;
        this.bath = bath;
        this.devicesOff = devicesOff;
    }

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

    public void setNoWater(boolean noWater) {
        this.noWater = noWater;
    }

    public String getEaID() {
        return eaID;
    }

    public void setEaID(String id) {
        this.eaID = eaID;
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

    public void setShower(boolean shower) {
        this.shower = shower;
    }

    public String getShowerTime() {
        return showerTime;
    }

    public void setShowerTime(String showerTime) {
        this.showerTime = showerTime;
    }

    public boolean isBath() {
        return bath;
    }

    public void setBath(boolean bath) {
        this.bath = bath;
    }

    public int getDevicesOff() {
        return devicesOff;
    }

    public void setDevicesOff(int devicesOff) {
        this.devicesOff = devicesOff;
    }
}
