package com.mercure.app.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mercure.app.MainActivity;
import com.mercure.app.R;
import com.mercure.app.databinding.FragmentHomeBinding;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    Context context;

    ConstraintLayout frameConnecting;
    ConstraintLayout frameConnectionFailed;

    ImageView btHomeRefreshConnection;

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
        frameConnecting = view.findViewById(R.id.frameConnecting);
        frameConnectionFailed = view.findViewById(R.id.frameConnectionFailed);
        btHomeRefreshConnection = view.findViewById(R.id.btHomeRefreshConnection);

        btHomeRefreshConnection.setOnClickListener(this::refreshConnection);

        if(MainActivity.isConnected) {
            frameConnecting.setVisibility(View.GONE);
            frameConnectionFailed.setVisibility(View.GONE);
        }
        else {
            frameConnecting.setVisibility(View.VISIBLE);
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
}