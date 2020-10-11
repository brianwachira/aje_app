package com.example.ex_contactapp.data.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;
import static com.example.ex_contactapp.data.Entities.Message.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class Message {
    public static final String TABLE_NAME = "message";

    public void setMessageid(Integer messageid) {
        this.messageid = messageid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    @PrimaryKey(autoGenerate = true)
    private Integer messageid;

    @NonNull
    private String messageContent;


    public Message(){}

    /**
     * @param messageContent
     */

    @Ignore
    public Message(@NonNull String messageContent,Integer groupid,Integer remotegroupid) {
        this.messageContent = messageContent;
        this.groupid = groupid;
        this.remotegroupid = remotegroupid;
    }


    @ForeignKey(
            entity = ContactGroup.class,
            parentColumns = "id",
            childColumns = "groupid",
            onDelete = CASCADE
    )
    private Integer groupid;

    public Integer getRemotegroupid() {
        return remotegroupid;
    }

    public void setRemotegroupid(Integer remotegroupid) {
        this.remotegroupid = remotegroupid;
    }

    private Integer remotegroupid;

    public Integer getMessageid() {
        return messageid;
    }

    public Integer getGroupid() {
        return groupid;
    }

    @NonNull
    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(@NonNull String messageContent) {
        this.messageContent = messageContent;
    }




}
