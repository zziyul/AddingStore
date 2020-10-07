package com.example.han.adding;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CastFragment extends Fragment {

    ListView listView;

    public CastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cast, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        load();


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

        final ShowingCastListInterface showingCastListInterface = factory.build(ShowingCastListInterface.class, new LambdaDataListBinder(new TypeToken<ArrayList<PreviewClass>>() {
        }.getType()));

        final CastRequestClass request = new CastRequestClass(0, 20);
        new AsyncTask<CastRequestClass, Void, ArrayList<PreviewClass>>() {
            private MyError myError = null;

            @Override
            protected ArrayList<PreviewClass> doInBackground(CastRequestClass... params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    return showingCastListInterface.addingShowCast(params[0]);
                } catch (LambdaFunctionException lfe) {
                    if (lfe.getMessage().equals("Handled")) {
                        myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                    }
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ArrayList<PreviewClass> result) {
                if (result == null) {
                    if (myError != null) {
                        Log.e("myerror", myError.getErrorMessage());
                    }
                    return;
                }

                CastListAdapter castListAdapter = new CastListAdapter(getActivity(), R.layout.fund_list_row, result);
                listView = (ListView) getActivity().findViewById(R.id.listView);
                listView.setAdapter(castListAdapter);

            }
        }.execute(request);
    }
}
