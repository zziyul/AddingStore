package com.example.han.adding;

class UserInfo {
    String email;
    String pw;
    String name;
    int age;
    int gender;
    String job;
    String favorite;

    public UserInfo() {
        //
    }

    public UserInfo(String email, String pw, String name, int age, int gender, String job, String favorite) {
        this.email = email;
        this.pw = pw;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.job = job;
        this.favorite = favorite;
    }


    public String getEmail() {
        return email;
    }

    public String getPw() {
        return pw;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getGender() {
        return gender;
    }

    public String getJob() {
        return job;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }
}
