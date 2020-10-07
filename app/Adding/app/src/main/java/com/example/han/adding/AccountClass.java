package com.example.han.adding;

public class AccountClass {
    private String email;
    private String pw;
    private String name;
    private int age;
    private int gender;
    private String job;
    private String interested;


    public AccountClass(String email, String pw, String name, int age, int gender, String job, String interested) {
        this.email = email;
        this.pw = pw;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.job = job;
        this.interested = interested;
    }

    public AccountClass() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

}
