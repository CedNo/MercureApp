package com.mercure.app;

import com.google.gson.annotations.SerializedName;

public class Utilisateur {

    @SerializedName("id")
    int id;

    @SerializedName("username")
    String utilisateur;

    @SerializedName("password")
    String mdp;


    public Utilisateur(int id, String utilisateur, String mdp) {
        this.id = id;
        this.utilisateur = utilisateur;
        this.mdp = mdp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
}
