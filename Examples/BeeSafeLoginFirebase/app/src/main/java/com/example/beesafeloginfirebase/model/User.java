package com.example.beesafeloginfirebase.model;

public class User {
    private String name;
    private String age;
    private String email;

    public User(){

    }

    public User(String name, String age, String email){
        this.age = age;
        this.name = name;
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
