package com.r4sh33d.medmanager.updateprofile;

import android.net.Uri;

import com.r4sh33d.medmanager.base.BaseContract;

public interface UpdateProfileContract {

    interface Presenter {
        void start();

        void updateProfile(String displayName, String path);

        void updateProfileDetails(String displayName, Uri photoUri);

        void detachListeners();
    }

    interface View extends BaseContract.view {
        void onProfileSuccesfullyUpdated();
    }
}
