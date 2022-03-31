package com.mercure.app.ui.remote;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mercure.app.MainActivity;
import com.mercure.app.R;
import com.mercure.app.databinding.FragmentRemoteBinding;

import org.w3c.dom.Text;

public class RemoteFragment extends Fragment
{
    private RemoteViewModel remoteViewModel;
    private FragmentRemoteBinding binding;

    Context context;

    Button btStop;
    Switch startAutoMode, openLum;
    MainActivity mainActivity;
    View joystick;
    static boolean isONTrajet = false;
    static boolean isONLum = false;

    SeekBar barVitesseDroite;
    SeekBar barVitesseTourne;

    TextView tvVitesseDroite;
    TextView tvVitesseTourne;

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

        btStop        = view.findViewById(R.id.btStop);
        startAutoMode = view.findViewById(R.id.startAutoMode);
        openLum       = view.findViewById(R.id.openLum);
        joystick      = view.findViewById(R.id.joystick);
        barVitesseTourne = view.findViewById(R.id.barVitesseTourne);
        barVitesseDroite = view.findViewById(R.id.barVitesseDroite);
        tvVitesseTourne  = view.findViewById(R.id.tvVitesseTourne);
        tvVitesseDroite  = view.findViewById(R.id.tvVitesseDroite);

        SharedPreferences sharedPref = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        int vDroit = sharedPref.getInt("vitesseDroit", 45);
        int vTourne = sharedPref.getInt("vitesseTourne", 50);
        barVitesseDroite.setProgress(vDroit);
        barVitesseTourne.setProgress(vTourne);

        if (isONTrajet == true)
        {
            startAutoMode.setChecked(true);
            joystick.setVisibility(view.INVISIBLE);
            setVitesseVisibility(view, false);
        }
        else {
            startAutoMode.setChecked(false);
            joystick.setVisibility(view.VISIBLE);
            setVitesseVisibility(view, true);

        }

        if (isONLum == true)
        {
            openLum.setChecked(true);
        }
        else openLum.setChecked(false);

        barVitesseDroite.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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
                    setVitesseVisibility(view, false);
                }
                else {
                    isONTrajet = false;
                    mainActivity.publishing("move", "stop_auto");
                    joystick.setVisibility(view.VISIBLE);
                    setVitesseVisibility(view, true);
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
        btStop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                stop();
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

    private void setVitesseVisibility(View view, boolean isVisible) {
        if(isVisible){
            tvVitesseTourne.setVisibility(view.VISIBLE);
            barVitesseTourne.setVisibility(view.VISIBLE);
            barVitesseDroite.setVisibility(view.VISIBLE);
        }
        else{
            tvVitesseTourne.setVisibility(view.INVISIBLE);
            barVitesseTourne.setVisibility(view.INVISIBLE);
            barVitesseDroite.setVisibility(view.INVISIBLE);
        }
    }
}