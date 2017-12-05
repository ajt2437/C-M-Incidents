package com.example.abetorres.cmincidents;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteTransactionListener;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.abetorres.cmincidents.data.IncidentsDbHelper;
import com.example.abetorres.cmincidents.data.IncidentsContract.IncidentsEntry;

public class AddIncidentActivity extends AppCompatActivity {

    private static final String TAG = "AddIncidentActivity";

    private EditText mEventEditText;
    private CheckBox mCExpectedCB;
    private CheckBox mCAbsentCB;
    private CheckBox mMExpectedCB;
    private CheckBox mMAbsentCB;
    private EditText mHoursLateEditText;
    private EditText mMinsLateEditText;
    private CheckBox mLateNACB;
    private SQLiteDatabase mDb;
    private View mainView;
    private Snackbar errorSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_incident);

        mainView = findViewById(R.id.add_incident_layout);
        mEventEditText = (EditText) findViewById(R.id.event_edit_text);
        mCExpectedCB = (CheckBox) findViewById(R.id.c_expected_cb);
        mCAbsentCB = (CheckBox) findViewById(R.id.camile_absent_cb);
        mMExpectedCB = (CheckBox) findViewById(R.id.m_expected_cb);
        mMAbsentCB = (CheckBox) findViewById(R.id.michael_absent_cb);
        mHoursLateEditText = (EditText) findViewById(R.id.hours_edit_text);
        mMinsLateEditText = (EditText) findViewById(R.id.minutes_edit_text);
        mLateNACB = (CheckBox) findViewById(R.id.n_a_late_check_button);

        IncidentsDbHelper dbHelper = new IncidentsDbHelper(this);

        mDb = dbHelper.getWritableDatabase();

    }

    public void expectedCheck(View view) {
        boolean cExpectedFlag = mCExpectedCB.isChecked();
        boolean mExpectedFlag = mMExpectedCB.isChecked();

        mCAbsentCB.setEnabled(cExpectedFlag);
        mMAbsentCB.setEnabled(mExpectedFlag);
        if (!cExpectedFlag) {
            mCAbsentCB.setChecked(false);
        }

        if (!mExpectedFlag) {
            mMAbsentCB.setChecked(false);
        }

    }

    public void absentCheck(View view) {
        boolean cAbsentFlag = mCAbsentCB.isChecked();
        boolean mAbsentFlag = mMAbsentCB.isChecked();

        mLateNACB.setChecked(cAbsentFlag && mAbsentFlag);
    }

    public boolean errorCheck() {
        String eventName = mEventEditText.getText().toString();
        boolean cExpectedFlag = mCExpectedCB.isChecked();
        boolean cAbsentFlag = mCAbsentCB.isChecked();
        boolean mExpectedFlag = mMExpectedCB.isChecked();
        boolean mAbsentFlag = mMAbsentCB.isChecked();
        String hoursString = mHoursLateEditText.getText().toString();
        String minsString = mMinsLateEditText.getText().toString();
        boolean naLateFlag = mLateNACB.isChecked();

        boolean errorFlag = false;
        View focusView = null;

        //reset errors
        mEventEditText.setError(null);
        mHoursLateEditText.setError(null);
        mMinsLateEditText.setError(null);


        if (TextUtils.isEmpty(eventName)) {
            mEventEditText.setError("Event name required");
            errorFlag = true;
            focusView = mEventEditText;

        }

        // case: Not expecting anyone
        else if (!cExpectedFlag && !mExpectedFlag) {
            if (errorSnackBar != null) {
                errorSnackBar.dismiss();
            }
            errorSnackBar = Snackbar.make(mainView, "Not expecting anyone", Snackbar.LENGTH_SHORT);
            errorSnackBar.show();
            return true;
        }

        //case: Cannot be absent and late
        else if (mAbsentFlag && cAbsentFlag && !naLateFlag) {
            if (errorSnackBar != null) {
                errorSnackBar.dismiss();
            }
            errorSnackBar = Snackbar.make(mainView, "Cannot be absent and late", Snackbar.LENGTH_SHORT);
            errorSnackBar.show();
            return true;
        }

        else if (TextUtils.isEmpty(hoursString)) {
            mHoursLateEditText.setError("Cannot be empty");
            errorFlag = true;
            focusView = mHoursLateEditText;
        }
        else if (TextUtils.isEmpty(minsString)) {
            mMinsLateEditText.setError("Cannot be empty");
            errorFlag = true;
            focusView = mMinsLateEditText;
        }


        if (errorFlag) {
            Log.e(TAG, "Focus View requested");
            focusView.requestFocus();
        }

        return errorFlag;
    }

    public void onClickSave(View view) {
        ContentValues contentValues = new ContentValues();

        String eventName = mEventEditText.getText().toString();
        boolean cExpectedFlag = mCExpectedCB.isChecked();
        boolean cAbsentFlag = mCAbsentCB.isChecked();
        boolean mExpectedFlag = mMExpectedCB.isChecked();
        boolean mAbsentFlag = mMAbsentCB.isChecked();
        String hoursString = mHoursLateEditText.getText().toString();
        String minsString = mMinsLateEditText.getText().toString();
        boolean naLateFlag = mLateNACB.isChecked();

        int totalTimeLate, timeLateMins, timeLateHrs;

        if (errorCheck()) {
            return;
        }

        if (!naLateFlag) {

            try {
                timeLateHrs = Integer.parseInt(hoursString);
                timeLateMins = Integer.parseInt(minsString);
            }
            catch (NumberFormatException ex) {
                Log.e(TAG, "Not a number");
                return;
            }
            totalTimeLate = timeLateMins + (60 * timeLateHrs);
        }
        else {
            totalTimeLate = 0;
        }
        contentValues.put(IncidentsEntry.COLUMN_EVENT, eventName);
        contentValues.put(IncidentsEntry.COLUMN_C_EXPECTED, (cExpectedFlag) ? 1 : 0);
        contentValues.put(IncidentsEntry.COLUMN_C_ABSENT, (cAbsentFlag) ? 1 : 0);
        contentValues.put(IncidentsEntry.COLUMN_M_EXPECTED, (mExpectedFlag) ? 1 : 0);
        contentValues.put(IncidentsEntry.COLUMN_M_ABSENT, (mAbsentFlag) ? 1 : 0);

        contentValues.put(IncidentsEntry.COLUMN_TIME_LATE, totalTimeLate);

        mDb.insert(IncidentsEntry.TABLE_NAME,
                null,
                contentValues);

        finish();

    }
}
