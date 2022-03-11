package com.mercure.app.ui.history;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mercure.app.Trajet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel extends ViewModel {

    private static MutableLiveData<List<Trajet>> trajetsLiveData;

    public LiveData<List<Trajet>> getTrajets() {
        if (trajetsLiveData == null) {
            trajetsLiveData = new MutableLiveData<List<Trajet>>();
            genererListe();
            Log.d("[LSITE]", "LISTE GENERER -> " + trajetsLiveData.getValue());
        }
        return trajetsLiveData;
    }

    public void addTrajet(final Trajet trajet) {
        List<Trajet> currentTrajets = trajetsLiveData.getValue();
        if (currentTrajets != null) {
            currentTrajets.add(trajet);
        }
        trajetsLiveData.postValue(currentTrajets);
    }

    private void genererListe() {

        List<Trajet> list = new ArrayList<Trajet>();

        list.add(new Trajet(40, 10, 12, 100.53, 135113, LocalDateTime.of(2022, 3,11,10,4,0), 3));
        list.add(new Trajet(35, 12, 4, 352.4, 35135, LocalDateTime.of(2022, 3, 11, 9, 30, 0), 5));
        list.add(new Trajet(70, 63, 1, 35.98, 1513, LocalDateTime.of(2022, 3, 11, 4, 5, 7), 7));
        list.add(new Trajet(12, 16, 6, 11.5, 64726, LocalDateTime.of(2022, 3, 9, 16, 5, 49), 2));
        list.add(new Trajet(12, 16, 6, 11.5, 64726, LocalDateTime.of(2022, 1, 9, 16, 5, 49), 2));
        list.add(new Trajet(12, 16, 6, 11.5, 64726, LocalDateTime.of(2019, 3, 9, 16, 5, 49), 2));

        trajetsLiveData.setValue(list);
    }
}