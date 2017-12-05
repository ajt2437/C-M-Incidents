package com.example.abetorres.cmincidents.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.abetorres.cmincidents.data.IncidentsContract.*;
/**
 * Created by AbeTorres on 10/29/17.
 */

public class IncidentsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "incidents.db";

    private static final int DATABASE_VERSION = 3;

    public IncidentsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_INCIDENTS_TABLE = "CREATE TABLE " + IncidentsEntry.TABLE_NAME +
                " (" + IncidentsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IncidentsEntry.COLUMN_EVENT + " TEXT NOT NULL, " +
                IncidentsEntry.COLUMN_C_ABSENT + " INTEGER NOT NULL, " +
                IncidentsEntry.COLUMN_M_ABSENT + " INTEGER NOT NULL, " +
                IncidentsEntry.COLUMN_C_EXPECTED + " INTEGER NOT NULL, " +
                IncidentsEntry.COLUMN_M_EXPECTED + " INTEGER NOT NULL, " +
                IncidentsEntry.COLUMN_TIME_LATE + " REAL NOT NULL, " +
                IncidentsEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        db.execSQL(SQL_CREATE_INCIDENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + IncidentsEntry.TABLE_NAME);
        onCreate(db);
    }
}
