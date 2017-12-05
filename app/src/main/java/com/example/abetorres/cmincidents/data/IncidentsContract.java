package com.example.abetorres.cmincidents.data;

import android.provider.BaseColumns;

/**
 * Created by AbeTorres on 10/29/17.
 */

public class IncidentsContract {
    public static final class IncidentsEntry implements BaseColumns {
        public static final String TABLE_NAME = "incidents";
        public static final String COLUMN_EVENT = "event";
        public static final String COLUMN_C_ABSENT = "cAbsent";
        public static final String COLUMN_C_EXPECTED = "cExpected";
        public static final String COLUMN_M_ABSENT = "mAbsent";
        public static final String COLUMN_M_EXPECTED = "mExpected";
        public static final String COLUMN_TIME_LATE = "timeLate";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
