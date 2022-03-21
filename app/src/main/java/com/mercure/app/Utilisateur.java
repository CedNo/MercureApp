package com.mercure.app;

import com.google.gson.annotations.SerializedName;

public class Utilisateur {

    @SerializedName("id")
    String id;

    @SerializedName("username")
    String username;

    @SerializedName("password")
    String password;

    public Utilisateur(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
