package com.r4sh33d.medmanager;

import android.app.job.JobParameters;

import com.firebase.jobdispatcher.JobService;


public class MedJobService extends JobService {

    @Override
    public boolean onStartJob(com.firebase.jobdispatcher.JobParameters job) {
        return false;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        return false;
    }
}

