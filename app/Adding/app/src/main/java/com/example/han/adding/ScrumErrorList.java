package com.example.han.adding;

public class ScrumErrorList {
    int sinum;
    int snum;
    int state;
    String sname;
    String comment;

    public ScrumErrorList(int sinum, int snum, int state, String sname, String comment) {
        this.sinum = sinum;
        this.snum = snum;
        this.state = state;
        this.sname = sname;
        this.comment = comment;
    }

    public int getSinum() {
        return sinum;
    }

    public void setSinum(int sinum) {
        this.sinum = sinum;
    }

    public int getSnum() {
        return snum;
    }

    public void setSnum(int snum) {
        this.snum = snum;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
