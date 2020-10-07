package com.example.han.adding;

public class CommentAnalData {
    String member;
    String job;
    String favorite;
    String gender;
    int age;

    public CommentAnalData(String member, String job, String favorite, String gender, int age) {
        this.member = member;
        this.job = job;
        this.favorite = favorite;
        this.gender = gender;
        this.age = age;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
