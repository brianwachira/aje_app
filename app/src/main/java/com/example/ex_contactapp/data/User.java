package com.example.ex_contactapp.data;

public class User {

    private int id;

    private String givenName;
    private String userId;

    public User(int id, String givenName, String userId) {
        this.id = id;
        this.givenName = givenName;
        this.userId = userId;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getGivenId() {
        return userId;
    }

    public void setGivenId(String userId) {
        this.userId = userId;
    }
}
