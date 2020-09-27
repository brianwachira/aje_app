package com.example.ex_contactapp.models;

public class ModelMessageParameters {

    Integer position;
    String nameParameter;

    public ModelMessageParameters(Integer position, String nameParameter) {
        this.position = position;
        this.nameParameter = nameParameter;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getNameParameter() {
        return nameParameter;
    }

    public void setNameParameter(String nameParameter) {
        this.nameParameter = nameParameter;
    }
}
