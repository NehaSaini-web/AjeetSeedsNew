package com.example.ajeetseeds.ui.dailyActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DailyActivityViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DailyActivityViewModel() {
        mText = new MutableLiveData<String>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}