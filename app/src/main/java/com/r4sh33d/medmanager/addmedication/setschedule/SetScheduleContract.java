package com.r4sh33d.medmanager.addmedication.setschedule;

public interface SetScheduleContract {

    interface Presenter  {
        void start();
        void addMedication(String args);
    }

    interface View  {
        void moveToNextStep();
    }
}
