package com.example.han.adding;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import static com.example.han.adding.AnalActivity.projNum;
import static com.example.han.adding.ShowDialog.scrumNumDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScrumFragment extends Fragment {

    static final int COUNT = 15;//자바 상수 선언
    Button[] bt1;
    int bt1ScrumNum[];
    Button[] bt2;
    int bt2ScrumNum[];
    Button[] bt3;
    int bt3ScrumNum[];

    // 스크럼 불러오기
    ArrayList<ShowScrumListResponse> scrumListResponseArrayList;
    ListView ScrumContentList;

    static String scrum_name;
    static String scrum_intro;

    public ScrumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_scrum, container, false);

        loadScrum(v); // 스크럼 정보 불러오기

        ScrumContentList = (ListView) v.findViewById(R.id.ScrumContentList);

        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                getActivity().getApplicationContext(),
                "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );
        LambdaInvokerFactory factory = new LambdaInvokerFactory(getActivity().getApplicationContext(),
                Regions.AP_NORTHEAST_2, cognitoProvider);

        final ScrumErrorListInterface scrumErrorListInterface = factory.build(ScrumErrorListInterface.class,
                new LambdaDataListBinder(new TypeToken<ArrayList<ScrumErrorList>>() {
                }.getType()));

        new AsyncTask<Integer, Void, ArrayList<ScrumErrorList>>() {
            private MyError myError = null;

            @Override
            protected ArrayList<ScrumErrorList> doInBackground(Integer... params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    return scrumErrorListInterface.addingshowsinquiry(params[0]);
                } catch (LambdaFunctionException lfe) {
                    if (lfe.getMessage().equals("Handled")) {
                        myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                    }
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ArrayList<ScrumErrorList> result) {
                if (result == null) {
                    if (myError != null) {
                        Log.e("myerror", myError.getErrorMessage());
                    }
                    return;
                }

                ScrumErrorListAdapter scrumErrorListAdapter = new ScrumErrorListAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, result);
                ScrumContentList.setAdapter(scrumErrorListAdapter);
            }
        }.execute(projNum);


        return v;
    }

    private void loadScrum(final View v) {
        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                getActivity().getApplicationContext(),
                "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );
        LambdaInvokerFactory factory = new LambdaInvokerFactory(getActivity().getApplicationContext(),
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
                //     ScrumContentList.setAdapter();

                initButton(v);


            }
        }.execute(projNum);
    }

    private void initButton(View v) {
        // 개발할 기능
        bt1 = new Button[COUNT];
        bt1ScrumNum = new int[COUNT];
        bt1[0] = (Button) v.findViewById(R.id.bt1_1);
        bt1[1] = (Button) v.findViewById(R.id.bt1_2);
        bt1[2] = (Button) v.findViewById(R.id.bt1_3);
        bt1[3] = (Button) v.findViewById(R.id.bt1_4);
        bt1[4] = (Button) v.findViewById(R.id.bt1_5);
        bt1[5] = (Button) v.findViewById(R.id.bt1_6);
        bt1[6] = (Button) v.findViewById(R.id.bt1_7);
        bt1[7] = (Button) v.findViewById(R.id.bt1_8);
        bt1[8] = (Button) v.findViewById(R.id.bt1_9);
        bt1[9] = (Button) v.findViewById(R.id.bt1_10);
        bt1[10] = (Button) v.findViewById(R.id.bt1_11);
        bt1[11] = (Button) v.findViewById(R.id.bt1_12);
        bt1[12] = (Button) v.findViewById(R.id.bt1_13);
        bt1[13] = (Button) v.findViewById(R.id.bt1_14);
        bt1[14] = (Button) v.findViewById(R.id.bt1_15);


        // 개발 중인 기능
        bt2 = new Button[COUNT];
        bt2ScrumNum = new int[COUNT];
        bt2[0] = (Button) v.findViewById(R.id.bt2_1);
        bt2[1] = (Button) v.findViewById(R.id.bt2_2);
        bt2[2] = (Button) v.findViewById(R.id.bt2_3);
        bt2[3] = (Button) v.findViewById(R.id.bt2_4);
        bt2[4] = (Button) v.findViewById(R.id.bt2_5);
        bt2[5] = (Button) v.findViewById(R.id.bt2_6);
        bt2[6] = (Button) v.findViewById(R.id.bt2_7);
        bt2[7] = (Button) v.findViewById(R.id.bt2_8);
        bt2[8] = (Button) v.findViewById(R.id.bt2_9);
        bt2[9] = (Button) v.findViewById(R.id.bt2_10);
        bt2[10] = (Button) v.findViewById(R.id.bt2_11);
        bt2[11] = (Button) v.findViewById(R.id.bt2_12);
        bt2[12] = (Button) v.findViewById(R.id.bt2_13);
        bt2[13] = (Button) v.findViewById(R.id.bt2_14);
        bt2[14] = (Button) v.findViewById(R.id.bt2_15);


        // 개발한 기능
        bt3 = new Button[COUNT];
        bt3ScrumNum = new int[COUNT];
        bt3[0] = (Button) v.findViewById(R.id.bt3_1);
        bt3[1] = (Button) v.findViewById(R.id.bt3_2);
        bt3[2] = (Button) v.findViewById(R.id.bt3_3);
        bt3[3] = (Button) v.findViewById(R.id.bt3_4);
        bt3[4] = (Button) v.findViewById(R.id.bt3_5);
        bt3[5] = (Button) v.findViewById(R.id.bt3_6);
        bt3[6] = (Button) v.findViewById(R.id.bt3_7);
        bt3[7] = (Button) v.findViewById(R.id.bt3_8);
        bt3[8] = (Button) v.findViewById(R.id.bt3_9);
        bt3[9] = (Button) v.findViewById(R.id.bt3_10);
        bt3[10] = (Button) v.findViewById(R.id.bt3_11);
        bt3[11] = (Button) v.findViewById(R.id.bt3_12);
        bt3[12] = (Button) v.findViewById(R.id.bt3_13);
        bt3[13] = (Button) v.findViewById(R.id.bt3_14);
        bt3[14] = (Button) v.findViewById(R.id.bt3_15);

        int bt1fill = 0;
        int bt2fill = 0;
        int bt3fill = 0;
        Log.v("리스트", scrumListResponseArrayList.size() + "");
        for (int i = 0; i < scrumListResponseArrayList.size(); i++) {
            switch (scrumListResponseArrayList.get(i).getState()) {
                case 0:
                    bt1ScrumNum[bt1fill] = scrumListResponseArrayList.get(i).getSnum();
                    bt1[bt1fill++].setBackgroundColor(getResources().getColor(R.color.scrumColor0));
                    break;
                case 1:
                    bt2ScrumNum[bt2fill] = scrumListResponseArrayList.get(i).getSnum();
                    bt2[bt2fill++].setBackgroundColor(getResources().getColor(R.color.scrumColor1));
//                    bt2[bt2fill++].setBackgroundColor(Color.parseColor("#ffbf7bc4"));
                    break;
                case 2:
                    bt3ScrumNum[bt3fill] = scrumListResponseArrayList.get(i).getSnum();
                    bt3[bt3fill++].setBackgroundColor(getResources().getColor(R.color.scrumColor2));
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
                            getActivity(),
                            "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                            Regions.AP_NORTHEAST_2 // 리전
                    );
                    LambdaInvokerFactory factory = new LambdaInvokerFactory(getActivity(),
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

                            ShowAnalDialog showDialog = new ShowAnalDialog(v.getContext(), result.getFile());
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
                            getActivity(),
                            "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                            Regions.AP_NORTHEAST_2 // 리전
                    );
                    LambdaInvokerFactory factory = new LambdaInvokerFactory(getActivity(),
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

                            ShowAnalDialog showDialog = new ShowAnalDialog(v.getContext(), result.getFile());
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
                            getActivity(),
                            "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                            Regions.AP_NORTHEAST_2 // 리전
                    );
                    LambdaInvokerFactory factory = new LambdaInvokerFactory(getActivity(),
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

                            ShowAnalDialog showDialog = new ShowAnalDialog(v.getContext(), result.getFile());
                            showDialog.show();
                        }
                    }.execute(bt3ScrumNum[finalI]);

                }
            });
        }


    }


}
