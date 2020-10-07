package com.example.han.adding;

public class CastRequestClass {
    private int start;
    private int number;

    CastRequestClass(int start, int number) {
        this.start = start;
        this.number = number;
    }

    CastRequestClass() {

    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
