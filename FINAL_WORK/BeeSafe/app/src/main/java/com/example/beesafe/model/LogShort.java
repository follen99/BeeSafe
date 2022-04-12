package com.example.beesafe.model;

@Deprecated
public class LogShort {
    private String fromUser;
    private String date;

    public LogShort(String fromUser, String date) {
        this.fromUser = fromUser;
        this.date = date;
    }

    @Override
    public String toString() {
        return "LogShort{" +
                "fromUser='" + fromUser + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
