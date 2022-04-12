package com.example.beesafeloginv2.model;

public class User {
    private String email;
    private String role;
    private String password;
    private String fullName;
    private String age;

    public User(String email, String role, String password, String fullName, String age) {
        this.email = email;
        this.role = role;
        this.password = password;
        this.fullName = fullName;
        this.age = age;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
