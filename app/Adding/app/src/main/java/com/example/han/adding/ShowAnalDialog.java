package com.example.han.adding;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.han.adding.ScrumFragment.scrum_intro;
import static com.example.han.adding.ScrumFragment.scrum_name;

public class ShowAnalDialog extends Dialog {

    Context context;
    TextView scrum_title;
    TextView closeBtn;
    TextView desc;
    String file;

    static int scrumNumDialog = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_scrum);


        scrum_title = (TextView) findViewById(R.id.scrum_title);
        scrum_title.setText(scrum_name);
        closeBtn = (TextView) findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        desc = (TextView) findViewById(R.id.desc);
        desc.setText(scrum_intro);

    }

    public ShowAnalDialog(@NonNull Context context, String file) {
        super(context);
        this.context = context;
        this.file = file;
    }
}
