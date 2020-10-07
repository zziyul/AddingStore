package com.example.han.adding;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.google.gson.Gson;

import static com.example.han.adding.MainActivity.user;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        pw = (EditText) findViewById(R.id.pw);


        // 만약 email과 pw가 데이터베이스 안에 있다면

        //user = new UserInfo(String email, String pw, String name, int age, int gender, String job, String favorite);
    }

    public void goCast(View view) {


        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );
        LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
                Regions.AP_NORTHEAST_2, cognitoProvider);

        final LogInInterface logInInterface = factory.build(LogInInterface.class);

        LogInRequestClass request = new LogInRequestClass(email.getText().toString(), pw.getText().toString());

        new AsyncTask<LogInRequestClass, Void, AccountClass>() {

            private MyError myError = null;

            @Override
            protected AccountClass doInBackground(LogInRequestClass... params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    return logInInterface.addingLogIn(params[0]);
                } catch (LambdaFunctionException lfe) {
                    if (lfe.getMessage().equals("Handled")) {
                        myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                    }
                    return null;
                }
            }

            @Override
            protected void onPostExecute(AccountClass result) {
                if (result == null) {
                    if (myError != null) {
                        Log.e("myerror", myError.getErrorMessage());
                    }
                    return;
                }

                Toast.makeText(getApplicationContext(), "환영합니다!", Toast.LENGTH_LONG).show();
                user = new UserInfo(result.getEmail(), result.getPw(), result.getName(), result.getAge(), result.getGender(), result.getJob(), result.getInterested());
                Intent intent = new Intent(getApplicationContext(), Collect.class);
                intent.putExtra("menu", "cast");
                startActivity(intent);
                finish();
//                } else {
//                    Toast.makeText(getApplicationContext(), result.getStr(), Toast.LENGTH_LONG).show();
//                }
            }
        }.execute(request);


    }
}
