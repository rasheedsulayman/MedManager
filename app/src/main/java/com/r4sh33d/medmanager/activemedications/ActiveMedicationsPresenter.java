package com.r4sh33d.medmanager.activemedications;

/**
 * Created by rasheed on 3/1/18.
 */

public class ActiveMedicationsPresenter implements ActiveMedicationsContract.Presenter {
    private ActiveMedicationsContract.View view;


    ActiveMedicationsPresenter(ActiveMedicationsContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {}

    @Override
    public void addMedication(String args) {}

}
