package com.r4sh33d.medmanager.addmedication;

/**
 * Created by rasheed on 3/1/18.
 */

public class AddMedicationPresenter implements AddMedicationContract.Presenter {
    private AddMedicationContract.View view;


    AddMedicationPresenter(AddMedicationContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
    }

    @Override
    public void addMedication(String args) {

    }

}
