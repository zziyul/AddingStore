package com.example.han.adding;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AnalActivity extends AppCompatActivity {
    Button x;
    TextView analyze;
    TextView scrum;
    AnalyzeFragment analyzeFragment;
    ScrumFragment scrumFragment;

    static int projNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anal);

        Intent intent = getIntent();
        projNum = intent.getIntExtra("projNum", 0);

        x = (Button) findViewById(R.id.x);
        analyze = (TextView) findViewById(R.id.analyze);
        scrum = (TextView) findViewById(R.id.scrum);
        analyzeFragment = new AnalyzeFragment();
        scrumFragment = new ScrumFragment();
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analyze.setTextColor(Color.parseColor("#000000"));
                scrum.setTextColor(Color.parseColor("#848484"));

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment, analyzeFragment, "analyze").commit();

            }
        });
        scrum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analyze.setTextColor(Color.parseColor("#848484"));
                scrum.setTextColor(Color.parseColor("#000000"));

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment, scrumFragment, "scrum").commit();
            }
        });

    }
}
