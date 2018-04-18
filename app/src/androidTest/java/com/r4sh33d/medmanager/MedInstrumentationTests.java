package com.r4sh33d.medmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;

import com.r4sh33d.medmanager.database.MedicationDBHelper;
import com.r4sh33d.medmanager.database.MedicationDao;
import com.r4sh33d.medmanager.models.Medication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MedInstrumentationTests {
    private Context context;
    SQLiteDatabase db;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getContext();
        MedicationDBHelper medicationDBHelper = new MedicationDBHelper(context);
        db = medicationDBHelper.getWritableDatabase();
    }


    @Test
    public void testAddMedication() {
        Medication expectedMedication = new Medication("Paracetamol",
                "For Headache",
                "50", 1524008520000L, 1524008520000L, 52200000, 2, 1524014256962L);
        MedicationDao.addMedication(expectedMedication, db);
        Medication actualMedication = MedicationDao.getMedicationInfoWithId(2, db);
        assertEquals(expectedMedication.dbRowId, actualMedication.dbRowId);
    }

    @Test
    public  void testGetMedicationList() {
        Medication expectedMedication = new Medication("Paracetamol",
                "For Headache",
                "50", 1524008520000L, 1524008520000L, 52200000, 2, 1524014256962L);
        MedicationDao.addMedication(expectedMedication, db);
        ArrayList<Medication> medications = MedicationDao.getMedicationsList(db, null);
        assertEquals(true, medications.size() > 0);
    }


    @Test
    public  void testUpdateMedicationNextRingTime() {
        Medication expectedMedication = new Medication("Paracetamol",
                "For Headache",
                "50", 1524008520000L, 1524008520000L, 52200000, 2, 1524014256962L);
        long expectedNewNextRingTime = 15240142509004L;
        MedicationDao.updateNextRingTime(expectedMedication.dbRowId, expectedNewNextRingTime, db);
        Medication actualMedication = MedicationDao.getMedicationInfoWithId(expectedMedication.dbRowId, db);
        assertEquals(expectedNewNextRingTime, actualMedication.nextRingTime);
    }

    @Test
    public void testUpdateMedication() {
        Medication medicationToTest = new Medication("Paracetamol",
                "For Headache",
                "50", 1524008520000L, 1524008520000L, 52200000, 2, 1524014256962L);
        MedicationDao.addMedication(medicationToTest, db);
        String expectedNameString = "Updated Parectamol";
        medicationToTest.name = expectedNameString;
        MedicationDao.updateMedication(medicationToTest , db);
        Medication actualMedication = MedicationDao.getMedicationInfoWithId(medicationToTest.dbRowId, db);
        assertEquals(expectedNameString, actualMedication.dbRowId);
    }
}
