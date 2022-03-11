package com.mercure.app.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mercure.app.MainActivity;
import com.mercure.app.R;
import com.mercure.app.databinding.FragmentHomeBinding;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    Context context;

    ImageView btHomeRefreshConnection;

    CustomGauge displaySpeed;
    TextView tvSpeed;
    CustomGauge displayAngleFace;
    CustomGauge displayAngleLateral;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = view.getContext();
        MainActivity.frameConnecting = view.findViewById(R.id.frameConnecting);
        MainActivity.frameConnectionFailed = view.findViewById(R.id.frameConnectionFailed);
        MainActivity.frameInfos = view.findViewById(R.id.infos_layout);
        btHomeRefreshConnection = view.findViewById(R.id.btHomeRefreshConnection);
        displaySpeed = view.findViewById(R.id.displaySpeed);
        displayAngleFace = view.findViewById(R.id.displayAngleFace);
        displayAngleLateral = view.findViewById(R.id.displayAngleLateral);
        tvSpeed = view.findViewById(R.id.tvSpeed);

        displayAngleFace.setValue(90);
        displayAngleLateral.setValue(90);

        btHomeRefreshConnection.setOnClickListener(this::refreshConnection);

        if(MainActivity.isConnected) {
            MainActivity.frameConnecting.setVisibility(View.GONE);
            MainActivity.frameConnectionFailed.setVisibility(View.GONE);
            view.findViewById(R.id.infos_layout).setVisibility(View.VISIBLE);
        }
        else {
            refreshConnection(view);
            MainActivity.frameConnecting.setVisibility(View.VISIBLE);
            view.findViewById(R.id.infos_layout).setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void refreshConnection(View view) {
        ((MainActivity)getActivity()).connect();
    }

    public void setSpeed(int value) {
        String txt = value + " m/s";

        if(value >= 0 && value <= 20) {
            displaySpeed.setValue(value);
            tvSpeed.setText(txt);
        }
    }
}