package com.example.sustainableapp.models;

public class BooVariable {
    private boolean boo = false;
    private BooVariable.ChangeListener listener;
    public boolean isBoo() {
        return boo;
    }
    public void setBoo(boolean boo) {
        this.boo = boo;
        if (listener != null) listener.onChange();
    }
    public BooVariable.ChangeListener getListener() {
        return listener;
    }
    public void setListener(BooVariable.ChangeListener listener) {
        this.listener = listener;
    }
    public interface ChangeListener {
        void onChange();
    }
}