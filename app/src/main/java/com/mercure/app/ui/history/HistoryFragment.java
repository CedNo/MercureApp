package com.mercure.app.ui.history;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.mercure.app.MainActivity;
import com.mercure.app.R;
import com.mercure.app.Trajet;
import com.mercure.app.TrajetDAO;
import com.mercure.app.TrajetsDB;
import com.mercure.app.databinding.FragmentHistoryBinding;

import java.util.List;

public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    private FragmentHistoryBinding binding;

    RecyclerView rvListe;
    AdapterHistory adapterListe;

    TrajetsDB tdb;
    public static TrajetDAO tdao;

    ProgressBar progObstacles;

    Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = view.getContext();

        progObstacles = view.findViewById(R.id.progObstacles);

        tdb = Room.databaseBuilder(context, TrajetsDB.class, "TrajetsDB")
                .allowMainThreadQueries()
                .build();

        if(tdao == null)
            tdao = tdb.tdao();

        historyViewModel.getTrajets().observe(getViewLifecycleOwner(), trajetsObserver);

        rvListe = view.findViewById(R.id.rv_history);
        rvListe.setHasFixedSize(true);
        rvListe.setLayoutManager(new LinearLayoutManager(context));
        historyViewModel.genererListe();
        adapterListe = new AdapterHistory(historyViewModel.getTrajets().getValue(), context, historyViewModel, 0, 0, 0, 0, 0, 0, 0);
        rvListe.setAdapter(adapterListe);

        MainActivity.frameConnecting.setTranslationZ(-10);
        MainActivity.frameConnectionFailed.setTranslationZ(-10);
    }

    Observer<List<Trajet>> trajetsObserver = new Observer<List<Trajet>>() {
        @Override
        public void onChanged(List<Trajet> trajetList) {

            List<Trajet> liste = historyViewModel.getTrajets().getValue();

            if(liste.size() != 0) {
                int maxObstacles = 0;
                int maxDuree = 0;
                int maxAngleY = 0;
                int maxAngleX = 0;
                int maxDistance = 0;
                int maxVitesseMoy = 0;
                int maxVitesseMax = 0;

                for(int i = 0; i < liste.size(); i++) {
                    int obstacles = Integer.parseInt(liste.get(i).getObstacles());
                    if(maxObstacles < obstacles){
                        maxObstacles = obstacles;
                    }

                    int duree = Integer.parseInt(liste.get(i).getTemps());
                    if(maxDuree < duree){
                        maxDuree = duree;
                    }

                    int angleY = Integer.parseInt(liste.get(i).getAngleYmax());
                    if(maxAngleY < angleY){
                        maxAngleY = angleY;
                    }

                    int angleX = Integer.parseInt(liste.get(i).getAngleXmax());
                    if(maxAngleX < angleX){
                        maxAngleX = angleX;
                    }

                    int distance = (int) Double.parseDouble(liste.get(i).getDistance());
                    if(maxDistance < distance){
                        maxDistance = distance;
                    }

                    int vitesseMoy = (int) Double.parseDouble(liste.get(i).getVitesseMoy());
                    if(maxVitesseMoy < vitesseMoy){
                        maxVitesseMoy = vitesseMoy;
                    }

                    int vitesseMax = (int) Double.parseDouble(liste.get(i).getVitesseMax());
                    if(maxVitesseMax < vitesseMax){
                        maxVitesseMax = vitesseMax;
                    }
                }

                adapterListe = new AdapterHistory(liste, context, historyViewModel, maxObstacles, maxDuree, maxAngleY, maxAngleX, maxDistance, maxVitesseMax, maxVitesseMoy);
                rvListe.setAdapter(adapterListe);
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}