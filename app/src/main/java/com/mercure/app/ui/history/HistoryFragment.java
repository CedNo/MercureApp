package com.mercure.app.ui.history;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        tdb = Room.databaseBuilder(context, TrajetsDB.class, "TrajetsDB")
                .allowMainThreadQueries()
                .build();

        tdao = tdb.tdao();

        historyViewModel.getTrajets().observe(getViewLifecycleOwner(), trajetsObserver);

        rvListe = view.findViewById(R.id.rv_history);
        rvListe.setHasFixedSize(true);
        rvListe.setLayoutManager(new LinearLayoutManager(context));
        historyViewModel.genererListe();
        adapterListe = new AdapterHistory(historyViewModel.getTrajets().getValue(), context, historyViewModel);
        rvListe.setAdapter(adapterListe);

        MainActivity.frameConnecting.setTranslationZ(-10);
        MainActivity.frameConnectionFailed.setTranslationZ(-10);
    }

    Observer<List<Trajet>> trajetsObserver = new Observer<List<Trajet>>() {
        @Override
        public void onChanged(List<Trajet> trajetList) {
            if(historyViewModel.getTrajets().getValue().size() != 0) {
                adapterListe = new AdapterHistory(historyViewModel.getTrajets().getValue(), context, historyViewModel);
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