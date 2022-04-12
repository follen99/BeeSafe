package com.example.beesafe.model;

public class Image {
    private long name;
    private String imageUrl;

    public Image(long name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public long getName() {
        return name;
    }

    public void setName(long name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
