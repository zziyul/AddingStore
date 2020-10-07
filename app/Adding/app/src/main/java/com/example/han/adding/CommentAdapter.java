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
import java.util.List;

class CommentAdapter extends ArrayAdapter<CommentData> {

    ArrayList<CommentData> m_Data;
    Context context;

    public CommentAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CommentData> objects) {
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
            v = vi.inflate(R.layout.comment_row, null);
        }
        if (m_Data.get(position) != null) {
            TextView member = (TextView) v.findViewById(R.id.member);
            TextView comment = (TextView) v.findViewById(R.id.comment);
            RatingBar score = (RatingBar) v.findViewById(R.id.score);
            TextView scoreInt = (TextView) v.findViewById(R.id.scoreInt);

            member.setText(m_Data.get(position).getMember());
            comment.setText(m_Data.get(position).getComment());
            score.setRating(m_Data.get(position).getScore());
            scoreInt.setText(String.valueOf(m_Data.get(position).getScore()));
        }
        return v;

    }

}
