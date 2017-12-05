package com.example.abetorres.cmincidents;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.abetorres.cmincidents.data.IncidentsDbHelper;

import com.example.abetorres.cmincidents.data.IncidentsContract.*;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

public class MainActivity extends AppCompatActivity {

    private IncidentListAdapter mAdapter;
    private SQLiteDatabase mDb;
    private RecyclerView mIncidentRecyclerView;
    private DecoView mCAbsenceDecoView;

    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIncidentRecyclerView = (RecyclerView) findViewById(R.id.incidents_list_view);
        mCAbsenceDecoView = (DecoView) findViewById(R.id.c_absence_decoview);

        //Adding background  series
        SeriesItem backgroundSeriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, 50, 0)
                .build();

        int backIndex = mCAbsenceDecoView.addSeries(backgroundSeriesItem);

        //Adding actual data series
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF8800"))
                .setRange(0, 50, 0)
                .build();

        int series1Index = mCAbsenceDecoView.addSeries(seriesItem);

        //Create timed events to animate the DecoView data series
        mCAbsenceDecoView.addEvent(new DecoEvent.Builder(50)
                .setIndex(backIndex)
                .build());

        mCAbsenceDecoView.addEvent(new DecoEvent.Builder(16.3f)
                .setIndex(series1Index)
                .setDelay(5000)
                .build());

        mCAbsenceDecoView.addEvent(new DecoEvent.Builder(30f)
                .setIndex(series1Index)
                .setDelay(10000)
                .build());

        mIncidentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        IncidentsDbHelper dbHelper = new IncidentsDbHelper(this);

        mDb = dbHelper.getReadableDatabase();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {
        Cursor cursor = getAllIncidents();
        if (mAdapter == null) {
            mAdapter = new IncidentListAdapter(this, cursor);
            mIncidentRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.changeCursor(cursor);
            mAdapter.notifyDataSetChanged();
        }
        Log.d(TAG, "Cursor size: " + cursor.getCount());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.add_incident:
                Intent intent = new Intent(this, AddIncidentActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Cursor getAllIncidents() {
        return mDb.query(
                IncidentsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                IncidentsEntry.COLUMN_TIMESTAMP
        );
    }


}
