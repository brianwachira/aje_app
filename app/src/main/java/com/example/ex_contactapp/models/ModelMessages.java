package com.example.ex_contactapp.models;

import com.example.ex_contactapp.data.Entities.Grouplist;
import java.util.List;

public class ModelMessages {
    private List<Grouplist> grouplistToUse;
    private String message;

    public ModelMessages(List<Grouplist> grouplistToUse2, String message2) {
        this.grouplistToUse = grouplistToUse2;
        this.message = message2;
    }

    public List<Grouplist> getGrouplistToUse() {
        return this.grouplistToUse;
    }

    public void setGrouplistToUse(List<Grouplist> grouplistToUse2) {
        this.grouplistToUse = grouplistToUse2;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }
}
