package com.example.uploadimagestofirebasedb;

public class Image {
    private String name;
    private String imageUrl;
    // l'email dell'utente che l'ha caricata
    @Deprecated
    private String fromUser;

    public Image(){ }

    public Image(String name, String imageUrl, String fromUser){
        if (name.trim().isEmpty())
            this.name = "Nessun nome";
        else this.name = name;

        this.imageUrl = imageUrl;

        this.fromUser = fromUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }
}
