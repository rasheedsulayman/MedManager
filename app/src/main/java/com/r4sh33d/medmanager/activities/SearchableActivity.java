package com.r4sh33d.medmanager.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.r4sh33d.medmanager.recycleradapters.MedicationsListAdapter;
import com.r4sh33d.medmanager.R;
import com.r4sh33d.medmanager.database.MedicationDBContract;
import com.r4sh33d.medmanager.database.MedicationsListLoader;
import com.r4sh33d.medmanager.models.Medication;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchableActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<Medication>>, SearchView.OnQueryTextListener {
      private static final  String TAG = SearchableActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private String queryEntered;
    private MedicationsListAdapter medicationsListAdapter;
    private String SEARCH_FILTER = MedicationDBContract.MedicationEntry._ID + " = -1";//We want an empty page at first

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicationsListAdapter = new MedicationsListAdapter(new ArrayList<Medication>() , true);
        recyclerView.setAdapter(medicationsListAdapter);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        onQueryTextChange(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.equals(queryEntered)) {
            return true;
        }
        queryEntered = newText.trim();
        if (!TextUtils.isEmpty(queryEntered) && queryEntered.length() >= 2) {
            SEARCH_FILTER = MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_NAME +  " LIKE '%" + queryEntered + "%'";
            Log.d(TAG,"Search filter now is : \n" + SEARCH_FILTER);
            getSupportLoaderManager().restartLoader(0, null, this);
        }
        return true;
    }

    @NonNull
    @Override
    public Loader<ArrayList<Medication>> onCreateLoader (int id, Bundle args) {
        return new MedicationsListLoader(this, SEARCH_FILTER, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Medication>> loader, ArrayList<Medication> data) {
        Log.d(TAG , "ONLOadFinished, Length is " + data.size());
        medicationsListAdapter.updateData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Medication>> loader) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu_activity_actions, menu);
        // Get the MenuItem for the action item
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when action item collapses
                onBackPressed();
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        };

        // Assign the listener to that action item
        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);
        // Configure the search info and add any event listeners...
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);
        searchItem.expandActionView();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
