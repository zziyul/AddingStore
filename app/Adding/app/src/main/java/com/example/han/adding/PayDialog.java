package com.example.han.adding;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class PayDialog extends Dialog {


    TextView closeBtn;
    TextView nowCash;
    TextView chargeCash;
    TextView resultCash;

    EditText payPrice;
    Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_pay);

        calcCash();


        closeBtn = (TextView) findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        nowCash = (TextView) findViewById(R.id.nowCash);
        chargeCash = (TextView) findViewById(R.id.chargeCash);
        resultCash = (TextView) findViewById(R.id.resultCash);
        payPrice = (EditText) findViewById(R.id.payPrice);
        confirmBtn = (Button) findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이 프로젝트의 모금액 +payPrice.getText();
                CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                        getContext().getApplicationContext(),
                        "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                        Regions.AP_NORTHEAST_2 // 리전
                );

                LambdaInvokerFactory factory = new LambdaInvokerFactory(getContext().getApplicationContext(),
                        Regions.AP_NORTHEAST_2, cognitoProvider);

                final ChargeCashInterface chargeCashInterface = factory.build(ChargeCashInterface.class);

                ChargeCashRequest chargeCashRequest = new ChargeCashRequest(user.email, -Integer.parseInt(payPrice.getText().toString()), 10, projNum);

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

                        Toast.makeText(getContext(), "성공적으로 투자했습니다", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }.execute(chargeCashRequest);

            }
        });


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String s = edit.toString();
                if (s.length() > 0) {
                    confirmBtn.setEnabled(true);
                    calcCash();
                } else {
                    confirmBtn.setEnabled(false);

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

        payPrice.addTextChangedListener(textWatcher);

    }

    private void calcCash() {

        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                getContext().getApplicationContext(),
                "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );
        LambdaInvokerFactory factory = new LambdaInvokerFactory(getContext().getApplicationContext(),
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

                nowCash.setText("현재 보유 캐시 : " + result.toString());
                chargeCash.setText("투자할 캐시 : " + payPrice.getText().toString());

                String money;
                if (payPrice.getText().toString().length() == 0) {
                    money = "0";
                } else {
                    money = String.valueOf(result - Integer.parseInt(payPrice.getText().toString()));
                }
                resultCash.setText("남은 캐시 : " + money);
            }
        }.execute(user.getEmail().toString());

    }

    public PayDialog(@NonNull Context context) {
        super(context);
    }
}
