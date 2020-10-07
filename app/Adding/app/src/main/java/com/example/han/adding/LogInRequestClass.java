package com.example.han.adding;

public class LogInRequestClass {
    private String email;
    private String pw;

    public LogInRequestClass(String email, String pw) {
        this.email = email;
        this.pw = pw;
    }

    public LogInRequestClass() {
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
}
