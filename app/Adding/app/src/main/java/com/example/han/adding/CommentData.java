package com.example.han.adding;

public class CommentData {
    String member;
    String comment;
    float score;

    public CommentData(String member, String comment, float score) {
        this.member = member;
        this.comment = comment;
        this.score = score;
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

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
