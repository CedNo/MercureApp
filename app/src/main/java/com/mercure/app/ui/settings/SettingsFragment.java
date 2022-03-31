package com.mercure.app.ui.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.mercure.app.MainActivity;
import com.mercure.app.R;
import com.mercure.app.ui.home.HomeFragment;

public class SettingsFragment extends Fragment {

    Context context;
    Button btSave;
    EditText inAdresse;
    LinearLayout layoutSettings;

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
        layoutSettings = view.findViewById(R.id.layoutSettings);

        btSave.setOnClickListener(this::saveSettings);

        inAdresse.setText(MainActivity.address);

        layoutSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard((MainActivity) getActivity());
            }
        });

        MainActivity.frameConnecting.setTranslationZ(-10);
        MainActivity.frameConnectionFailed.setTranslationZ(-10);
    }

    public void saveSettings(View view) {
        if(!inAdresse.getText().equals("")) {
            String adresse = inAdresse.getText().toString();
            MainActivity.setAddress(adresse);
            ((MainActivity)getActivity()).connect();
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("adresseMQTT", adresse);
            editor.apply();
        }
        else {
            Snackbar.make(layoutSettings, "Adresse IP manquante", Snackbar.LENGTH_LONG).show();
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }
}