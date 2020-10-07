package com.example.han.adding;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.han.adding.MainActivity.user;


/**
 * A simple {@link Fragment} subclass.
 */
public class CashFragment extends Fragment {

    LinearLayout chargeLayout;
    LinearLayout exchangeLayout;

    public CashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cash, container, false);
        chargeLayout = (LinearLayout) v.findViewById(R.id.charge);
        exchangeLayout = (LinearLayout) v.findViewById(R.id.exchange);
        chargeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    Toast.makeText(getActivity(), "로그인 해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(getActivity(), ChargeActivity.class));
            }
        });
        exchangeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    Toast.makeText(getActivity(), "로그인 해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(getActivity(), ExchangeActivity.class));
            }
        });

        TextView intro = (TextView) v.findViewById(R.id.intro);
        intro.setText("안녕하세요 " + user.getName() + "님");


        return v;
    }

}
