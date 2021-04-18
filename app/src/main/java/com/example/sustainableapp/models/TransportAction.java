package com.example.sustainableapp.models;

import androidx.annotation.NonNull;

public class TransportAction extends SustainableAction{
    private String taID;
    private String date;
    private boolean noTravelling;
    private boolean walking;
    private int walkingKM;
    private boolean bicycle;
    private int bicycleKM;
    private boolean publicTransport;
    private int publicTransportKM;
    private boolean car;
    private int carKM;
    private int carPassengersKM;
    private int carPassengers;

    public TransportAction() {
    }

    public TransportAction(String id, String category, String userID, String dateBegin, String dateEnd, String taID, String date, boolean noTravelling, boolean walking, int walkingKM, boolean bicycle, int bicycleKM, boolean publicTransport, int publicTransportKM, boolean car, int carKM, int carPassengersKM, int carPassengers) {
        super(id, category, userID, dateBegin, dateEnd);
        this.taID = taID;
        this.date = date;
        this.noTravelling = noTravelling;
        this.walking = walking;
        this.walkingKM = walkingKM;
        this.bicycle = bicycle;
        this.bicycleKM = bicycleKM;
        this.publicTransport = publicTransport;
        this.publicTransportKM = publicTransportKM;
        this.car = car;
        this.carKM = carKM;
        this.carPassengersKM = carPassengersKM;
        this.carPassengers = carPassengers;
    }

    public TransportAction(String id, String category, String userID, String dateBegin, String dateEnd, String date, boolean noTravelling, boolean walking, int walkingKM, boolean bicycle, int bicycleKM, boolean publicTransport, int publicTransportKM, boolean car, int carKM, int carPassengersKM, int getCarPassengers) {
        super(id, category, userID, dateBegin, dateEnd);
        this.taID = userID + date;
        this.date = date;
        this.noTravelling = noTravelling;
        this.walking = walking;
        this.walkingKM = walkingKM;
        this.bicycle = bicycle;
        this.bicycleKM = bicycleKM;
        this.publicTransport = publicTransport;
        this.publicTransportKM = publicTransportKM;
        this.car = car;
        this.carKM = carKM;
        this.carPassengersKM = carPassengersKM;
        this.carPassengers = getCarPassengers;
    }

    @NonNull
    @Override
    public String toString() {
        return "TransportAction{" +
                "taID='" + taID + '\'' +
                ", date='" + date + '\'' +
                ", noTravelling=" + noTravelling +
                ", walking=" + walking +
                ", walkingKM='" + walkingKM + '\'' +
                ", bicycle=" + bicycle +
                ", bicycleKM='" + bicycleKM + '\'' +
                ", publicTransport=" + publicTransport +
                ", publicTransportKM='" + publicTransportKM + '\'' +
                ", car=" + car +
                ", carKM='" + carKM + '\'' +
                ", carPassengersKM='" + carPassengersKM + '\'' +
                ", carPassengers='" + carPassengers + '\'' +
                '}';
    }
    public String getTaID() {
        return taID;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public boolean isNoTravelling() {
        return noTravelling;
    }
    public boolean isWalking() {
        return walking;
    }
    public boolean isBicycle() {
        return bicycle;
    }
    public boolean isPublicTransport() {
        return publicTransport;
    }
    public boolean isCar() {
        return car;
    }
    public int getWalkingKM() {
        return walkingKM;
    }
    public int getBicycleKM() {
        return bicycleKM;
    }
    public int getPublicTransportKM() {
        return publicTransportKM;
    }
    public int getCarKM() {
        return carKM;
    }
    public int getCarPassengersKM() {
        return carPassengersKM;
    }
    public int getCarPassengers() {
        return carPassengers;
    }
}
