package com.example.han.adding;

public class ScrumErrorSendRequest {
    int snum;
    String member;
    String comment;

    public ScrumErrorSendRequest(int snum, String member, String comment) {
        this.snum = snum;
        this.member = member;
        this.comment = comment;
    }

    public int getSnum() {
        return snum;
    }

    public void setSnum(int snum) {
        this.snum = snum;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
