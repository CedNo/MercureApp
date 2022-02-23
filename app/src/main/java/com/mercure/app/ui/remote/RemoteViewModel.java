package com.mercure.app.ui.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RemoteViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RemoteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the remote fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}