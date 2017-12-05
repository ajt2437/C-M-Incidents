package com.example.abetorres.cmincidents;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.abetorres.cmincidents.data.IncidentsContract.*;

/**
 * Created by AbeTorres on 10/29/17.
 */

public class IncidentListAdapter extends RecyclerView.Adapter<IncidentListAdapter.IncidentViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public IncidentListAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public IncidentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.incident_list_item, parent, false);
        return new IncidentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IncidentViewHolder holder, int position) {

        if (!mCursor.moveToPosition(position)) {
            return;
        }

        int eventNameIndex = mCursor.getColumnIndex(IncidentsEntry.COLUMN_EVENT);
        int cAbsentColIndex = mCursor.getColumnIndex(IncidentsEntry.COLUMN_C_ABSENT);
        int mAbsentColIndex = mCursor.getColumnIndex(IncidentsEntry.COLUMN_M_ABSENT);
        int timeLateColIndex = mCursor.getColumnIndex(IncidentsEntry.COLUMN_TIME_LATE);

        String eventName = mCursor.getString(eventNameIndex);
        int cAbsentFlag = mCursor.getInt(cAbsentColIndex);
        int mAbsentFlag = mCursor.getInt(mAbsentColIndex);
        long timeLate = mCursor.getLong(timeLateColIndex);

        holder.eventName.setText(eventName);
        holder.cAbsentCB.setChecked(cAbsentFlag == 1);
        holder.mAbsentCB.setChecked(mAbsentFlag == 1);

        if (timeLate == 0) {
            holder.timeLateTextView.setText("n/a");
        }
        else {
            long hoursLate = timeLate / 60;
            long minutesLate = timeLate % 60;
            String lateTimeString = hoursLate + "HH " + minutesLate + "MM"; //String.format("%2.d HH, %2.d MM", hoursLate, minutesLate);
            holder.timeLateTextView.setText(lateTimeString);
        }

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void changeCursor(Cursor cursor) {
        mCursor = cursor;
    }

    class IncidentViewHolder extends RecyclerView.ViewHolder {

        TextView eventName;
        CheckBox cAbsentCB;
        CheckBox mAbsentCB;
        TextView timeLateTextView;
        public IncidentViewHolder(View itemView) {
            super(itemView);
            eventName = (TextView) itemView.findViewById(R.id.event_text_view);
            cAbsentCB = (CheckBox) itemView.findViewById(R.id.camile_absence_cb);
            mAbsentCB = (CheckBox) itemView.findViewById(R.id.michael_absence_cb);
            timeLateTextView = (TextView) itemView.findViewById(R.id.time_late_text_view);
        }

    }

}
