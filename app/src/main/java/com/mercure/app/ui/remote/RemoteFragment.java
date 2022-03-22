package com.mercure.app.ui.remote;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

    Button btStop, btAvancer, btAvDroit, btArDroit, btReculer, btAvGauche, btArGauche;
    Switch startAutoMode, startVideo;
    VideoView carVideo;
    MainActivity mainActivity;

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
        btAvancer     = view.findViewById(R.id.btAvancer);
        btAvDroit     = view.findViewById(R.id.btAvDroit);
        btAvGauche    = view.findViewById(R.id.btAvGauche);
        btArDroit     = view.findViewById(R.id.btArDroit);
        btArGauche    = view.findViewById(R.id.btArGauche);
        btReculer     = view.findViewById(R.id.btReculer);
        startAutoMode = view.findViewById(R.id.startAutoMode);
        startVideo    = view.findViewById(R.id.startVideo);
        carVideo      = view.findViewById(R.id.carVideo);

        startAutoMode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(startAutoMode.isChecked())
                {
                    btAvancer.setVisibility(View.INVISIBLE);
                    btAvDroit.setVisibility(View.INVISIBLE);
                    btAvGauche.setVisibility(View.INVISIBLE);
                    btArDroit.setVisibility(View.INVISIBLE);
                    btArGauche.setVisibility(View.INVISIBLE);
                    btReculer.setVisibility(View.INVISIBLE);
                }
                else {
                    btAvancer.setVisibility(View.VISIBLE);
                    btAvDroit.setVisibility(View.VISIBLE);
                    btAvGauche.setVisibility(View.VISIBLE);
                    btArDroit.setVisibility(View.VISIBLE);
                    btArGauche.setVisibility(View.VISIBLE);
                    btReculer.setVisibility(View.VISIBLE);

                    stop();
                }
            }
        });
        startVideo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(startVideo.isChecked())
                {

                }
                else{
                    carVideo.stopPlayback();
                }
            }
        });
        btAvancer.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    mainActivity.publishing("move", "avancer");
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    stop();
                }

                return false;
            }
        });
        btAvDroit.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    mainActivity.publishing("move", "avDroit");
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    stop();
                }

                return false;
            }
        });
        btAvGauche.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    mainActivity.publishing("move", "avGauche");
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    stop();
                }

                return false;
            }
        });
        btArGauche.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    mainActivity.publishing("move", "arGauche");
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    stop();
                }

                return false;
            }
        });
        btArDroit.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    mainActivity.publishing("move", "arDroit");
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    stop();
                }

                return false;
            }
        });
        btReculer.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    mainActivity.publishing("move", "reculer");
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    stop();
                }

                return false;
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

    private void trajetAuto()
    {

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