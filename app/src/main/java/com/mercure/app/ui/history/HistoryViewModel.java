package com.mercure.app.ui.history;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mercure.app.InterfaceServeur;
import com.mercure.app.RetrofitInstance;
import com.mercure.app.Trajet;
import com.mercure.app.TrajetDAO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryViewModel extends ViewModel {

    private static MutableLiveData<List<Trajet>> trajetsLiveData;

    TrajetDAO tdao;

    public LiveData<List<Trajet>> getTrajets() {
        if (trajetsLiveData == null) {
            List<Trajet> list = new ArrayList<Trajet>();
            trajetsLiveData = new MutableLiveData<List<Trajet>>();
            trajetsLiveData.setValue(list);
            genererListe();
            Log.d("[LISTE]", "LISTE GENERER -> " + trajetsLiveData.getValue());
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

    public void genererListe() {
        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
        Call<List<Trajet>> call = serveur.getTrajets();

        tdao = HistoryFragment.tdao;

        List<Trajet> t = Arrays.asList(tdao.getTrajets());
        Collections.reverse(t);

        trajetsLiveData.postValue(t);

        call.enqueue(new Callback<List<Trajet>>() {
            @Override
            public void onResponse(Call<List<Trajet>> call, Response<List<Trajet>> response) {
                if(tdao.getNombreTrajets() > 0) {
                    DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime lastTimeRemoteDB = LocalDateTime.parse(response.body().get(0).getDateTime(), f);
                    LocalDateTime lastTimeLocalDB = LocalDateTime.parse(tdao.getLastTrajet().getDateTime(), f);

                    Log.d("[DATA]", lastTimeLocalDB + " " + lastTimeRemoteDB);

                    if(lastTimeRemoteDB.isAfter(lastTimeLocalDB)){
                        tdao.deleteTrajets();
                        tdao.ajouterPlusieursTrajets(response.body());
                        trajetsLiveData.postValue(response.body());
                    }
                }
                else {
                    tdao.ajouterPlusieursTrajets(response.body());
                    trajetsLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Trajet>> call, Throwable t) {
                Log.d("[LISTE]", "" + t);
            }
        });
    }
}