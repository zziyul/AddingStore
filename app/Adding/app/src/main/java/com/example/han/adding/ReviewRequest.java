package com.example.han.adding;

public class ReviewRequest {
    String member;
    int project;
    int score;
    String comment;

    public ReviewRequest(String member, int project, int score, String comment) {
        this.member = member;
        this.project = project;
        this.score = score;
        this.comment = comment;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
