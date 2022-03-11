package com.mercure.app;

import java.time.LocalDateTime;

public class Trajet {

    int vitesseMax;
    int angleYmax;
    int angleXmax;
    double distance;
    double temps;
    LocalDateTime dateTime;
    int obstacles;

    public Trajet(int vitesseMax, int angleYmax, int angleXmax, double distance, double temps, LocalDateTime dateTime, int obstacles) {
        this.vitesseMax = vitesseMax;
        this.angleYmax = angleYmax;
        this.angleXmax = angleXmax;
        this.distance = distance;
        this.temps = temps;
        this.dateTime = dateTime;
        this.obstacles = obstacles;
    }

    public int getVitesseMax() {
        return vitesseMax;
    }

    public void setVitesseMax(int vitesseMax) {
        this.vitesseMax = vitesseMax;
    }

    public int getAngleYmax() {
        return angleYmax;
    }

    public void setAngleYmax(int angleYmax) {
        this.angleYmax = angleYmax;
    }

    public int getAngleXmax() {
        return angleXmax;
    }

    public void setAngleXmax(int angleXmax) {
        this.angleXmax = angleXmax;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTemps() {
        return temps;
    }

    public void setTemps(double temps) {
        this.temps = temps;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getObstacles() {
        return obstacles;
    }

    public void setObstacles(int obstacles) {
        this.obstacles = obstacles;
    }
}
