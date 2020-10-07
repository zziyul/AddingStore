package com.example.han.adding;

public class PreviewClassFund {
    int pnum;
    private String pname;
    private String image;

    public PreviewClassFund(int pnum, String pname, String image) {
        this.pnum = pnum;
        this.pname = pname;
        this.image = image;
    }

    public PreviewClassFund() {
    }

    public void setPnum(int pnum) {
        this.pnum = pnum;
    }

    public int getPnum() {
        return pnum;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
