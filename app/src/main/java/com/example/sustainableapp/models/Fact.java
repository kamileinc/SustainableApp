package com.example.sustainableapp.models;

import androidx.annotation.NonNull;

public class Fact {
    private String id;
    private String category;
    private String text;
    public Fact() {
    }
    @NonNull
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
