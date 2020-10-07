package com.example.han.adding;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

class ScrumErrorListAdapter extends ArrayAdapter<ScrumErrorList> {

    ArrayList<ScrumErrorList> m_Data;
    Context context;

    public ScrumErrorListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ScrumErrorList> objects) {
        super(context, resource, objects);
        this.m_Data = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Log.v("형태", m_Data + "");

        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.scrum_error_row, null);
        }
        if (m_Data.get(position) != null) {
            TextView sname = (TextView) v.findViewById(R.id.sname);
            TextView state = (TextView) v.findViewById(R.id.state);
            TextView comment = (TextView) v.findViewById(R.id.comment);

            sname.setText(m_Data.get(position).getSname());
            if (m_Data.get(position).getState() == 0) {
                state.setText("개발전");
            } else if (m_Data.get(position).getState() == 1) {
                state.setText("개발중");
            } else {
                state.setText("개발후");
            }
            comment.setText(m_Data.get(position).getComment());
        }
        return v;

    }

}
