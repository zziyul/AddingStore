package com.example.han.adding;

public class ProjectDetailResponseClass {
    int pnum;
    String admin;
    String pname;
    int state;
    String category;
    String deadline;
    int fundgoal;
    String image;
    String introduction;
    float score;
    int current;

    public ProjectDetailResponseClass(int pnum, String admin, String pname, int state, String category, String deadline, int fundgoal, String image, String introduction, float score, int current) {
        this.pnum = pnum;
        this.admin = admin;
        this.pname = pname;
        this.state = state;
        this.category = category;
        this.deadline = deadline;
        this.fundgoal = fundgoal;
        this.image = image;
        this.introduction = introduction;
        this.score = score;
        this.current = current;
    }

    public int getPnum() {
        return pnum;
    }

    public void setPnum(int pnum) {
        this.pnum = pnum;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public int getFundgoal() {
        return fundgoal;
    }

    public void setFundgoal(int fundgoal) {
        this.fundgoal = fundgoal;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
}
