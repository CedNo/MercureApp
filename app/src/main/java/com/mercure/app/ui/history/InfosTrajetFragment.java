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

        TextView tvDate = view.findViewById(R.id.tv_infos_date);
        TextView tvDuree = view.findViewById(R.id.tv_infos_duree);
        TextView tvDistance = view.findViewById(R.id.tv_infos_distance);
        TextView tvVitesseMoyenne = view.findViewById(R.id.tv_infos_vitesse_moy);
        TextView tvVitesseMax = view.findViewById(R.id.tv_infos_vitesse_max);
        TextView tvAngleMaxY = view.findViewById(R.id.tv_infos_angleY_max);
        TextView tvAngleMaxX = view.findViewById(R.id.tv_infos_angleX_max);
        TextView tvObstacles = view.findViewById(R.id.tv_infos_obstacles);

        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTrajet = LocalDateTime.parse(trajet.getDateTime(), f);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a");
        String txtDate = dateTrajet.format(formatter);

        tvDate.setText(txtDate);
        tvDuree.setText(trajet.getTemps() + " s");
        tvDistance.setText(trajet.getDistance() + " m");
        tvVitesseMoyenne.setText(trajet.getVitesseMoy() + " km/h");
        tvVitesseMax.setText(trajet.getVitesseMax() + " km/h");
        tvAngleMaxY.setText(trajet.getAngleYmax() + "°");
        tvAngleMaxX.setText(trajet.getAngleXmax() + "°");
        tvObstacles.setText(trajet.getObstacles() + "");
    }
}