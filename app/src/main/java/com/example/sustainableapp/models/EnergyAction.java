package com.example.sustainableapp.models;

public class EnergyAction extends SustainableAction{
    private String id;
    private String date;
    private boolean shower;
    private String showerTime;
    private boolean bath;
    private String devicesOff;

    public EnergyAction(String id, String category, String userID, String dateBegin, String dateEnd, String id1, String date, boolean shower, String showerTime, boolean bath, String devicesOff) {
        super(id, category, userID, dateBegin, dateEnd);
        this.id = id1;
        this.date = date;
        this.shower = shower;
        this.showerTime = showerTime;
        this.bath = bath;
        this.devicesOff = devicesOff;
    }

    public EnergyAction(String id, String category, String userID, String dateBegin, String dateEnd, String date, boolean shower, String showerTime, boolean bath, String devicesOff) {
        super(id, category, userID, dateBegin, dateEnd);
        this.id = Long.toString(System.currentTimeMillis()/1000);
        this.date = date;
        this.shower = shower;
        this.showerTime = showerTime;
        this.bath = bath;
        this.devicesOff = devicesOff;
    }

    @Override
    public String toString() {
        return "EnergyAction{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", shower=" + shower +
                ", showerTime='" + showerTime + '\'' +
                ", bath=" + bath +
                ", devicesOff='" + devicesOff + '\'' +
                '}';
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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

    public String getDevicesOff() {
        return devicesOff;
    }

    public void setDevicesOff(String devicesOff) {
        this.devicesOff = devicesOff;
    }
}
