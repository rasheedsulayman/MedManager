package com.r4sh33d.medmanager;

import com.r4sh33d.medmanager.database.LocalData;
import com.r4sh33d.medmanager.models.Interval;
import com.r4sh33d.medmanager.models.Medication;
import com.r4sh33d.medmanager.utility.Utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class UtilsUnitTest {

    @Test
    public void testProposalSpinnerHelper() throws Exception {
        ArrayList<Interval> intervals = LocalData.intervalArrayList;
        Interval interval = new Interval("Every 5 minutes", (5 / 60.0f));
        int expectedIndex = 0; // "5 minutes " interval is the first item in the position
        int actualIndex = Utils.getSpinnerIndexFromMedicationsList(interval.getTimeInMilliseconds(), intervals);
        assertEquals(expectedIndex, actualIndex);
    }

}
