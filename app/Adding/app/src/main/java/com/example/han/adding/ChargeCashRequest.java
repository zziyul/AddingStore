package com.example.han.adding;

public class ChargeCashRequest {
    String member;
    int moneychange;
    int reason;
    int project;

    public ChargeCashRequest(String member, int moneychange, int reason, int project) {
        this.member = member;
        this.moneychange = moneychange;
        this.reason = reason;
        this.project = project;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public int getMoneychange() {
        return moneychange;
    }

    public void setMoneychange(int moneychange) {
        this.moneychange = moneychange;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }
}
