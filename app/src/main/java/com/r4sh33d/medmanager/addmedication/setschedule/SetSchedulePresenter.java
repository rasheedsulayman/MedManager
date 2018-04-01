package com.r4sh33d.medmanager.addmedication.setschedule;

/**
 * Created by rasheed on 3/1/18.
 */

public class SetSchedulePresenter implements SetScheduleContract.Presenter {
    private SetScheduleContract.View view;


    SetSchedulePresenter(SetScheduleContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {}

    @Override
    public void addMedication(String args) {

    }

}
