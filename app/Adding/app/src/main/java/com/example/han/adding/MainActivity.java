package com.example.han.adding;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;


public class MainActivity extends AppCompatActivity {


    static public UserInfo user;
    TextView signIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v("asdf", user + "");

        signIn = (TextView) findViewById(R.id.signin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });
    }

    public void goCast(View view) {
        Intent intent = new Intent(this, Collect.class);
        intent.putExtra("menu", "cast");
        startActivity(intent);
    }

    public void goFund(View view) {
        Intent intent = new Intent(this, Collect.class);
        intent.putExtra("menu", "fund");
        startActivity(intent);
    }

    public void goLogin(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
