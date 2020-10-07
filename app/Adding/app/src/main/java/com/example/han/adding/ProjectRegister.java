package com.example.han.adding;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

import static com.example.han.adding.MainActivity.user;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.lambdainvoker.*;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;


public class ProjectRegister extends AppCompatActivity {

    final int SELECT_IMAGE = 10;
    private StorageReference mStorageRef;


    Spinner favorite; // 관심분야
    ArrayList<String> favoItem;
    AdapterSpinner favoAdapter;
    final String[] favoItemArray = {"건강", "게임", "교육", "금융", "쇼핑", "스포츠", "미디어", "소셜"};


    String kind; // 분야
    EditText pName;
    ImageView pimg;
    Uri pimgUrl;
    EditText pIntro;
    CalendarView date;
    EditText fundGoal;

    int myear;
    int mmonth;
    int mdayOfMonth;

    static int projNum;
    static int scrumNum;


    static final int COUNT = 15;//자바 상수 선언
    Button[] bt1;
    int bt1ScrumNum[];
    Button[] bt2;
    int bt2ScrumNum[];
    Button[] bt3;
    int bt3ScrumNum[];
    ArrayList<ShowScrumListResponse> scrumListResponseArrayList;
    static String scrum_name;
    static String scrum_intro;
    private ProjectRegister thisActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_register);

        thisActivity = this;

        // 스피너 연결
        favorite = (Spinner) findViewById(R.id.kind);
        favoItem = new ArrayList<>();
        Collections.addAll(favoItem, favoItemArray);
        favoAdapter = new AdapterSpinner(this, favoItem);
        favorite.setAdapter(favoAdapter);


        favorite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kind = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                kind = parent.getItemAtPosition(0).toString();

            }
        });

        pName = (EditText) findViewById(R.id.pName);
        pimg = (ImageView) findViewById(R.id.selectImg);
        pimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAppPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})) { //예전에 권한을 얻었다면 갤러리 화면으로 이동
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, SELECT_IMAGE);
                } else {
                    askPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SELECT_IMAGE); // 예전에 권한을 얻지 않았다면 권한요구
                }

            }
        });


        pIntro = (EditText) findViewById(R.id.pIntro);
        date = (CalendarView) findViewById(R.id.date);
        date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                myear = year;
                mmonth = month + 1; // 월은 가져올 때 1을 더해준다.
                mdayOfMonth = dayOfMonth;
                Log.v("asdf", myear + "-" + mmonth + "-" + mdayOfMonth);
            }
        });


        fundGoal = (EditText) findViewById(R.id.fundGoal);


        // 만약 수정이라면
        Intent intent = getIntent();
        projNum = intent.getIntExtra("projNum", 0);
        if (projNum != 0) {


            CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                    Regions.AP_NORTHEAST_2 // 리전
            );
            LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
                    Regions.AP_NORTHEAST_2, cognitoProvider);

            final ShowingProjectDetailInterface showingProjectDetailInterface = factory.build(ShowingProjectDetailInterface.class);

            final ProjectDetailRequestClass request = new ProjectDetailRequestClass(projNum);

            new AsyncTask<Integer, Void, ProjectDetailResponseClass>() {
                private MyError myError = null;


                @Override
                protected ProjectDetailResponseClass doInBackground(Integer... params) {
                    // invoke "echo" method. In case it fails, it will throw a
                    // LambdaFunctionException.
                    try {
                        return showingProjectDetailInterface.addingShowProjectDetail(params[0]);
                    } catch (LambdaFunctionException lfe) {
                        if (lfe.getMessage().equals("Handled")) {
                            myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                        }
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(ProjectDetailResponseClass result) {
                    if (result == null) {
                        if (myError != null) {
                            Log.e("myerror", myError.getErrorMessage());
                        }
                        return;
                    }

                    Log.v("수정", result + "");

                    Picasso.with(getApplicationContext())
                            .load(result.getImage())
                            .into(pimg);

                    pimgUrl = Uri.parse(result.getImage());
                    pName.setText(result.getPname().toString());
                    pIntro.setText(result.getIntroduction().toString());
                    //date.setDate();
                    fundGoal.setText(String.valueOf(result.getFundgoal()));

                    loadScrum();
                }
            }.execute(projNum);


        }

    }

    public void plusSub(View view) {
        // 서브 프로젝트 등록 페이지 로드
        scrumNum = 0;
        scrum_name = "";
        AddDialog addDialog = new AddDialog(this);
        addDialog.show();
    }


    public void register(View view) {
        Log.v("들어간 입력", "클릭함");
        if (pimgUrl == null) {
            return;
        }

        // 여기서 아마존 람다함수로 서버에 넣어주기
        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );
        LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
                Regions.AP_NORTHEAST_2, cognitoProvider);

        AmazonS3 s3 = new AmazonS3Client(cognitoProvider);
        final TransferUtility transferUtility = new TransferUtility(s3, getApplicationContext());


        final RegistProjInterface registProjInterface = factory.build(RegistProjInterface.class);
        if (fundGoal.getText().toString().length() == 0) {
            return;
        }
        int temp = Integer.parseInt(fundGoal.getText().toString());

        Log.v("들어간 정보", projNum + " " + user.getEmail() + " " + String.valueOf(pName.getText()) + " " + 0 + " " + kind + " " + myear + "-" + mmonth + "-" + mdayOfMonth + " " + temp + " " + pimgUrl.toString() + " " + String.valueOf(pIntro.getText()));
        RegisterProjRequest request = new RegisterProjRequest(projNum, user.getEmail(), String.valueOf(pName.getText()), 0, kind, myear + "-" + mmonth + "-" + mdayOfMonth,
                temp, pimgUrl.toString(), String.valueOf(pIntro.getText()));

        new AsyncTask<RegisterProjRequest, Void, Integer>() {

            private MyError myError = null;

            @Override
            protected Integer doInBackground(RegisterProjRequest... params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    return registProjInterface.addingregisterProject(params[0]);
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
                Toast.makeText(getApplicationContext(), "프로젝트가 등록됐습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        }.execute(request);


        // kind;
        pName.getText();
        pIntro.getText();
        Log.v("aa", myear + " : " + mmonth + " : " + mdayOfMonth);

        fundGoal.getText();

    }

    void askPermission(String[] requestPermission, int REQ_PERMISSION) { // Manifest에 등록된 권한들의 배열을 requestPermission이라고 한다. requestPermission중 전화 권한인 callRequest(프로그래먼가 만들어준 수)를 넘겨준다.
        ActivityCompat.requestPermissions( // 권한요청 코드
                this,
                requestPermission,
                REQ_PERMISSION // 전화 권한요청 코드
        );
    }


    boolean checkAppPermission(String[] requestPermission) {
        boolean[] requestResult = new boolean[requestPermission.length];
        for (int i = 0; i < requestResult.length; i++) {
            requestResult[i] = (ContextCompat.checkSelfPermission(this,
                    requestPermission[i]) == PackageManager.PERMISSION_GRANTED);
            if (!requestResult[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { // 권한 요청에 대한 질문이 끝났을 때 자동으로 호출된다.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SELECT_IMAGE: // 갤러리픽 권한에 대한 내용을 평가해준다.
                if (checkAppPermission(permissions)) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, SELECT_IMAGE);
                } else {
                    Toast.makeText(getApplicationContext(), "권한 획득에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                mStorageRef = FirebaseStorage.getInstance().getReference("img/");

                pimg.setImageURI(data.getData()); // getData로 넘어오는 정보를 바로 지정해준다.

                pimg.setDrawingCacheEnabled(true);
                pimg.buildDrawingCache();
                Bitmap bitmap = pimg.getDrawingCache();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Log.v("asdf", bitmap + "");
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] pimgdata = baos.toByteArray();

                UploadTask uploadTask = mStorageRef.putBytes(pimgdata);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Log.v("asdf", exception + "");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        pimgUrl = taskSnapshot.getDownloadUrl();
                        Log.v("asdf", pimgUrl.toString());
                    }
                });
            }
        }

    }

    private void initButton() {


        // 개발할 기능
        bt1 = new Button[COUNT];
        bt1ScrumNum = new int[COUNT];
        bt1[0] = (Button) findViewById(R.id.bt1_1);
        bt1[1] = (Button) findViewById(R.id.bt1_2);
        bt1[2] = (Button) findViewById(R.id.bt1_3);
        bt1[3] = (Button) findViewById(R.id.bt1_4);
        bt1[4] = (Button) findViewById(R.id.bt1_5);
        bt1[5] = (Button) findViewById(R.id.bt1_6);
        bt1[6] = (Button) findViewById(R.id.bt1_7);
        bt1[7] = (Button) findViewById(R.id.bt1_8);
        bt1[8] = (Button) findViewById(R.id.bt1_9);
        bt1[9] = (Button) findViewById(R.id.bt1_10);
        bt1[10] = (Button) findViewById(R.id.bt1_11);
        bt1[11] = (Button) findViewById(R.id.bt1_12);
        bt1[12] = (Button) findViewById(R.id.bt1_13);
        bt1[13] = (Button) findViewById(R.id.bt1_14);
        bt1[14] = (Button) findViewById(R.id.bt1_15);


        // 개발 중인 기능
        bt2 = new Button[COUNT];
        bt2ScrumNum = new int[COUNT];
        bt2[0] = (Button) findViewById(R.id.bt2_1);
        bt2[1] = (Button) findViewById(R.id.bt2_2);
        bt2[2] = (Button) findViewById(R.id.bt2_3);
        bt2[3] = (Button) findViewById(R.id.bt2_4);
        bt2[4] = (Button) findViewById(R.id.bt2_5);
        bt2[5] = (Button) findViewById(R.id.bt2_6);
        bt2[6] = (Button) findViewById(R.id.bt2_7);
        bt2[7] = (Button) findViewById(R.id.bt2_8);
        bt2[8] = (Button) findViewById(R.id.bt2_9);
        bt2[9] = (Button) findViewById(R.id.bt2_10);
        bt2[10] = (Button) findViewById(R.id.bt2_11);
        bt2[11] = (Button) findViewById(R.id.bt2_12);
        bt2[12] = (Button) findViewById(R.id.bt2_13);
        bt2[13] = (Button) findViewById(R.id.bt2_14);
        bt2[14] = (Button) findViewById(R.id.bt2_15);


        // 개발한 기능
        bt3 = new Button[COUNT];
        bt3ScrumNum = new int[COUNT];
        bt3[0] = (Button) findViewById(R.id.bt3_1);
        bt3[1] = (Button) findViewById(R.id.bt3_2);
        bt3[2] = (Button) findViewById(R.id.bt3_3);
        bt3[3] = (Button) findViewById(R.id.bt3_4);
        bt3[4] = (Button) findViewById(R.id.bt3_5);
        bt3[5] = (Button) findViewById(R.id.bt3_6);
        bt3[6] = (Button) findViewById(R.id.bt3_7);
        bt3[7] = (Button) findViewById(R.id.bt3_8);
        bt3[8] = (Button) findViewById(R.id.bt3_9);
        bt3[9] = (Button) findViewById(R.id.bt3_10);
        bt3[10] = (Button) findViewById(R.id.bt3_11);
        bt3[11] = (Button) findViewById(R.id.bt3_12);
        bt3[12] = (Button) findViewById(R.id.bt3_13);
        bt3[13] = (Button) findViewById(R.id.bt3_14);
        bt3[14] = (Button) findViewById(R.id.bt3_15);

        int bt1fill = 0;
        int bt2fill = 0;
        int bt3fill = 0;
        Log.v("리스트", scrumListResponseArrayList.size() + "");
        for (int i = 0; i < scrumListResponseArrayList.size(); i++) {
            switch (scrumListResponseArrayList.get(i).getState()) {
                case 0:
                    bt1ScrumNum[bt1fill] = scrumListResponseArrayList.get(i).getSnum();
                    bt1[bt1fill++].setBackgroundColor(Color.parseColor("#ffc29ac6"));
                    break;
                case 1:
                    bt2ScrumNum[bt2fill] = scrumListResponseArrayList.get(i).getSnum();
                    bt2[bt2fill++].setBackgroundColor(Color.parseColor("#ffbf7bc4"));
                    break;
                case 2:
                    bt3ScrumNum[bt3fill] = scrumListResponseArrayList.get(i).getSnum();
                    bt3[bt3fill++].setBackgroundColor(Color.parseColor("#ffb559bc"));
                    break;
            }

        }

        for (int i = 0; i < bt1fill; i++) {
            final int finalI = i;
            bt1[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                            getApplicationContext(),
                            "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                            Regions.AP_NORTHEAST_2 // 리전
                    );
                    LambdaInvokerFactory factory = new LambdaInvokerFactory(getApplicationContext(),
                            Regions.AP_NORTHEAST_2, cognitoProvider);

                    final ShowScrumDetailInterface showScrumDetailInterface = factory.build(ShowScrumDetailInterface.class);

                    new AsyncTask<Integer, Void, ScrumDetailResponse>() {
                        private MyError myError = null;


                        @Override
                        protected ScrumDetailResponse doInBackground(Integer... params) {
                            // invoke "echo" method. In case it fails, it will throw a
                            // LambdaFunctionException.
                            try {
                                return showScrumDetailInterface.addingShowScrumDetail(params[0]);
                            } catch (LambdaFunctionException lfe) {
                                if (lfe.getMessage().equals("Handled")) {
                                    myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                                }
                                return null;
                            }
                        }

                        @Override
                        protected void onPostExecute(ScrumDetailResponse result) {
                            if (result == null) {
                                if (myError != null) {
                                    Log.e("myerror", myError.getErrorMessage());
                                }
                                return;
                            }

                            scrum_name = result.getSname();
                            scrum_intro = result.getIntroduction();
                            scrumNum = bt1ScrumNum[finalI];

                            AddDialog addDialog = new AddDialog(thisActivity);
                            addDialog.show();

                        }
                    }.execute(bt1ScrumNum[finalI]);
                }
            });
        }


        for (int i = 0; i < bt2fill; i++) {
            final int finalI = i;
            bt2[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                            getApplicationContext(),
                            "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                            Regions.AP_NORTHEAST_2 // 리전
                    );
                    LambdaInvokerFactory factory = new LambdaInvokerFactory(getApplicationContext(),
                            Regions.AP_NORTHEAST_2, cognitoProvider);

                    final ShowScrumDetailInterface showScrumDetailInterface = factory.build(ShowScrumDetailInterface.class);

                    new AsyncTask<Integer, Void, ScrumDetailResponse>() {
                        private MyError myError = null;


                        @Override
                        protected ScrumDetailResponse doInBackground(Integer... params) {
                            // invoke "echo" method. In case it fails, it will throw a
                            // LambdaFunctionException.
                            try {
                                return showScrumDetailInterface.addingShowScrumDetail(params[0]);
                            } catch (LambdaFunctionException lfe) {
                                if (lfe.getMessage().equals("Handled")) {
                                    myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                                }
                                return null;
                            }
                        }

                        @Override
                        protected void onPostExecute(ScrumDetailResponse result) {
                            if (result == null) {
                                if (myError != null) {
                                    Log.e("myerror", myError.getErrorMessage());
                                }
                                return;
                            }

                            scrum_name = result.getSname();
                            scrum_intro = result.getIntroduction();
                            scrumNum = bt2ScrumNum[finalI];


                            AddDialog addDialog = new AddDialog(thisActivity);
                            addDialog.show();

                        }
                    }.execute(bt2ScrumNum[finalI]);

                }
            });

        }


        for (int i = 0; i < bt3fill; i++) {
            final int finalI = i;
            bt3[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                            getApplicationContext(),
                            "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                            Regions.AP_NORTHEAST_2 // 리전
                    );
                    LambdaInvokerFactory factory = new LambdaInvokerFactory(getApplicationContext(),
                            Regions.AP_NORTHEAST_2, cognitoProvider);

                    final ShowScrumDetailInterface showScrumDetailInterface = factory.build(ShowScrumDetailInterface.class);

                    new AsyncTask<Integer, Void, ScrumDetailResponse>() {
                        private MyError myError = null;


                        @Override
                        protected ScrumDetailResponse doInBackground(Integer... params) {
                            // invoke "echo" method. In case it fails, it will throw a
                            // LambdaFunctionException.
                            try {
                                return showScrumDetailInterface.addingShowScrumDetail(params[0]);
                            } catch (LambdaFunctionException lfe) {
                                if (lfe.getMessage().equals("Handled")) {
                                    myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                                }
                                return null;
                            }
                        }

                        @Override
                        protected void onPostExecute(ScrumDetailResponse result) {
                            if (result == null) {
                                if (myError != null) {
                                    Log.e("myerror", myError.getErrorMessage());
                                }
                                return;
                            }

                            scrum_name = result.getSname();
                            scrum_intro = result.getIntroduction();
                            scrumNum = bt3ScrumNum[finalI];


                            AddDialog addDialog = new AddDialog(thisActivity);
                            addDialog.show();

                        }
                    }.execute(bt3ScrumNum[finalI]);

                }
            });
        }


    }

    private void loadScrum() {
        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext().getApplicationContext(),
                "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );
        LambdaInvokerFactory factory = new LambdaInvokerFactory(getApplicationContext().getApplicationContext(),
                Regions.AP_NORTHEAST_2, cognitoProvider);

        final ShowingScrumListInterface showingScrumListInterface = factory.build(ShowingScrumListInterface.class,
                new LambdaDataListBinder(new TypeToken<ArrayList<ShowScrumListResponse>>() {
                }.getType()));

        new AsyncTask<Integer, Void, ArrayList<ShowScrumListResponse>>() {
            private MyError myError = null;

            @Override
            protected ArrayList<ShowScrumListResponse> doInBackground(Integer... params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    return showingScrumListInterface.addingshowScrum(params[0]);
                } catch (LambdaFunctionException lfe) {
                    if (lfe.getMessage().equals("Handled")) {
                        myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                    }
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ArrayList<ShowScrumListResponse> result) {
                if (result == null) {
                    if (myError != null) {
                        Log.e("myerror", myError.getErrorMessage());
                    }
                    return;
                }


                scrumListResponseArrayList = result;

                initButton();


            }
        }.execute(projNum);
    }


    void refresh() {
        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );
        LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
                Regions.AP_NORTHEAST_2, cognitoProvider);

        final ShowingProjectDetailInterface showingProjectDetailInterface = factory.build(ShowingProjectDetailInterface.class);

        final ProjectDetailRequestClass request = new ProjectDetailRequestClass(projNum);

        new AsyncTask<Integer, Void, ProjectDetailResponseClass>() {
            private MyError myError = null;


            @Override
            protected ProjectDetailResponseClass doInBackground(Integer... params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    return showingProjectDetailInterface.addingShowProjectDetail(params[0]);
                } catch (LambdaFunctionException lfe) {
                    if (lfe.getMessage().equals("Handled")) {
                        myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                    }
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ProjectDetailResponseClass result) {
                if (result == null) {
                    if (myError != null) {
                        Log.e("myerror", myError.getErrorMessage());
                    }
                    return;
                }

                Log.v("수정", result + "");

                Picasso.with(getApplicationContext())
                        .load(result.getImage())
                        .into(pimg);

                pimgUrl = Uri.parse(result.getImage());
                pName.setText(result.getPname().toString());
                pIntro.setText(result.getIntroduction().toString());
                //date.setDate();
                fundGoal.setText(String.valueOf(result.getFundgoal()));

                loadScrum();
            }
        }.execute(projNum);
    }
}

