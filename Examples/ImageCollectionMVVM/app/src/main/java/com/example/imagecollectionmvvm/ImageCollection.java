package com.example.imagecollectionmvvm;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "image_collection")
public class ImageCollection implements Parcelable {
    // keet it simple

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "image_path")
    private String imagePath;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "priority")
    private int priority;


    public ImageCollection(String name, String imagePath, String description, int priority) {
        this.name = name;
        this.imagePath = imagePath;
        this.description = description;
        this.priority = priority;
    }

    public ImageCollection() {
    }

    protected ImageCollection(Parcel in) {
        id = in.readInt();
        name = in.readString();
        imagePath = in.readString();
        description = in.readString();
        priority = in.readInt();
    }



    public void setId(int id) {
        this.id = id;
    }
    public int getId(){
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


    // metodi usati per passare l'oggetto tra activities

    public static final Creator<ImageCollection> CREATOR = new Creator<ImageCollection>() {
        @Override
        public ImageCollection createFromParcel(Parcel in) {
            return new ImageCollection(in);
        }

        @Override
        public ImageCollection[] newArray(int size) {
            return new ImageCollection[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(imagePath);
        parcel.writeString(description);
        parcel.writeInt(priority);
    }
}
