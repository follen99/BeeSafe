package it.ranauro.beesafe.backend.model;

public class MongoLogin {
    private String username;
    private String password;

    public MongoLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public MongoLogin() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
