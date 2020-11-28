package com.example.closet.ui.myclothes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyclothesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyclothesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}