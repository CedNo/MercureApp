package com.mercure.app.ui.remote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mercure.app.R;
import com.mercure.app.databinding.FragmentRemoteBinding;

public class RemoteFragment extends Fragment
{

    private RemoteViewModel remoteViewModel;
    private FragmentRemoteBinding binding;

    Button btStop;
    ImageButton btAvancer, btReculer, btAvDroit, btAvGauche, btArDroit,btArGauche;
    Switch startAutoMode, startVideo;
    VideoView carVideo;

    public RemoteFragment()
    {

    }

    public static RemoteFragment newInstance()
    {
        return new RemoteFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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

        btAvancer.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {

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
    }

    private void stop()
    {

    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}