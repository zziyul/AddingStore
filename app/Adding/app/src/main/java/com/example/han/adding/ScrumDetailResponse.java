package com.example.han.adding;

public class ScrumDetailResponse {
    String sname;
    String introduction;
    String file;

    public ScrumDetailResponse(String sname, String introduction, String file) {
        this.sname = sname;
        this.introduction = introduction;
        this.file = file;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
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
