package com.mercure.app;

public class Trajet {

    int vitesseMax;
    int angleYmax;
    int angleXmax;
    double distance;
    double temps;

    public Trajet() {
        this.vitesseMax = 0;
        this.angleYmax = 0;
        this.angleXmax = 0;
        this.distance = 0;
        this.temps = 0;
    }

    public int getVitesseMax() {
        return vitesseMax;
    }

    public void setVitesseMax(int vitesse) {
        if(vitesse > this.vitesseMax)
            this.vitesseMax = vitesse;
    }

    public int getAngleYmax() {
        return angleYmax;
    }

    public void setAngleYmax(int angle) {
        if(angle > this.angleYmax)
            this.angleYmax = angle;
    }

    public int getAngleXmax() {
        return angleXmax;
    }

    public void setAngleXmax(int angle) {
        if(angle > this.angleYmax)
            this.angleXmax = angle;
    }
}
