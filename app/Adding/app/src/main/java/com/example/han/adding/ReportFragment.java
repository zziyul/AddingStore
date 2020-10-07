package com.example.han.adding;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import static com.example.han.adding.MainActivity.user;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {

    TextView registerProj;

    ArrayList<String> fundList; // 임시적으로 String이라고 함
    ListView fundListView;
    ReportFundListAdapter reportFundListAdapter;


    ArrayList<Integer> myList; // 임시적으로 String이라고 함
    ListView myListView;
    ReportMyListAdapter reportMyListAdapter;


    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        registerProj = (TextView) getActivity().findViewById(R.id.registerProj);
        registerProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    Toast.makeText(getActivity(), "로그인 해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivity(new Intent(getActivity(), ProjectRegister.class));
            }
        });

        if (user != null) { // 로그인 햇다면
            // myList 가져오기
            CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                    getActivity().getApplicationContext(),
                    "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                    Regions.AP_NORTHEAST_2 // 리전
            );
            LambdaInvokerFactory factory = new LambdaInvokerFactory(getActivity().getApplicationContext(),
                    Regions.AP_NORTHEAST_2, cognitoProvider);
            Log.i("asdfgh", factory + "");

            final ShowMyProjListInterface showMyProjListInterface = factory.build(ShowMyProjListInterface.class,
                    new LambdaDataListBinder(new TypeToken<ArrayList<PreviewMyProjClass>>() {
                    }.getType()));

            new AsyncTask<String, Void, ArrayList<PreviewMyProjClass>>() {
                private MyError myError = null;

                @Override
                protected ArrayList<PreviewMyProjClass> doInBackground(String... params) {
                    // invoke "echo" method. In case it fails, it will throw a
                    // LambdaFunctionException.
                    try {
                        return showMyProjListInterface.addingShowMyProject(params[0]);
                    } catch (LambdaFunctionException lfe) {
                        if (lfe.getMessage().equals("Handled")) {
                            myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                        }
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(ArrayList<PreviewMyProjClass> result) {
                    if (result == null) {
                        if (myError != null) {
                            Log.e("myerror", myError.getErrorMessage());
                        }
                        return;
                    }

                    reportMyListAdapter = new ReportMyListAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, result);
                    myListView = (ListView) getActivity().findViewById(R.id.myListView);
                    myListView.setAdapter(reportMyListAdapter);
                }
            }.execute(user.getEmail());


            // ListView 연결
            final ShowFundProjListInterface showFundProjListInterface = factory.build(ShowFundProjListInterface.class,
                    new LambdaDataListBinder(new TypeToken<ArrayList<PreviewMyProjClass>>() {
                    }.getType()));

            new AsyncTask<String, Void, ArrayList<PreviewMyProjClass>>() {
                private MyError myError = null;

                @Override
                protected ArrayList<PreviewMyProjClass> doInBackground(String... params) {
                    // invoke "echo" method. In case it fails, it will throw a
                    // LambdaFunctionException.
                    try {
                        return showFundProjListInterface.addingSponsor(params[0]);
                    } catch (LambdaFunctionException lfe) {
                        if (lfe.getMessage().equals("Handled")) {
                            myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                        }
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(ArrayList<PreviewMyProjClass> result) {
                    if (result == null) {
                        if (myError != null) {
                            Log.e("myerror", myError.getErrorMessage());
                        }
                        return;
                    }

                    reportFundListAdapter = new ReportFundListAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, result);
                    fundListView = (ListView) getActivity().findViewById(R.id.fundListView);
                    fundListView.setAdapter(reportFundListAdapter);
                }
            }.execute(user.getEmail());

        }

    }
}
