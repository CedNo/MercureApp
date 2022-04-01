package com.mercure.app.ui.remote;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mercure.app.MainActivity;
import com.mercure.app.R;
import com.mercure.app.databinding.FragmentRemoteBinding;

public class RemoteFragment extends Fragment
{
    private RemoteViewModel remoteViewModel;
    private FragmentRemoteBinding binding;

    Context context;

    Button btKlaxon;
    Switch startAutoMode, openLum;
    MainActivity mainActivity;
    View joystick;
    static boolean isONTrajet = false;
    static boolean isONLum = false;

    SeekBar barVitesseDroite;
    SeekBar barVitesseTourne;

    TextView tvVitesseDroite;
    TextView tvVitesseTourne;

    Spinner spinner;
    int indexKlaxon = 0;

    public RemoteFragment()
    {
        // Required empty public constructor
    }

    public static RemoteFragment newInstance()
    {
        return new RemoteFragment();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        remoteViewModel =
                new ViewModelProvider(this).get(RemoteViewModel.class);

        binding = FragmentRemoteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();

        mainActivity = (MainActivity)getActivity();

        btKlaxon        = view.findViewById(R.id.btKlaxon);
        startAutoMode = view.findViewById(R.id.startAutoMode);
        openLum       = view.findViewById(R.id.openLum);
        joystick      = view.findViewById(R.id.joystick);
        barVitesseTourne = view.findViewById(R.id.barVitesseTourne);
        barVitesseDroite = view.findViewById(R.id.barVitesseDroite);
        tvVitesseTourne  = view.findViewById(R.id.tvVitesseTourne);
        tvVitesseDroite  = view.findViewById(R.id.tvVitesseDroite);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.klaxon_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                indexKlaxon = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SharedPreferences sharedPref = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        int vDroit = sharedPref.getInt("vitesseDroite", 45);
        int vTourne = sharedPref.getInt("vitesseTourne", 50);
        barVitesseDroite.setProgress(vDroit);
        setTextVitesseDroite(vDroit);
        barVitesseTourne.setProgress(vTourne);
        setTextVitesseTourne(vTourne);

        if (isONTrajet == true)
        {
            startAutoMode.setChecked(true);
            joystick.setVisibility(view.INVISIBLE);
        }
        else {
            startAutoMode.setChecked(false);
            joystick.setVisibility(view.VISIBLE);

        }

        if (isONLum == true)
        {
            openLum.setChecked(true);
        }
        else openLum.setChecked(false);

        barVitesseDroite.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setTextVitesseDroite(progress);
                setVitesse();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setVitesseSettings();
            }

        });

        barVitesseTourne.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setTextVitesseTourne(progress);
                setVitesse();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setVitesseSettings();
            }

        });

        startAutoMode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(startAutoMode.isChecked())
                {
                    isONTrajet = true;
                    mainActivity.publishing("move", "auto");
                    joystick.setVisibility(view.INVISIBLE);
                }
                else {
                    isONTrajet = false;
                    mainActivity.publishing("move", "stop_auto");
                    joystick.setVisibility(view.VISIBLE);
                    stop();
                }
            }
        });
        openLum.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(openLum.isChecked())
                {
                    isONLum = true;
                    mainActivity.publishing("lumiere", "allume");
                }
                else{
                    isONLum = false;
                    mainActivity.publishing("lumiere", "ferme");
                }
            }
        });

        btKlaxon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mainActivity.publishing("klaxon", indexKlaxon + "");
            }
        });

        MainActivity.frameConnecting.setTranslationZ(45);
        MainActivity.frameConnectionFailed.setTranslationZ(45);
    }

    private void stop()
    {
        mainActivity.publishing("move", "stop");
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

    private void setVitesse()
    {
        int vitesseTourne = barVitesseTourne.getProgress();
        int vitesseDroite = barVitesseDroite.getProgress();
        mainActivity.publishing("move", vitesseDroite + "@" + vitesseTourne);
    }

    private void setVitesseSettings() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("vitesseDroite", barVitesseDroite.getProgress());
        editor.putInt("vitesseTourne", barVitesseTourne.getProgress());
        editor.apply();
    }

    private void setTextVitesseDroite(int progress)
    {
        String sVitesse = "Vitesse en ligne droite : " + progress;
        tvVitesseDroite.setText(sVitesse);
    }

    private void setTextVitesseTourne(int progress)
    {
        String sVitesse = "Vitesse en tournant : " + progress;
        tvVitesseTourne.setText(sVitesse);
    }
}