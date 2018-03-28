package com.r4sh33d.medmanager.addmedication;

public interface AddMedicationContract {

    interface Presenter  {
        void start();
        void addMedication (String args);
    }

    interface View  {
        void moveToNextStep();
    }
}
