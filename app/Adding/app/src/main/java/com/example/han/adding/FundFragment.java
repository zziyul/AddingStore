package com.example.han.adding;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class FundFragment extends Fragment {

    LinearLayout searchBox;
    //  int layoutWidth = 1000;
    //   int layoutHeight = 140;
    final String[] favoItemArray = {"건강", "게임", "교육", "금융", "쇼핑", "스포츠", "미디어", "소셜"};
    private Spinner favorite;
    private ArrayList<String> favoItem;
    private AdapterSpinner1 favoAdapter;
    private ImageButton sButton;
    private EditText keyword;
    String selectedFavorite;


    ArrayList<PreviewClassFund> serchResult;
    private ListView listView;
    private FundListAdapter fundListAdapter;

    public FundFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_fund, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //load();
        searchBox = (LinearLayout) getActivity().findViewById(R.id.searchBox);
        //  searchBox.setLayoutParams(new LinearLayout.LayoutParams(layoutWidth, 0));
        searchBox.setVisibility(View.GONE);

        favorite = (Spinner) getActivity().findViewById(R.id.favorite);
        favoItem = new ArrayList<>();
        Collections.addAll(favoItem, favoItemArray);
        favoAdapter = new AdapterSpinner1(getActivity(), favoItem);
        favorite.setAdapter(favoAdapter);
        favorite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFavorite = parent.getItemAtPosition(position).toString();
                load();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // 검색 버튼 누를시
        sButton = (ImageButton) getActivity().findViewById(R.id.searchButton);
        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = (EditText) getActivity().findViewById(R.id.keyword);
                keyword.getText();


                //검색 결과 보여주기
                serchResult = new ArrayList<PreviewClassFund>();
                listView = (ListView) getActivity().findViewById(R.id.listView);
                fundListAdapter = new FundListAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, serchResult);
                listView.setAdapter(fundListAdapter);
            }
        });
    }

    private void load() {

        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                getActivity().getApplicationContext(),
                "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );
        LambdaInvokerFactory factory = new LambdaInvokerFactory(getActivity().getApplicationContext(),
                Regions.AP_NORTHEAST_2, cognitoProvider);
        Log.i("asdfgh", factory + "");

        final ShowingFundListInterface showingFundListInterface = factory.build(ShowingFundListInterface.class,
                new LambdaDataListBinder(new TypeToken<ArrayList<PreviewClassFund>>() {
                }.getType()));

        Log.v("fdsa", selectedFavorite.toString());
        final FundRequestClass request = new FundRequestClass(selectedFavorite, "", 0, 20);
        new AsyncTask<FundRequestClass, Void, ArrayList<PreviewClassFund>>() {
            private MyError myError = null;

            @Override
            protected ArrayList<PreviewClassFund> doInBackground(FundRequestClass... params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    return showingFundListInterface.addingShowProject(params[0]);
                } catch (LambdaFunctionException lfe) {
                    if (lfe.getMessage().equals("Handled")) {
                        myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                    }
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ArrayList<PreviewClassFund> result) {
                if (result == null) {
                    if (myError != null) {
                        Log.e("myerror", myError.getErrorMessage());
                    }
                    return;
                }

                FundListAdapter fundListAdapter = new FundListAdapter(getActivity(), R.layout.fund_list_row, result);
                listView = (ListView) getActivity().findViewById(R.id.listView);
                listView.setAdapter(fundListAdapter);

                Log.v("asdf", result.get(0).getPname() + "");

            }
        }.execute(request);
    }

    public void showSearch() {
        //  searchBox.setLayoutParams(new LinearLayout.LayoutParams(layoutWidth, layoutHeight));
        searchBox.setVisibility(View.VISIBLE);
        //   Log.v("asdf", layoutWidth + " " + layoutHeight);
    }
}
