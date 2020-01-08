package com.example.ex_contactapp.models;

public class ModelContacts {

    private String id,name, number;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ModelContacts(String id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
