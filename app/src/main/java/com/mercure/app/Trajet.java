package com.mercure.app;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDateTime;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "trajets")
public class Trajet implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "ID")
    int id;

    @ColumnInfo(name = "vitesseMax")
    @SerializedName("vitesseMax")
    String vitesseMax;

    @ColumnInfo(name = "vitesseMoyenne")
    @SerializedName("vitesseMoyenne")
    String vitesseMoy;

    @ColumnInfo(name = "angleMaxY")
    @SerializedName("angleMaxY")
    String angleYmax;

    @ColumnInfo(name = "angleMaxX")
    @SerializedName("angleMaxX")
    String angleXmax;

    @ColumnInfo(name = "distance")
    @SerializedName("distance")
    String distance;

    @ColumnInfo(name = "temps")
    @SerializedName("temps")
    String temps;

    @ColumnInfo(name = "dateTrajet")
    @SerializedName("dateTrajet")
    String dateTime;

    @ColumnInfo(name = "obstacles")
    @SerializedName("obstacles")
    String obstacles;

    public Trajet(int id, String vitesseMax, String vitesseMoy, String angleYmax, String angleXmax, String distance, String temps, String dateTime, String obstacles) {
        this.id = id;
        this.vitesseMax = vitesseMax;
        this.vitesseMoy = vitesseMoy;
        this.angleYmax = angleYmax;
        this.angleXmax = angleXmax;
        this.distance = distance;
        this.temps = temps;
        this.dateTime = dateTime;
        this.obstacles = obstacles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
