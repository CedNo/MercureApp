package com.mercure.app.ui.history;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mercure.app.R;
import com.mercure.app.Trajet;

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
        TextView tvVitesseMoyenne = view.findViewById(R.id.tv_infos_vitesseMoyenne);
        TextView tvVitesseMax = view.findViewById(R.id.tv_infos_vitesseMax);
        TextView tvAngleMaxY = view.findViewById(R.id.tv_infos_angleMaxY);
        TextView tvAngleMaxX = view.findViewById(R.id.tv_infos_angleMaxX);
        TextView tvObstacles = view.findViewById(R.id.tv_infos_obstacles);

        tvDate.setText(trajet.getDateTime().toString());
        tvDuree.setText(trajet.getTemps() + "");
        tvDistance.setText(trajet.getDistance() + "");
        tvVitesseMoyenne.setText(trajet.getVitesseMoy() + "");
        tvVitesseMax.setText(trajet.getVitesseMax() + "");
        tvAngleMaxY.setText(trajet.getAngleYmax() + "");
        tvAngleMaxX.setText(trajet.getAngleXmax() + "");
        tvObstacles.setText(trajet.getObstacles() + "");
    }

}