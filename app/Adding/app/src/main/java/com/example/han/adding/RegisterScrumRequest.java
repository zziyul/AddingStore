package com.example.han.adding;

public class RegisterScrumRequest {
    int snum;
    int project;
    String sname;
    int state;
    String image;
    String introduction;
    String file;

    public RegisterScrumRequest(int snum, int project, String sname, int state, String image, String introduction, String file) {
        this.snum = snum;
        this.project = project;
        this.sname = sname;
        this.state = state;
        this.image = image;
        this.introduction = introduction;
        this.file = file;
    }

    public int getSnum() {
        return snum;
    }

    public void setSnum(int snum) {
        this.snum = snum;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
