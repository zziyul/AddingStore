package com.example.han.adding;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import static com.example.han.adding.MainActivity.user;

public class CastDetailActivity extends AppCompatActivity {

    TextView title;
    TextView date;
    ImageView image;
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_detail);

        title = (TextView) findViewById(R.id.title);
        date = (TextView) findViewById(R.id.date);
        image = (ImageView) findViewById(R.id.image);
        content = (TextView) findViewById(R.id.content);


        Intent intent = getIntent();
        int castNum = intent.getIntExtra("castNum", -1);
        if (castNum != -1) {
            CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                    Regions.AP_NORTHEAST_2 // 리전
            );
            LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
                    Regions.AP_NORTHEAST_2, cognitoProvider);

            final ShowingCastDetailInterface showingCastDetailInterface = factory.build(ShowingCastDetailInterface.class);

            CastDetailRequestClass request = new CastDetailRequestClass(castNum);

            new AsyncTask<Integer, Void, CastDetailResponseClass>() {
                private MyError myError = null;

                @Override
                protected CastDetailResponseClass doInBackground(Integer... params) {
                    // invoke "echo" method. In case it fails, it will throw a
                    // LambdaFunctionException.
                    try {
                        return showingCastDetailInterface.addingShowCastDetail(params[0]);
                    } catch (LambdaFunctionException lfe) {
                        if (lfe.getMessage().equals("Handled")) {
                            myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                        }
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(CastDetailResponseClass result) {
                    if (result == null) {
                        if (myError != null) {
                            Log.e("myerror", myError.getErrorMessage());
                        }
                        return;
                    }


                    title.setText(result.getTitle());
                    date.setText(result.getDate());
                    Picasso.with(getApplicationContext())
                            .load(result.getImage())
                            .into(image);
                    content.setText(result.getContent());

//                    else {
//                        Toast.makeText(getApplicationContext(), result.getStr(), Toast.LENGTH_LONG).show();
//                    }
                }
            }.execute(castNum);
        }
    }

    public void close(View view) {
        finish();
    }
}
