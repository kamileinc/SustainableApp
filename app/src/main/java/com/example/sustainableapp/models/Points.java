package com.example.sustainableapp.models;

import android.graphics.Bitmap;

import java.util.Comparator;

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


    public double compareTo(Points comparePoi) {
        double comparePoints=((Points)comparePoi).getFaPoints();
        /* For Ascending order*/
        return this.faPoints-comparePoints;

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }
/*
    @Override
    public int compareTo(Object o) {
        double comparePoints=((Points)o).getFaPoints();
        //For Ascending order
        return Double.compare(this.faPoints, comparePoints);
    }
    */
    /*
    public static Comparator<Points> PointsFAComparator = new Comparator<Points>() {

        public int compare(Points points1, Points points2) {

            double pointsFA1 = points1.getFaPoints();
            double pointsFA2 = points2.getFaPoints();

            //ascending order
            return Double.compare(pointsFA1, pointsFA2);
            //return fruitName1.compareTo(fruitName2);

            //descending order
            //return fruitName2.compareTo(fruitName1);
        }

    };
    */

    public static class SortByFA implements Comparator<Points> {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Points points1, Points points2) {

            double pointsFA1 = points1.getFaPoints();
            double pointsFA2 = points2.getFaPoints();

            //ascending order
            return Double.compare(pointsFA2, pointsFA1);
            //return fruitName1.compareTo(fruitName2);

            //descending order
            //return fruitName2.compareTo(fruitName1);
        }
    }

    public static class SortByEA implements Comparator<Points> {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Points points1, Points points2) {

            double pointsEA1 = points1.getEaPoints();
            double pointsEA2 = points2.getEaPoints();

            //ascending order
            return Double.compare(pointsEA2, pointsEA1);
            //return fruitName1.compareTo(fruitName2);

            //descending order
            //return fruitName2.compareTo(fruitName1);
        }
    }
    public static class SortByTA implements Comparator<Points> {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Points points1, Points points2) {

            double pointsTA1 = points1.getTaPoints();
            double pointsTA2 = points2.getTaPoints();

            //ascending order
            return Double.compare(pointsTA2, pointsTA1);
            //return fruitName1.compareTo(fruitName2);

            //descending order
            //return fruitName2.compareTo(fruitName1);
        }
    }
    public static class SortByTotal implements Comparator<Points> {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Points points1, Points points2) {

            double pointsTA1 = points1.getTaPoints() + points1.getEaPoints() + points1.getFaPoints();
            double pointsTA2 = points2.getTaPoints() + points2.getEaPoints() + points2.getFaPoints();

            //ascending order
            return Double.compare(pointsTA2, pointsTA1);
            //return fruitName1.compareTo(fruitName2);

            //descending order
            //return fruitName2.compareTo(fruitName1);
        }
    }
}
