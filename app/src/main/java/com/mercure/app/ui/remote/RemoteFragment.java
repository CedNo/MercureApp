package com.mercure.app.ui.remote;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.VideoView;

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

    Button btStop;
    Switch startAutoMode, openLum;
    MainActivity mainActivity;
    View joystick;
    static boolean isONTrajet = false;
    static boolean isONLum = false;

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
}