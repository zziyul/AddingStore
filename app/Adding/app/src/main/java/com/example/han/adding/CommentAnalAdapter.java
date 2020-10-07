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

class CommentAnalAdapter extends ArrayAdapter<CommentAnalData> {

    ArrayList<CommentAnalData> m_Data;
    Context context;

    public CommentAnalAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CommentAnalData> objects) {
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
            v = vi.inflate(R.layout.comment_anal_row, null);
        }
        if (m_Data.get(position) != null) {
            TextView member = (TextView) v.findViewById(R.id.member);
            TextView comment = (TextView) v.findViewById(R.id.comment);
            RatingBar score = (RatingBar) v.findViewById(R.id.score);
            TextView scoreInt = (TextView) v.findViewById(R.id.scoreInt);

            TextView job = (TextView) v.findViewById(R.id.mjob);
            TextView favo = (TextView) v.findViewById(R.id.mfavo);
            TextView gender = (TextView) v.findViewById(R.id.mgender);
            TextView age = (TextView) v.findViewById(R.id.mage);

            member.setText(m_Data.get(position).getMember());
            job.setText(m_Data.get(position).getJob().toString());
            favo.setText(m_Data.get(position).getFavorite().toString());

            Log.v("성별", m_Data.get(position).getGender().toString() + "");
            if (m_Data.get(position).getGender().toString().equals("0")) {
                gender.setText("남성");
            } else {
                gender.setText("여성");
            }
            age.setText(String.valueOf(m_Data.get(position).getAge() + "세"));


        }
        return v;

    }

}
