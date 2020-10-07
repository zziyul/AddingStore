package com.example.han.adding;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.google.gson.Gson;

import static com.example.han.adding.MainActivity.user;
import static com.example.han.adding.ProjectDetailActivity.projNum;

public class ExchangeActivity extends AppCompatActivity {

    TextView myCash;
    Button exchangeBtn;
    EditText exCash;
    TextView nowCash;
    TextView expay;
    TextView resultCash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);


        myCash = (TextView) findViewById(R.id.myCash);
        exchangeBtn = (Button) findViewById(R.id.exchangeBtn);
        exchangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                        getApplicationContext().getApplicationContext(),
                        "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                        Regions.AP_NORTHEAST_2 // 리전
                );

                LambdaInvokerFactory factory = new LambdaInvokerFactory(getApplicationContext().getApplicationContext(),
                        Regions.AP_NORTHEAST_2, cognitoProvider);

                final ChargeCashInterface chargeCashInterface = factory.build(ChargeCashInterface.class);

                ChargeCashRequest chargeCashRequest = new ChargeCashRequest(user.email, -Integer.parseInt(exCash.getText().toString()), 10, projNum);

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

                        Toast.makeText(getApplicationContext(), "성공적으로 환급했습니다", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }.execute(chargeCashRequest);
            }
        });
        exCash = (EditText) findViewById(R.id.exCash);
        nowCash = (TextView) findViewById(R.id.nowCash);
        expay = (TextView) findViewById(R.id.expay);
        resultCash = (TextView) findViewById(R.id.resultCash);

        calcCash();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String s = edit.toString();
                if (s.length() > 0) {
                    exchangeBtn.setEnabled(true);
                    calcCash();
                } else {
                    exchangeBtn.setEnabled(false);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        };

        exCash.addTextChangedListener(textWatcher);


    }

    private void calcCash() {

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

                myCash.setText(result.toString());

                nowCash.setText("현재 보유한 캐쉬 : " + result.toString());
                expay.setText("환급할 캐쉬 : " + exCash.getText().toString());

                String money;
                if (exCash.getText().toString().length() == 0) {
                    money = "0";
                } else {
                    money = String.valueOf(result - Integer.parseInt(exCash.getText().toString()));
                }
                resultCash.setText("남은 캐시 : " + money);
            }
        }.execute(user.getEmail().toString());

    }

}
