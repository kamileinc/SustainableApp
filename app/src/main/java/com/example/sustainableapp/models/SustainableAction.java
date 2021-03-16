package com.example.sustainableapp.models;

public class SustainableAction {
    private String id;
    private String category;
    private String userID;
    private String dateBegin;
    private String dateEnd;

    public SustainableAction() {
    }

    public SustainableAction(String id, String category, String userID, String dateBegin, String dateEnd) {
        this.id = id;
        this.category = category;
        this.userID = userID;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
    }

    public SustainableAction(String category, String userID, String dateBegin, String dateEnd) {
        this.id = Long.toString(System.currentTimeMillis()/1000);
        this.category = category;
        this.userID = userID;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
    }

    @Override
    public String toString() {
        return "SustainableAction{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", userID='" + userID + '\'' +
                ", dateBegin='" + dateBegin + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin) {
        this.dateBegin = dateBegin;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }
}
