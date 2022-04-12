package com.example.emergencyui_v1;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "report")
public class Report {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "place_name")
    private String placeName;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "kind_of_problem")
    private String kindOfProblem;

    @ColumnInfo(name = "gravity")
    private int gravity;

    @ColumnInfo(name = "urgency")
    private int urgency;

    @ColumnInfo(name = "image_path")
    private String imagePath;

    @ColumnInfo(name = "date_time")
    private String dateTime;

    @ColumnInfo(name = "phone_number")
    private long phoneNumber;


    @ColumnInfo(name = "longitude")
    private double longitude;
    @ColumnInfo(name = "latitude")
    private double latitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKindOfProblem() {
        return kindOfProblem;
    }

    public void setKindOfProblem(String kindOfProblem) {
        this.kindOfProblem = kindOfProblem;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", placeName='" + placeName + '\'' +
                ", description='" + description + '\'' +
                ", kindOfProblem='" + kindOfProblem + '\'' +
                ", gravity=" + gravity +
                ", urgency=" + urgency +
                ", imagePath='" + imagePath + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
