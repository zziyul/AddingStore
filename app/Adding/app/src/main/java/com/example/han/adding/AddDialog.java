package com.example.han.adding;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static android.app.Activity.RESULT_OK;
import static com.example.han.adding.MainActivity.user;
import static com.example.han.adding.ProjectRegister.projNum;
import static com.example.han.adding.ProjectRegister.scrumNum;
import static com.example.han.adding.ProjectRegister.scrum_intro;
import static com.example.han.adding.ProjectRegister.scrum_name;

/**
 * Created by S on 2018-05-20.
 */

public class AddDialog extends Dialog {

    final String[] state = {"개발전", "개발중", "개발후"};
    Spinner stateSpinner; // 직업군
    ArrayList<String> stateItem;
    AdapterSpinner1 stateAdapter;
    TextView closeBtn;
    EditText title;
    EditText desc;
    Button fileUploadBtn;
    Button exec;
    int stateCode;
    static String fileStr1 = ""; // 업로드 파일 url 주소

    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_scrum);

        stateSpinner = (Spinner) findViewById(R.id.state);
        stateItem = new ArrayList<>();
        Collections.addAll(stateItem, state);
        stateAdapter = new AdapterSpinner1(getContext(), stateItem);
        stateSpinner.setAdapter(stateAdapter);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateCode = Integer.parseInt(String.valueOf(parent.getItemIdAtPosition(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                stateCode = 0;
            }
        });


        closeBtn = (TextView) findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title = (EditText) findViewById(R.id.title);
        desc = (EditText) findViewById(R.id.desc);
        fileUploadBtn = (Button) findViewById(R.id.fileUploadBtn);
        fileUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, UploadActivity.class));
            }
        });
        exec = (Button) findViewById(R.id.execBtn);
        exec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 서버에 서브 프로젝트 정보적용하기

                if (title.getText().toString().length() == 0) {
                    return;
                }

                // 여기서 아마존 람다함수로 서버에 넣어주기
                CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                        getContext(),
                        "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                        Regions.AP_NORTHEAST_2 // 리전
                );
                LambdaInvokerFactory factory = new LambdaInvokerFactory(getContext(),
                        Regions.AP_NORTHEAST_2, cognitoProvider);

                AmazonS3 s3 = new AmazonS3Client(cognitoProvider);
                final TransferUtility transferUtility = new TransferUtility(s3, getContext());


                final RegistScrumInterface registScrumInterface = factory.build(RegistScrumInterface.class);
//                int temp = Integer.parseInt(fundGoal.getText().toString());

                if (scrum_name != "") {

                }

                Log.v("스크럼넘버", scrumNum + "");
                Log.v("파일", fileStr1 + " 이다");
                if (fileStr1.length() == 0) {
                    fileStr1 = "0";
                }
                RegisterScrumRequest request = new RegisterScrumRequest(scrumNum, projNum, title.getText().toString(), stateCode, "", desc.getText().toString(), fileStr1);

                new AsyncTask<RegisterScrumRequest, Void, Boolean>() {

                    private MyError myError = null;

                    @Override
                    protected Boolean doInBackground(RegisterScrumRequest... params) {
                        // invoke "echo" method. In case it fails, it will throw a
                        // LambdaFunctionException.
                        try {
                            return registScrumInterface.addingRegisterScrum(params[0]);
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
                        Toast.makeText(getContext(), "스크럼이 등록됐습니다.", Toast.LENGTH_LONG).show();
                        onBackPressed();
                        ((ProjectRegister) mActivity).refresh();
                    }
                }.execute(request);
            }
        });


        if (scrum_name != "") {
            title.setText(scrum_name);
            desc.setText(scrum_intro);
        }
    }


    public AddDialog(@NonNull Context context) {
        super(context);
    }

    public AddDialog(Activity activity) {
        super(activity);
        mActivity = activity;
        setContentView(R.layout.add_scrum);
    }
}