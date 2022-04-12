package com.example.beesafe.model;

public class ReportBuilder {
    private int id;
    private String placeName;
    private String description;
    private String kindOfProblem;
    private int gravity;
    private int urgency;
    private String imagePath;
    private String dateTime;
    private long phoneNumber;
    private double longitude;
    private double latitude;
    private String fromUser;

    public ReportBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public ReportBuilder setPlaceName(String placeName) {
        this.placeName = placeName;
        return this;
    }

    public ReportBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public ReportBuilder setKindOfProblem(String kindOfProblem) {
        this.kindOfProblem = kindOfProblem;
        return this;
    }

    public ReportBuilder setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public ReportBuilder setUrgency(int urgency) {
        this.urgency = urgency;
        return this;
    }

    public ReportBuilder setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public ReportBuilder setDateTime(String dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public ReportBuilder setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public ReportBuilder setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public ReportBuilder setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public ReportBuilder setFromUser(String fromUser) {
        this.fromUser = fromUser;
        return this;
    }

    public Report createReport() {
        return new Report(id, placeName, description, kindOfProblem, gravity, urgency, imagePath, dateTime, phoneNumber, longitude, latitude, fromUser);
    }
}