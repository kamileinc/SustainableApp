package com.example.sustainableapp.models;

public class TransportAction extends SustainableAction{
    private String taID;
    private String date;
    private boolean noTravelling;
    private boolean walking;
    private String walkingKM;
    private boolean bicycle;
    private String bicycleKM;
    private boolean publicTransport;
    private String publicTransportKM;
    private boolean car;
    private String carKM;
    private String carPassengersKM;
    private String carPassengers;

    public TransportAction() {
    }

    public TransportAction(String id, String category, String userID, String dateBegin, String dateEnd, String taID, String date, boolean noTravelling, boolean walking, String walkingKM, boolean bicycle, String bicycleKM, boolean publicTransport, String publicTransportKM, boolean car, String carKM, String carPassengersKM, String carPassengers) {
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

    public TransportAction(String id, String category, String userID, String dateBegin, String dateEnd, String date, boolean noTravelling, boolean walking, String walkingKM, boolean bicycle, String bicycleKM, boolean publicTransport, String publicTransportKM, boolean car, String carKM, String carPassengersKM, String getCarPassengers) {
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

    public String getWalkingKM() {
        return walkingKM;
    }

    public void setWalkingKM(String walkingKM) {
        this.walkingKM = walkingKM;
    }

    public boolean isBicycle() {
        return bicycle;
    }

    public void setBicycle(boolean bicycle) {
        this.bicycle = bicycle;
    }

    public String getBicycleKM() {
        return bicycleKM;
    }

    public void setBicycleKM(String bicycleKM) {
        this.bicycleKM = bicycleKM;
    }

    public boolean isPublicTransport() {
        return publicTransport;
    }

    public void setPublicTransport(boolean publicTransport) {
        this.publicTransport = publicTransport;
    }

    public String getPublicTransportKM() {
        return publicTransportKM;
    }

    public void setPublicTransportKM(String publicTransportKM) {
        this.publicTransportKM = publicTransportKM;
    }

    public boolean isCar() {
        return car;
    }

    public void setCar(boolean car) {
        this.car = car;
    }

    public String getCarKM() {
        return carKM;
    }

    public void setCarKM(String carKM) {
        this.carKM = carKM;
    }

    public String getCarPassengersKM() {
        return carPassengersKM;
    }

    public void setCarPassengersKM(String carPassengersKM) {
        this.carPassengersKM = carPassengersKM;
    }

    public String getCarPassengers() {
        return carPassengers;
    }

    public void setCarPassengers(String carPassengers) {
        this.carPassengers = carPassengers;
    }
}
