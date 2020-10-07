package com.example.han.adding;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;


public class SignInActivity extends AppCompatActivity {

    EditText email;
    EditText pw;
    EditText name;
    EditText age;
    RadioGroup gender;
    int val_gender;
    String str_job;
    String str_fav;
    Spinner jobs; // 직업군
    Spinner favorite; // 관심분야
    ArrayList<String> jobItem;
    ArrayList<String> favoItem;
    AdapterSpinner jobAdapter;
    AdapterSpinner favoAdapter;
    final String[] jobItemArray = {"예술", "사무직", "자영업", "서비스직", "생산직", "농/축산업", "학생", "기타"};
    final String[] favoItemArray = {"건강", "게임", "교육", "금융", "쇼핑", "스포츠", "미디어", "소셜"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();


    }

    void init() {
        jobs = (Spinner) findViewById(R.id.jobs);
        favorite = (Spinner) findViewById(R.id.favorite);
        jobItem = new ArrayList<>();
        favoItem = new ArrayList<>();
        Collections.addAll(jobItem, jobItemArray);
        Collections.addAll(favoItem, favoItemArray);
        jobAdapter = new AdapterSpinner(this, jobItem);
        favoAdapter = new AdapterSpinner(this, favoItem);
        jobs.setAdapter(jobAdapter);
        favorite.setAdapter(favoAdapter);

    }

    public void goCast(View view) {
        email = (EditText) findViewById(R.id.email);
        String str_email = email.getText().toString();
        pw = (EditText) findViewById(R.id.pw);
        String str_pw = pw.getText().toString();
        name = (EditText) findViewById(R.id.name);
        String str_name = name.getText().toString();
        age = (EditText) findViewById(R.id.age);
        int val_age = Integer.parseInt(age.getText().toString());
        gender = (RadioGroup) findViewById(R.id.gender);
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.male:
                        val_gender = 0;
                        break;
                    case R.id.female:
                        val_gender = 1;
                        break;
                }
            }

        });
        jobs = (Spinner) findViewById(R.id.jobs);
        jobs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_job = parent.getItemAtPosition(position).toString();
                Log.v("asdf", str_job);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                str_job = parent.getItemAtPosition(0).toString();
            }
        });
        favorite = (Spinner) findViewById(R.id.favorite);
        favorite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_fav = parent.getItemAtPosition(position).toString();
                Log.v("asdf", str_fav);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                str_fav = parent.getItemAtPosition(0).toString();
            }
        });


        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );
        LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
                Regions.AP_NORTHEAST_2, cognitoProvider);

        final CreatingAccountInterface creatingAccountInterface = factory.build(CreatingAccountInterface.class);

        AccountClass request = new AccountClass(str_email, str_pw, str_name, val_age, val_gender, str_job, str_fav);

        new AsyncTask<AccountClass, Void, Boolean>() {
            private MyError myError = null;


            @Override
            protected Boolean doInBackground(AccountClass... params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    return creatingAccountInterface.addingCreateAccount(params[0]);
                } catch (LambdaFunctionException lfe) {
                    if (lfe.getMessage().equals("Handled")) {
                        myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                    }
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result == false) {
                    if (myError != null) {
                        Log.e("myerror", myError.getErrorMessage());
                    }
                    return;
                }
                Toast.makeText(getApplicationContext(), "가입 성공", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
//                } else {
//                    Toast.makeText(getApplicationContext(), result.getStr(), Toast.LENGTH_LONG).show();
//                }
            }
        }.execute(request);


    }


}