package com.example.sustainableapp.models;


public class IntVariable {
    private int number = 0;
    private String id = "";
    private IntVariable.ChangeListener listener;
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
        if (listener != null) listener.onChange();
    }
    public String getID() {
        return id;
    }
    public void setID(String id) {
        this.id = id;
        if (listener != null) listener.onChange();
    }
    public IntVariable.ChangeListener getListener() {
        return listener;
    }
    public void setListener(IntVariable.ChangeListener listener) {
        this.listener = listener;
    }
    public interface ChangeListener {
        void onChange();
    }
}
