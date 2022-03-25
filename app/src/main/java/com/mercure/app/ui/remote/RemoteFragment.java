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
    Switch startAutoMode, startLight;
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
        startAutoMode = view.findViewById(R.id.startAutoMode);
        startLight    = view.findViewById(R.id.startLight);
        carVideo      = view.findViewById(R.id.carVideo);

        startAutoMode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(startAutoMode.isChecked())
                {

                }
                else {

                    stop();
                }
            }
        });
        startLight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(startLight.isChecked())
                {
                    mainActivity.publishing("lumiere", "allume");
                }
                else{
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