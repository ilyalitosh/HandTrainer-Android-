package com.litosh.ilya.handtrainer.db.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ilya_ on 17.03.2018.
 */

public class Person extends RealmObject {

    @PrimaryKey
    private long id;

    private String login;

    private String password;

    private int wholeCountRotations;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWholeCountRotations() {
        return wholeCountRotations;
    }

    public void setWholeCountRotations(int wholeCountRotations) {
        this.wholeCountRotations = wholeCountRotations;
    }
}
