package com.example.ex_contactapp.data.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;
import static com.example.ex_contactapp.data.Entities.Grouplist.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class Grouplist {
    public static final String TABLE_NAME = "grouplist";


    @PrimaryKey(autoGenerate = true)
    private Integer contactid;

    @NonNull
    private String firstName;


    private String lastName;

    @NonNull
    private String phoneNumber;


    private String middleName;

    @NonNull
    private Integer groupid;


    public Grouplist(){

    }

    /**
     * @param lastName
     * @param firstName
     * @param phoneNumber
     * @param middleName
     */


    public Grouplist(String firstName, String lastName, @NonNull String phoneNumber, String middleName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.middleName = middleName;
    }

    @ForeignKey(
            entity = ContactGroup.class,
            parentColumns = "id",
            childColumns = "groupid",
            onDelete = CASCADE
    )



    public Integer getContactid() {
        return contactid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Integer getGroupid() {
        return groupid;
    }
    public void setContactid(Integer contactid) {
        this.contactid = contactid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

}
