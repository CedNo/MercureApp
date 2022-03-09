package com.mercure.app.ui.history;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mercure.app.Trajet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TrajetViewModel extends ViewModel
{
    private static MutableLiveData<List<Trajet>> trajetsLiveData;

    public LiveData<List<Trajet>> getEvenements() {
        if (trajetsLiveData == null) {
            trajetsLiveData = new MutableLiveData<List<Trajet>>();
            genererListe();
        }
        return trajetsLiveData;
    }

    public void addEvenement(final Trajet trajet) {
        List<Trajet> currentTrajets = trajetsLiveData.getValue();
        if (currentTrajets != null) {
            currentTrajets.add(trajet);
        }
        trajetsLiveData.postValue(currentTrajets);
    }

    private void genererListe() {

        LocalDateTime dateHeure = LocalDateTime.now();
        List<Trajet> list = new ArrayList<Trajet>();

//        list.add(new Trajet("CONCERT COOL ET NICE DE MALADE", "Un concert qui promet d'etre cool. Reste a voir :)", dateHeure));
//        list.add(new Trajet("Faire l'epicerie", "On fait l'epicerie ensemble :)", dateHeure));
//        list.add(new Trajet("Conference Apple", "Un conference sur les pommes?", dateHeure));
//        list.add(new Trajet("Noel", "C'est noel car il neige dans la tete.", dateHeure));

        trajetsLiveData.setValue(list);
    }
}

