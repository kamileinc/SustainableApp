package com.example.sustainableapp.models;

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

    public void setTaID(String taID) {
        this.taID = taID;
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

    public void setNoTravelling(boolean noTravelling) {
        this.noTravelling = noTravelling;
    }

    public boolean isWalking() {
        return walking;
    }

    public void setWalking(boolean walking) {
        this.walking = walking;
    }


    public boolean isBicycle() {
        return bicycle;
    }

    public void setBicycle(boolean bicycle) {
        this.bicycle = bicycle;
    }


    public boolean isPublicTransport() {
        return publicTransport;
    }

    public void setPublicTransport(boolean publicTransport) {
        this.publicTransport = publicTransport;
    }

    public boolean isCar() {
        return car;
    }

    public void setCar(boolean car) {
        this.car = car;
    }

    public int getWalkingKM() {
        return walkingKM;
    }

    public void setWalkingKM(int walkingKM) {
        this.walkingKM = walkingKM;
    }

    public int getBicycleKM() {
        return bicycleKM;
    }

    public void setBicycleKM(int bicycleKM) {
        this.bicycleKM = bicycleKM;
    }

    public int getPublicTransportKM() {
        return publicTransportKM;
    }

    public void setPublicTransportKM(int publicTransportKM) {
        this.publicTransportKM = publicTransportKM;
    }

    public int getCarKM() {
        return carKM;
    }

    public void setCarKM(int carKM) {
        this.carKM = carKM;
    }

    public int getCarPassengersKM() {
        return carPassengersKM;
    }

    public void setCarPassengersKM(int carPassengersKM) {
        this.carPassengersKM = carPassengersKM;
    }

    public int getCarPassengers() {
        return carPassengers;
    }

    public void setCarPassengers(int carPassengers) {
        this.carPassengers = carPassengers;
    }
}
