package com.litosh.ilya.handtrainer.db.models;

import io.realm.RealmObject;

/**
 * Created by ilya_ on 17.03.2018.
 */

public class Note extends RealmObject {

    private long id;

    private String task;

    private String comment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
