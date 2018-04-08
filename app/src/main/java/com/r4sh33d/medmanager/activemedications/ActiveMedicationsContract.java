package com.r4sh33d.medmanager.activemedications;

public interface ActiveMedicationsContract {

    interface Presenter  {
        void start();
        void addMedication(String args);
    }

    interface View  {
        void moveToNextStep();
    }
}
