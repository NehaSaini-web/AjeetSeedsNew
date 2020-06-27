package com.example.ajeetseeds.ui.upgradeApp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ajeetseeds.Model.AndroidVersion;

public class UpgradeAppViewModel extends ViewModel {
    private MutableLiveData<AndroidVersion> andoidVersionUPload;

    public UpgradeAppViewModel() {
        andoidVersionUPload = new MutableLiveData<>();
    }

    public LiveData<AndroidVersion> getAndroidVersion() {
        return andoidVersionUPload;
    }

    public void setAndroidVersion(AndroidVersion andoidVersionUPload) {
        this.andoidVersionUPload.setValue(andoidVersionUPload);
    }
}
