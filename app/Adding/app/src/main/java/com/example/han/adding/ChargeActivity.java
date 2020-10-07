package com.example.han.adding;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.google.gson.Gson;

import static com.example.han.adding.MainActivity.user;

public class ChargeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charge);


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
                TextView myCash = (TextView) findViewById(R.id.myCash);
                myCash.setText(result.toString());
            }
        }.execute(user.getEmail().toString());


        ImageView chargebutton = (ImageView) findViewById(R.id.chargebutton);
        chargebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText chargeAmount = (EditText) findViewById(R.id.chargeAmount);
                // 충전이 발생해야함

                CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                        getApplicationContext().getApplicationContext(),
                        "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                        Regions.AP_NORTHEAST_2 // 리전
                );

                LambdaInvokerFactory factory = new LambdaInvokerFactory(getApplicationContext().getApplicationContext(),
                        Regions.AP_NORTHEAST_2, cognitoProvider);

                final ChargeCashInterface chargeCashInterface = factory.build(ChargeCashInterface.class);

                ChargeCashRequest chargeCashRequest = new ChargeCashRequest(user.email, Integer.parseInt(chargeAmount.getText().toString()), 0, 0);

                new AsyncTask<ChargeCashRequest, Void, Integer>() {
                    private MyError myError = null;

                    @Override
                    protected Integer doInBackground(ChargeCashRequest... params) {
                        // invoke "echo" method. In case it fails, it will throw a
                        // LambdaFunctionException.
                        try {
                            return chargeCashInterface.addingChangeMyCash(params[0]);
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

                        Toast.makeText(getApplicationContext(), "성공적으로 충전되었습니다", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }.execute(chargeCashRequest);

            }
        });
    }
}
