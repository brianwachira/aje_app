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


    private String middleName;

    @NonNull
    private String phoneNumber;

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


    public Grouplist(String firstName, String lastName, String middleName, @NonNull String phoneNumber,@NonNull int groupid) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.phoneNumber = phoneNumber;
        this.groupid = groupid;
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

    @NonNull
    public Integer getGroupid() {
        return groupid;
    }
    public void setContactid(@NonNull Integer contactid) {
        this.contactid = contactid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

}
