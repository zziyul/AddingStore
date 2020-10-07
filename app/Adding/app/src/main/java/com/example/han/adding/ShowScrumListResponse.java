package com.example.han.adding;

public class ShowScrumListResponse {
    int snum;
    String sname;
    Integer current;
    int state;
    String image;

    public ShowScrumListResponse(int snum, String sname, Integer current, int state, String image) {
        this.snum = snum;
        this.sname = sname;
        this.current = current;
        this.state = state;
        this.image = image;
    }

    public int getSnum() {
        return snum;
    }

    public void setSnum(int snum) {
        this.snum = snum;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

