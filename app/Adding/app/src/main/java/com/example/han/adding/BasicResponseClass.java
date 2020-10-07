package com.example.han.adding;

public class BasicResponseClass {
    private boolean state;
    private String str;

    public BasicResponseClass(boolean state, String str) {
        this.state = state;
        this.str = str;
    }

    public BasicResponseClass() {
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
