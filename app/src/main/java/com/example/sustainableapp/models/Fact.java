package com.example.sustainableapp.models;

public class Fact {
    private String id;
    private String category;
    private String text;

    public Fact() {
    }

    public Fact(String id, String category, String text) {
        this.id = id;
        this.category = category;
        this.text = text;
    }

    public Fact(String category, String text) {
        this.id = Long.toString(System.currentTimeMillis()/1000);
        this.category = category;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Fact{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", text='" + text + '\'' +
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
