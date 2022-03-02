package com.mercure.app.ui.settings;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mercure.app.MainActivity;
import com.mercure.app.R;
import com.mercure.app.ui.home.HomeFragment;

public class SettingsFragment extends Fragment {

    Context context;
    Button btSave;
    EditText inAdresse;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = view.getContext();
        inAdresse = view.findViewById(R.id.inAdresseIP);
        btSave = view.findViewById(R.id.btSaveSettings);

        btSave.setOnClickListener(this::saveSettings);

        inAdresse.setText(MainActivity.address);
    }

    public void saveSettings(View view) {
        String adresse = inAdresse.getText().toString();

        if(!adresse.equals("")) {
            Log.d("[ADRESSE]", adresse);
            MainActivity.setAddress(adresse);
        }

        Navigation.findNavController(view).navigate(R.id.navigation_home);
        ((MainActivity)getActivity()).connect();
    }
}