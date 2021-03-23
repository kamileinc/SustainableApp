package com.example.sustainableapp.models;

import android.graphics.Bitmap;

public class Points {
    private String userID;
    private String userName;
    private String photo;
    private double faPoints;
    private double taPoints;
    private double eaPoints;
    private Bitmap bitmap;

    public Points(String userID, String userName, String photo) {
        this.userID = userID;
        this.userName = userName;
        this.photo = photo;
        this.faPoints = -1;
        this.taPoints = -1;
        this.eaPoints = -1;
        this.bitmap = null;
    }

    @Override
    public String toString() {
        return "Points{" +
                "userID='" + userID + '\'' +
                ", userName='" + userName + '\'' +
                ", photo='" + photo + '\'' +
                ", faPoints=" + faPoints +
                ", taPoints=" + taPoints +
                ", eaPoints=" + eaPoints +
                ", bitmap=" + bitmap +
                '}';
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public double getFaPoints() {
        return faPoints;
    }

    public void setFaPoints(double faPoints) {
        this.faPoints = faPoints;
    }

    public double getTaPoints() {
        return taPoints;
    }

    public void setTaPoints(double taPoints) {
        this.taPoints = taPoints;
    }

    public double getEaPoints() {
        return eaPoints;
    }

    public void setEaPoints(double eaPoints) {
        this.eaPoints = eaPoints;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
