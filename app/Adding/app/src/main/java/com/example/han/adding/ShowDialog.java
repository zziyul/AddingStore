package com.example.han.adding;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import static com.example.han.adding.ProjectDetailActivity.scrum_intro;
import static com.example.han.adding.ProjectDetailActivity.scrum_name;

public class ShowDialog extends Dialog {

    Context context;
    TextView scrum_title;
    TextView closeBtn;
    TextView desc;
    EditText errorMsg;
    Button downBtn;
    Button submitBtn;
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
        errorMsg = (EditText) findViewById(R.id.errorMsg);

        downBtn = (Button) findViewById(R.id.downBtn);
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다운로드 하는 동작 수행
                if (file.length() > 0) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(file)));
                }
            }
        });

        submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 오류보고 하기
                CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                        getContext(),
                        "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                        Regions.AP_NORTHEAST_2 // 리전
                );
                LambdaInvokerFactory factory = new LambdaInvokerFactory(getContext(),
                        Regions.AP_NORTHEAST_2, cognitoProvider);

                final ScrumErrorSendInterface scrumErrorSendInterface = factory.build(ScrumErrorSendInterface.class);


                ScrumErrorSendRequest request = new ScrumErrorSendRequest(scrumNumDialog, user.getEmail(), errorMsg.getText().toString());

                new AsyncTask<ScrumErrorSendRequest, Void, Boolean>() {

                    private MyError myError = null;

                    @Override
                    protected Boolean doInBackground(ScrumErrorSendRequest... params) {
                        // invoke "echo" method. In case it fails, it will throw a
                        // LambdaFunctionException.
                        try {
                            return scrumErrorSendInterface.addingcreatesinquiry(params[0]);
                        } catch (LambdaFunctionException lfe) {
                            if (lfe.getMessage().equals("Handled")) {
                                myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                            }
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        if (result == null) {
                            if (myError != null) {
                                Log.e("myerror", myError.getErrorMessage());
                            }
                            return;
                        }

                        Toast.makeText(getContext(), "오류보고를 완료했습니다.", Toast.LENGTH_SHORT);
                        onBackPressed();
                    }
                }.execute(request);
            }
        });
    }

    public ShowDialog(@NonNull Context context, String file) {
        super(context);
        this.context = context;
        this.file = file;
    }
}
