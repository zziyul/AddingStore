package com.example.han.adding;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CastListAdapter extends ArrayAdapter<PreviewClass> {

    List<PreviewClass> mData;
    Context context;

    public CastListAdapter(@NonNull Context context, int resource, @NonNull List<PreviewClass> objects) {
        super(context, resource, objects);

        mData = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            // 이미지, 텍스트 붙이기
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.fund_list_row, null);
            TextView tv = (TextView) v.findViewById(R.id.title);
            tv.setText(mData.get(position).getTitle());
            ImageView img = (ImageView) v.findViewById(R.id.img);
            Picasso.with(getContext())
                    .load(mData.get(position).getImage())
                    .into(img);
        }
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CastDetailActivity.class);
                intent.putExtra("castNum", mData.get(position).getCnum());
                context.startActivity(intent);
            }
        });

        return v;
    }
}
