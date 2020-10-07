package com.example.han.adding;

public class PreviewFundProjClass {
    int pnum;
    private String pname;
    private String category;
    private String deadline;
    private int current;
    private String image;


    public PreviewFundProjClass(int pnum, String pname, String category, String deadline, int current, String image) {
        this.pnum = pnum;
        this.pname = pname;
        this.category = category;
        this.deadline = deadline;
        this.current = current;
        this.image = image;
    }

    public int getPnum() {
        return pnum;
    }

    public void setPnum(int pnum) {
        this.pnum = pnum;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
