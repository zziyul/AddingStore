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

import org.w3c.dom.Text;

import java.util.List;

public class ReportMyListAdapter extends ArrayAdapter<PreviewMyProjClass> {

    List<PreviewMyProjClass> mData;
    Context context;

    public ReportMyListAdapter(@NonNull Context context, int resource, @NonNull List<PreviewMyProjClass> objects) {
        super(context, resource, objects);

        mData = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.report_my_list_row, null);
        }
        // 이미지, 텍스트 붙이기
        ImageView imageView = (ImageView) v.findViewById(R.id.img);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView kind = (TextView) v.findViewById(R.id.kind);
        TextView deadline = (TextView) v.findViewById(R.id.deadline);
        TextView take = (TextView) v.findViewById(R.id.take);

        Picasso.with(getContext())
                .load(mData.get(position).getImage())
                .into(imageView);
        title.setText(mData.get(position).getPname());
        kind.setText(mData.get(position).getCategory());
        deadline.setText(mData.get(position).getDeadline());
        take.setText(String.valueOf(mData.get(position).getCurrent()));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProjectRegister.class);
                intent.putExtra("projNum", mData.get(position).getPnum());
                context.startActivity(intent);
            }
        });


        return v;
    }
}
