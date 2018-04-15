package com.r4sh33d.medmanager.database;

import android.provider.BaseColumns;

/**
 * Created by rasheed on 3/30/18.
 */

public class MedicationDBContract {

    private MedicationDBContract() {}

    public static class MedicationEntry implements BaseColumns {
        public static final String TABLE_NAME = "medication";
        public static final String COLUMN_MEDICATION_NAME = "medication_name";
        public static final String COLUMN_MEDICATION_DESCRIPTION = "medication_subtitle";
        public static final String COLOMN_MEDICATION_QUANTITY = "medication_quantity";
        public static final String COLUMN_MEDICATION_INTERVAL = "medication_interval";
        public static final String COLUMN_MEDICATION_START_TIME = "medication_start_time";
        public static final String COLUMN_MEDICATION_END_TIME = "medication_end_time";//no of milliseconds till midnight of the last day specified.
        public static final String COLUMN_MEDICATION_NEXT_RING_TIME = "next_ring_time";
    }
}