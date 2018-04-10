package com.r4sh33d.medmanager;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseFragment  extends Fragment{

    public void setToolbarTitle (String title){
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setTitle(title);
    }
}
