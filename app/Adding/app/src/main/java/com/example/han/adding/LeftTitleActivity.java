package com.example.han.adding;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.Inet4Address;
import java.util.ArrayList;

import static com.example.han.adding.MainActivity.user;

public class LeftTitleActivity extends AppCompatActivity {

    RelativeLayout notLogin;
    RelativeLayout login;
    TextView userName;
    TextView currnetCash;
    Button logoutBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.left_title);

        checkLogin();


    }

    private void checkLogin() {
        notLogin = (RelativeLayout) findViewById(R.id.notLogin);
        login = (RelativeLayout) findViewById(R.id.login);

        if (user == null) { // 로그인이 안됐다면
            notLogin.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
        } else { // 로그인 됐다면
            notLogin.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            userName = (TextView) findViewById(R.id.userName);
            logoutBtn = (Button) findViewById(R.id.logoutBtn);
            logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user = null;
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            });


            currnetCash = (TextView) findViewById(R.id.currnetCash);
            userName.setText(user.getName().toString());

            // 캐쉬 계산
            CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext().getApplicationContext(),
                    "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                    Regions.AP_NORTHEAST_2 // 리전
            );
            LambdaInvokerFactory factory = new LambdaInvokerFactory(getApplicationContext().getApplicationContext(),
                    Regions.AP_NORTHEAST_2, cognitoProvider);

            final ShowMyCashInterface showMyCashInterface = factory.build(ShowMyCashInterface.class);

            new AsyncTask<String, Void, Integer>() {
                private MyError myError = null;

                @Override
                protected Integer doInBackground(String... params) {
                    // invoke "echo" method. In case it fails, it will throw a
                    // LambdaFunctionException.
                    try {
                        return showMyCashInterface.addingShowMyCash(params[0]);
                    } catch (LambdaFunctionException lfe) {
                        if (lfe.getMessage().equals("Handled")) {
                            myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                        }
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(Integer result) {
                    if (result == null) {
                        if (myError != null) {
                            Log.e("myerror", myError.getErrorMessage());
                        }
                        return;
                    }

                    currnetCash.setText("잔여 캐쉬 : " + result);
                }
            }.execute(user.getEmail().toString());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_slide_in_right);

    }

    public void goback(View view) {
        onBackPressed();
    }

    public void goCash(View view) {
        if (user == null) {
            Toast.makeText(getApplicationContext(), "로그인 해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, Collect.class);
        intent.putExtra("menu", "cash");
        setResult(RESULT_OK, intent);
        onBackPressed();
    }


    public void goCast(View view) {
        Intent intent = new Intent(this, Collect.class);
        intent.putExtra("menu", "cast");
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    public void goFund(View view) {
        Intent intent = new Intent(this, Collect.class);
        intent.putExtra("menu", "fund");
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    public void goReport(View view) {
        Intent intent = new Intent(this, Collect.class);
        intent.putExtra("menu", "report");
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    public void goSetting(View view) {
        Intent intent = new Intent(this, Collect.class);
        intent.putExtra("menu", "setting");
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    public void goLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void goSignin(View view) {
        startActivity(new Intent(this, SignInActivity.class));
    }


}
