package com.mercure.app;

import java.io.Serializable;
import java.time.LocalDateTime;
import com.google.gson.annotations.SerializedName;

public class Trajet implements Serializable {

    @SerializedName("vitesseMax")
    String vitesseMax;
    @SerializedName("vitesseMoyenne")
    String vitesseMoy;
    @SerializedName("angleMaxY")
    String angleYmax;
    @SerializedName("angleMaxX")
    String angleXmax;
    @SerializedName("distance")
    String distance;
    @SerializedName("temps")
    String temps;
    @SerializedName("dateTrajet")
    String dateTime;
    @SerializedName("obstacles")
    String obstacles;

    public Trajet(String vitesseMax, String vitesseMoy, String angleYmax, String angleXmax, String distance, String temps, String dateTime, String obstacles) {
        this.vitesseMax = vitesseMax;
        this.vitesseMoy = vitesseMoy;
        this.angleYmax = angleYmax;
        this.angleXmax = angleXmax;
        this.distance = distance;
        this.temps = temps;
        this.dateTime = dateTime;
        this.obstacles = obstacles;
    }

    public String getVitesseMax() {
        return vitesseMax;
    }

    public void setVitesseMax(String vitesseMax) {
        this.vitesseMax = vitesseMax;
    }

    public String getVitesseMoy() {
        return vitesseMoy;
    }

    public void setVitesseMoy(String vitesseMoy) {
        this.vitesseMoy = vitesseMoy;
    }

    public String getAngleYmax() {
        return angleYmax;
    }

    public void setAngleYmax(String angleYmax) {
        this.angleYmax = angleYmax;
    }

    public String getAngleXmax() {
        return angleXmax;
    }

    public void setAngleXmax(String angleXmax) {
        this.angleXmax = angleXmax;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTemps() {
        return temps;
    }

    public void setTemps(String temps) {
        this.temps = temps;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getObstacles() {
        return obstacles;
    }

    public void setObstacles(String obstacles) {
        this.obstacles = obstacles;
    }
}
