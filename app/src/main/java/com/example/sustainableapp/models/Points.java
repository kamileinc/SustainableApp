package com.example.sustainableapp.models;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.util.Comparator;

public class Points {
    private String userID;
    private final String userName;
    private final String photo;
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

    @NonNull
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

    public static class SortByFA implements Comparator<Points> {
        public int compare(Points points1, Points points2) {
            double pointsFA1 = points1.getFaPoints();
            double pointsFA2 = points2.getFaPoints();
            return Double.compare(pointsFA2, pointsFA1);
        }
    }

    public static class SortByEA implements Comparator<Points> {
        public int compare(Points points1, Points points2) {
            double pointsEA1 = points1.getEaPoints();
            double pointsEA2 = points2.getEaPoints();
            return Double.compare(pointsEA2, pointsEA1);
        }
    }
    public static class SortByTA implements Comparator<Points> {
        public int compare(Points points1, Points points2) {
            double pointsTA1 = points1.getTaPoints();
            double pointsTA2 = points2.getTaPoints();
            return Double.compare(pointsTA2, pointsTA1);
        }
    }
    public static class SortByTotal implements Comparator<Points> {
        public int compare(Points points1, Points points2) {
            double pointsTA1 = points1.getTaPoints() + points1.getEaPoints() + points1.getFaPoints();
            double pointsTA2 = points2.getTaPoints() + points2.getEaPoints() + points2.getFaPoints();
            return Double.compare(pointsTA2, pointsTA1);
        }
    }
}
