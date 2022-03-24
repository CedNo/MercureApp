package com.mercure.app.ui.history;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mercure.app.MainActivity;
import com.mercure.app.R;
import com.mercure.app.Trajet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class InfosTrajetFragment extends Fragment {

    private InfosTrajetViewModel mViewModel;

    public static InfosTrajetFragment newInstance() {
        return new InfosTrajetFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(InfosTrajetViewModel.class);

        return inflater.inflate(R.layout.infos_trajet_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        Trajet trajet = (Trajet) bundle.getSerializable("trajet");
        int maxObstacles = bundle.getInt("maxObstacles");
        int maxDuree = bundle.getInt("maxDuree");
        int maxAngleY = bundle.getInt("maxAngleY");
        int maxAngleX = bundle.getInt("maxAngleX");
        int maxDistance = bundle.getInt("maxDistance");
        int maxVitesseMoy = bundle.getInt("maxVitesseMoy");
        int maxVitesseMax = bundle.getInt("maxVitesseMax");

        TextView tvDate = view.findViewById(R.id.tv_infos_date);
        TextView tvDuree = view.findViewById(R.id.tv_infos_duree);
        ProgressBar progDuree = view.findViewById(R.id.progDuree);
        TextView tvDistance = view.findViewById(R.id.tv_infos_distance);
        ProgressBar progDistance = view.findViewById(R.id.progDistance);
        TextView tvVitesseMoyenne = view.findViewById(R.id.tv_infos_vitesse_moy);
        ProgressBar progVitesseMoy = view.findViewById(R.id.progVitesseMoy);
        TextView tvVitesseMax = view.findViewById(R.id.tv_infos_vitesse_max);
        ProgressBar progVitesseMax = view.findViewById(R.id.progVitesseMax);
        TextView tvAngleMaxY = view.findViewById(R.id.tv_infos_angleY_max);
        ProgressBar progAngleY = view.findViewById(R.id.progAngleY);
        TextView tvAngleMaxX = view.findViewById(R.id.tv_infos_angleX_max);
        ProgressBar progAngleX = view.findViewById(R.id.progAngleX);
        TextView tvObstacles = view.findViewById(R.id.tv_infos_obstacles);
        ProgressBar progObstacles = view.findViewById(R.id.progObstacles);

        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTrajet = LocalDateTime.parse(trajet.getDateTime(), f);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a");
        String txtDate = dateTrajet.format(formatter);

        tvDate.setText(txtDate);
        tvDuree.setText(trajet.getTemps() + " s");
        progDuree.setProgress(Integer.parseInt(trajet.getTemps()));
        progDuree.setMax(maxDuree);
        tvDistance.setText(trajet.getDistance() + " m");
        progDistance.setProgress((int) Double.parseDouble(trajet.getDistance()));
        progDistance.setMax(maxDistance);
        tvVitesseMoyenne.setText(trajet.getVitesseMoy() + " km/h");
        progVitesseMoy.setProgress((int) Double.parseDouble(trajet.getVitesseMoy()));
        progVitesseMoy.setMax(maxVitesseMoy);
        tvVitesseMax.setText(trajet.getVitesseMax() + " km/h");
        progVitesseMax.setProgress((int) Double.parseDouble(trajet.getVitesseMax()));
        progVitesseMax.setMax(maxVitesseMax);
        tvAngleMaxY.setText(trajet.getAngleYmax() + "°");
        progAngleY.setProgress(Integer.parseInt(trajet.getAngleYmax()));
        progAngleY.setMax(maxAngleY);
        tvAngleMaxX.setText(trajet.getAngleXmax() + "°");
        progAngleX.setProgress(Integer.parseInt(trajet.getAngleXmax()));
        progAngleX.setMax(maxAngleX);
        tvObstacles.setText(trajet.getObstacles() + "");
        progObstacles.setProgress(Integer.parseInt(trajet.getObstacles()));
        progObstacles.setMax(maxObstacles);
    }
}