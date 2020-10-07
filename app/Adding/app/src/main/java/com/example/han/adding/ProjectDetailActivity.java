package com.example.han.adding;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.han.adding.MainActivity.user;
import static com.example.han.adding.ShowDialog.scrumNumDialog;

public class ProjectDetailActivity extends AppCompatActivity {
    static final int COUNT = 15;//자바 상수 선언

    static int projNum;
    Button[] bt1;
    int bt1ScrumNum[];
    Button[] bt2;
    int bt2ScrumNum[];
    Button[] bt3;
    int bt3ScrumNum[];

    RatingBar reviewPoint;
    EditText reviewText;
    Button reviewButton;
    ProgressBar progressBar;


    RatingBar projScore;
    TextView projScoreInt;
    TextView kind;
    TextView pname;
    ImageView img;
    TextView intro;
    TextView deadline;
    TextView current;
    TextView fundgoal;

    ListView commentListView;
    ArrayList<CommentData> commentData;
    CommentAdapter commentAdapter;


    // 스크럼 불러오기
    ArrayList<ShowScrumListResponse> scrumListResponseArrayList;
    static String scrum_name;
    static String scrum_intro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);


        reviewPoint = (RatingBar) findViewById(R.id.reviewPoint);
        reviewText = (EditText) findViewById(R.id.reviewText);
        reviewButton = (Button) findViewById(R.id.reviewButton);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        projScore = (RatingBar) findViewById(R.id.score);
        projScoreInt = (TextView) findViewById(R.id.scoreInt);
        kind = (TextView) findViewById(R.id.kind);
        pname = (TextView) findViewById(R.id.pName);
        img = (ImageView) findViewById(R.id.img);
        intro = (TextView) findViewById(R.id.intro);
        deadline = (TextView) findViewById(R.id.deadline);
        current = (TextView) findViewById(R.id.current);
        fundgoal = (TextView) findViewById(R.id.fundGoal);


        progressBar.setProgress(25);

        // 평가, 댓글에 작성하기 기능 구현


        Intent intent = getIntent();
        projNum = intent.getIntExtra("projNum", -1);
        if (projNum != -1) {

            loadScrum();


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
                    projScore.setRating(result.getScore());
                    projScoreInt.setText(String.valueOf(result.getScore()));
                    kind.setText(result.getCategory());
                    pname.setText(result.getPname());
                    Picasso.with(getApplicationContext())
                            .load(result.getImage())
                            .into(img);
                    Log.v("img", result.getImage() + "");
                    intro.setText(result.getIntroduction());
                    deadline.setText("프로젝트 마감일 : " + result.getDeadline());
                    current.setText("현재 모금액 : " + result.getCurrent());
                    fundgoal.setText("목표 모금액 : " + result.getFundgoal());

                    commentLoad(); // 댓글 보여준다

//                     else{
//                        Toast.makeText(getApplicationContext(), result.getStr(), Toast.LENGTH_LONG).show();
//                    }
                }
            }.execute(projNum);
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

    private void commentLoad() {
        commentListView = (ListView) findViewById(R.id.comment);

        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );
        LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
                Regions.AP_NORTHEAST_2, cognitoProvider);

        final ShowCommentListInterface showCommentListInterface = factory.build(ShowCommentListInterface.class,
                new LambdaDataListBinder(new TypeToken<ArrayList<CommentData>>() {
                }.getType()));

        new AsyncTask<Integer, Void, ArrayList<CommentData>>() {
            private MyError myError = null;


            @Override
            protected ArrayList<CommentData> doInBackground(Integer... params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    return showCommentListInterface.addingShowCommentList(params[0]);
                } catch (LambdaFunctionException lfe) {
                    if (lfe.getMessage().equals("Handled")) {
                        myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                    }
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ArrayList<CommentData> result) {
                if (result == null) {
                    if (myError != null) {
                        Log.e("myerror", myError.getErrorMessage());
                    }
                    return;
                }

                Log.v("리저트", result + "");
                Log.v("리저트", projNum + "");
                CommentAdapter commentAdapter = new CommentAdapter(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, result);
                commentListView.setAdapter(commentAdapter);
            }
        }.execute(projNum);

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
                    bt1[bt1fill++].setBackgroundColor(getApplication().getResources().getColor(R.color.scrumColor0));
                    break;
                case 1:
                    bt2ScrumNum[bt2fill] = scrumListResponseArrayList.get(i).getSnum();
                    bt2[bt2fill++].setBackgroundColor(getApplication().getResources().getColor(R.color.scrumColor1));
//                    bt2[bt2fill++].setBackgroundColor(Color.parseColor("#ffbf7bc4"));
                    break;
                case 2:
                    bt3ScrumNum[bt3fill] = scrumListResponseArrayList.get(i).getSnum();
                    bt3[bt3fill++].setBackgroundColor(getApplication().getResources().getColor(R.color.scrumColor2));
//                    bt3[bt3fill++].setBackgroundColor(Color.parseColor("#ffb559bc"));
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
                            scrumNumDialog = bt1ScrumNum[finalI];

                            ShowDialog showDialog = new ShowDialog(v.getContext(), result.getFile());
                            showDialog.show();

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
                            scrumNumDialog = bt2ScrumNum[finalI];

                            ShowDialog showDialog = new ShowDialog(v.getContext(), result.getFile());
                            showDialog.show();

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
                            scrumNumDialog = bt3ScrumNum[finalI];

                            ShowDialog showDialog = new ShowDialog(v.getContext(), result.getFile());
                            showDialog.show();
                        }
                    }.execute(bt3ScrumNum[finalI]);

                }
            });
        }


    }


    public void fundThisProj(View view) {
        if (user == null) {
            Toast.makeText(this, "로그인을 해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        PayDialog payDialog = new PayDialog(this);
        payDialog.show();
    }

    public void reviewBtnClicked(View view) {
        if (user == null) {// 로그인이 됐을 때만 실행
            Toast.makeText(this, "로그인을 해주세요", Toast.LENGTH_SHORT).show();
            return;
        }


        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );
        LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
                Regions.AP_NORTHEAST_2, cognitoProvider);

        final ReviewRegisterInterface reviewRegisterInterface = factory.build(ReviewRegisterInterface.class);

        final ReviewRequest request = new ReviewRequest(user.getEmail(), projNum, (int) (reviewPoint.getRating() * 2), reviewText.getText().toString());

        new AsyncTask<ReviewRequest, Void, Boolean>() {
            private MyError myError = null;


            @Override
            protected Boolean doInBackground(ReviewRequest... params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    return reviewRegisterInterface.addingRegisterEvaluation(params[0]);
                } catch (LambdaFunctionException lfe) {
                    if (lfe.getMessage().equals("Handled")) {
                        myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                    }
                    return false;
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
                commentLoad(); // 댓글 보여준다
                Toast.makeText(getApplicationContext(), "리뷰가 작성되었습니다", Toast.LENGTH_LONG).show();
                reviewPoint.setRating(0.0f);
                reviewText.setText("");

            }
        }.execute(request);

    }

    public void closeBtnClicked(View view) {
        finish();
    }


}
