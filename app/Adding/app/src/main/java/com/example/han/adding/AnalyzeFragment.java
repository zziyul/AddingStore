package com.example.han.adding;


import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.han.adding.AnalActivity.projNum;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnalyzeFragment extends Fragment {

    Spinner sub;
    Spinner spinner;
    AdapterSpinner mainSpinner;
    AdapterSpinner subSpinner;
    ArrayList<String> mainItem;
    final String[] mainArray = {"직업군", "취미", "성별", "나이대"};
    final String[] jobArray = {"예술", "사무직", "자영업", "서비스직", "생산직", "농축산", "학생", "기타"};
    final String[] ageArray = {"10대", "20대", "30대", "40대", "5/60대"};
    final String[] favArray = {"건강", "게임", "교육", "금융", "쇼핑", "스포츠", "소셜", "미디어"};
    final String[] genderArray = {"남", "여"};
    LinearLayout job;
    LinearLayout age;
    LinearLayout fav;
    LinearLayout gender;

    int check = 0;//현재 어떠한 정보가 위에서 선택되었는지 체크하는 변수
    private BarChart favChart;
    private PieChart pieChart;
    private PieChart secondChart;
    private BarChart barChart;

    ListView commentListView;
    ArrayList<CommentAnalData> commentList;
    int bigcutting;
    private String cutting;


    public AnalyzeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_analyze, container, false);

        setChart(v);

        mainItem = new ArrayList<>();
        spinner = (Spinner) v.findViewById(R.id.mainSpinner);
        sub = (Spinner) v.findViewById(R.id.sub);
        job = (LinearLayout) v.findViewById(R.id.job);
        age = (LinearLayout) v.findViewById(R.id.age);
        fav = (LinearLayout) v.findViewById(R.id.fav);
        gender = (LinearLayout) v.findViewById(R.id.gender);

        commentLoad(v);

        Collections.addAll(mainItem, mainArray);
        mainSpinner = new AdapterSpinner(getActivity(), mainItem);
        spinner.setAdapter(mainSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> subItem;
                subItem = new ArrayList<>();
                String str = parent.getItemAtPosition(position).toString();
                switch (str) {
                    case "직업군":
                        bigcutting = 0;

                        check = 1;
                        job.setVisibility(View.VISIBLE);
                        fav.setVisibility(View.GONE);
                        age.setVisibility(View.GONE);
                        gender.setVisibility(View.GONE);
                        Collections.addAll(subItem, jobArray);
                        subSpinner = new AdapterSpinner(getActivity(), subItem);
                        sub.setAdapter(subSpinner);

                        sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Log.v("보자", parent.getItemAtPosition(position) + "");
                                cutting = parent.getItemAtPosition(position).toString();

                                commentLoad(v);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Log.v("보자", parent.getItemAtPosition(0) + "");
                                cutting = parent.getItemAtPosition(0).toString();
                                commentLoad(v);

                            }
                        });
                        break;
                    case "취미":
                        bigcutting = 1;

                        check = 2;
                        job.setVisibility(View.GONE);
                        fav.setVisibility(View.VISIBLE);
                        age.setVisibility(View.GONE);
                        gender.setVisibility(View.GONE);

                        Collections.addAll(subItem, favArray);
                        subSpinner = new AdapterSpinner(getActivity(), subItem);
                        sub.setAdapter(subSpinner);
                        break;
                    case "성별":
                        bigcutting = 2;

                        check = 3;
                        job.setVisibility(View.GONE);
                        fav.setVisibility(View.GONE);
                        age.setVisibility(View.GONE);
                        gender.setVisibility(View.VISIBLE);

                        Collections.addAll(subItem, genderArray);
                        subSpinner = new AdapterSpinner(getActivity(), subItem);
                        sub.setAdapter(subSpinner);
                        break;
                    case "나이대":
                        bigcutting = 3;

                        check = 4;
                        job.setVisibility(View.GONE);
                        fav.setVisibility(View.GONE);
                        age.setVisibility(View.VISIBLE);
                        gender.setVisibility(View.GONE);

                        Collections.addAll(subItem, ageArray);
                        subSpinner = new AdapterSpinner(getActivity(), subItem);
                        sub.setAdapter(subSpinner);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bigcutting = 0;
                ArrayList<String> subItem;
                subItem = new ArrayList<>();
                check = 1;
                job.setVisibility(View.VISIBLE);
                fav.setVisibility(View.GONE);
                age.setVisibility(View.GONE);
                gender.setVisibility(View.GONE);

                Collections.addAll(subItem, jobArray);
                subSpinner = new AdapterSpinner(getActivity(), subItem);
                sub.setAdapter(subSpinner);
            }
        });
        return v;
    }

    private void setChart(View v) {

        pieChart = (PieChart) v.findViewById(R.id.piechart);
        secondChart = (PieChart) v.findViewById(R.id.secondchart);
        barChart = (BarChart) v.findViewById(R.id.bar);
        favChart = (BarChart) v.findViewById(R.id.favorite);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        yValues.add(new PieEntry(34f, "10대"));
        yValues.add(new PieEntry(23f, "20대"));
        yValues.add(new PieEntry(14f, "30대"));
        yValues.add(new PieEntry(35f, "40대"));
        yValues.add(new PieEntry(40f, "5/60대"));

        Description description = new Description();
        description.setText("연령 분포"); //라벨
        description.setTextSize(15);
        pieChart.setDescription(description);

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues, "age");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
        secondChart.setUsePercentValues(true);
        secondChart.getDescription().setEnabled(false);
        secondChart.setExtraOffsets(5, 10, 5, 5);

        secondChart.setDragDecelerationFrictionCoef(0.95f);

        secondChart.setDrawHoleEnabled(false);
        secondChart.setHoleColor(Color.WHITE);
        secondChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> gValues = new ArrayList<PieEntry>();

        gValues.add(new PieEntry(34f, "여성"));
        gValues.add(new PieEntry(23f, "남성"));

        Description gdescription = new Description();
        gdescription.setText("성별 분포"); //라벨
        gdescription.setTextSize(15);
        secondChart.setDescription(gdescription);

        secondChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet gdataSet = new PieDataSet(gValues, "gender");
        gdataSet.setSliceSpace(3f);
        gdataSet.setSelectionShift(5f);
        gdataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData gdata = new PieData((gdataSet));
        gdata.setValueTextSize(10f);
        gdata.setValueTextColor(Color.YELLOW);

        secondChart.setData(gdata);


        ArrayList job_entries = new ArrayList();
        job_entries.add(new BarEntry(1f, 3));
        job_entries.add(new BarEntry(2f, 2));
        job_entries.add(new BarEntry(3f, 7));
        job_entries.add(new BarEntry(4f, 13));
        job_entries.add(new BarEntry(5f, 7));
        job_entries.add(new BarEntry(6f, 4));
        job_entries.add(new BarEntry(7f, 6));
        job_entries.add(new BarEntry(8f, 9));

        ArrayList job_entries2 = new ArrayList();
        job_entries2.add(new BarEntry(1f, 8));
        job_entries2.add(new BarEntry(2f, 7));
        job_entries2.add(new BarEntry(3f, 6));
        job_entries2.add(new BarEntry(4f, 5));
        job_entries2.add(new BarEntry(5f, 4));
        job_entries2.add(new BarEntry(6f, 3));
        job_entries2.add(new BarEntry(7f, 2));
        job_entries2.add(new BarEntry(8f, 1));


        ArrayList<String> labels = new ArrayList<String>();
        labels.add("예술");
        labels.add("사무직");
        labels.add("자영업");
        labels.add("서비스직");
        labels.add("생산직");
        labels.add("농축산");
        labels.add("학생");
        labels.add("기타");

        BarDataSet jov_dataset = new BarDataSet(job_entries, "투자자수");
        jov_dataset.setColors(ColorTemplate.PASTEL_COLORS);
        BarDataSet jov_dataset2 = new BarDataSet(job_entries2, "총투자액");
        jov_dataset2.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData job_dt = new BarData(jov_dataset, jov_dataset2);
        job_dt.setValueFormatter(new LargeValueFormatter());

        float barWidth = 0.3f;
        float barSpace = 0f;
        float groupSpace = 0.4f;
        barChart.setData(job_dt);
        barChart.getBarData().setBarWidth(barWidth);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * job_entries.size());
        barChart.groupBars(0, groupSpace, barSpace);
        barChart.getData().setHighlightEnabled(false);
        barChart.invalidate();

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(20f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(labels.size());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getAxisRight().setEnabled(false);

        barChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);


        ArrayList fav_labels = new ArrayList();

        fav_labels.add("건강");
        fav_labels.add("게임");
        fav_labels.add("교육");
        fav_labels.add("금융");
        fav_labels.add("쇼핑");
        fav_labels.add("스포츠");
        fav_labels.add("소셜");
        fav_labels.add("미디어");


        ArrayList fav_entries = new ArrayList();
        fav_entries.add(new BarEntry(1f, 3));
        fav_entries.add(new BarEntry(2f, 2));
        fav_entries.add(new BarEntry(3f, 7));
        fav_entries.add(new BarEntry(4f, 13));
        fav_entries.add(new BarEntry(5f, 7));
        fav_entries.add(new BarEntry(6f, 4));
        fav_entries.add(new BarEntry(7f, 6));
        fav_entries.add(new BarEntry(8f, 9));

        ArrayList fav_entries2 = new ArrayList();
        fav_entries2.add(new BarEntry(1f, 0));
        fav_entries2.add(new BarEntry(2f, 1));
        fav_entries2.add(new BarEntry(3f, 2));
        fav_entries2.add(new BarEntry(4f, 3));
        fav_entries2.add(new BarEntry(5f, 4));
        fav_entries2.add(new BarEntry(6f, 5));
        fav_entries2.add(new BarEntry(7f, 6));

        BarDataSet fav_dataset = new BarDataSet(fav_entries, "투자자수");
        fav_dataset.setColors(ColorTemplate.PASTEL_COLORS);
        BarDataSet fav_dataset2 = new BarDataSet(fav_entries2, "총투자액");
        fav_dataset2.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData fv_dt = new BarData(fav_dataset, fav_dataset2);
        fv_dt.setValueFormatter(new LargeValueFormatter());


        favChart.setData(fv_dt);
        favChart.getBarData().setBarWidth(barWidth);
        favChart.getXAxis().setAxisMinimum(0);
        favChart.getXAxis().setAxisMaximum(0 + favChart.getBarData().getGroupWidth(groupSpace, barSpace) * fav_entries.size());
        favChart.groupBars(0, groupSpace, barSpace);
        favChart.getData().setHighlightEnabled(false);
        favChart.invalidate();

        Legend ll = favChart.getLegend();
        ll.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        ll.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        ll.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        ll.setDrawInside(true);
        ll.setYOffset(20f);
        ll.setXOffset(0f);
        ll.setYEntrySpace(0f);
        ll.setTextSize(8f);

        XAxis xxAxis = favChart.getXAxis();
        xxAxis.setGranularity(1f);
        xxAxis.setGranularityEnabled(true);
        xxAxis.setCenterAxisLabels(true);
        xxAxis.setDrawGridLines(false);
        xxAxis.setAxisMaximum(fav_labels.size());
        xxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xxAxis.setValueFormatter(new IndexAxisValueFormatter(fav_labels));
        favChart.getAxisRight().setEnabled(false);

        favChart.getAxisRight().setEnabled(false);
        YAxis lleftAxis = favChart.getAxisLeft();
        lleftAxis.setValueFormatter(new LargeValueFormatter());
        lleftAxis.setDrawGridLines(true);
        lleftAxis.setSpaceTop(35f);
        lleftAxis.setAxisMinimum(0f);

    }

    private void commentLoad(View v) {


        commentListView = (ListView) v.findViewById(R.id.commentList);

        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                getActivity(),
                "ap-northeast-2:a03da40d-6e0a-40f9-9510-6a5fb5c4a37a", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );
        LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getActivity(),
                Regions.AP_NORTHEAST_2, cognitoProvider);

        final ShowAnalCommentListInterface showAnalCommentListInterface = factory.build(ShowAnalCommentListInterface.class,
                new LambdaDataListBinder(new TypeToken<ArrayList<CommentAnalData>>() {
                }.getType()));

        new AsyncTask<Integer, Void, ArrayList<CommentAnalData>>() {
            private MyError myError = null;

            @Override
            protected ArrayList<CommentAnalData> doInBackground(Integer... params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    return showAnalCommentListInterface.addingshowfundingmemberlist(params[0]);
                } catch (LambdaFunctionException lfe) {
                    if (lfe.getMessage().equals("Handled")) {
                        myError = new Gson().fromJson(lfe.getDetails(), MyError.class);
                    }
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ArrayList<CommentAnalData> result) {
                if (result == null) {
                    if (myError != null) {
                        Log.e("myerror", myError.getErrorMessage());
                    }
                    return;
                }
                ArrayList<CommentAnalData> real = new ArrayList<>();

                for (int i = 0; i < result.size(); i++) {
                    if (bigcutting == 0) {
                        if (result.get(i).getJob().equals(cutting)) {
                            real.add(result.get(i));
                        }
                    } else if (bigcutting == 1) {
                        if (result.get(i).getFavorite().equals(cutting)) {
                            real.add(result.get(i));
                        }
                    } else if (bigcutting == 2) {
                        String s;
                        if (result.get(i).getGender().toString().equals("0")) {
                            s = "남";
                        } else {
                            s = "여";
                        }
                        if (s.equals(cutting)) {
                            real.add(result.get(i));
                        }
                        Log.v("성별로그", cutting);
                    } else if (bigcutting == 3) {
                        real.add(result.get(i));
                    }
                }

                CommentAnalAdapter commentAdapter = new CommentAnalAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, real);
                commentListView.setAdapter(commentAdapter);

                commentList = new ArrayList<>(result);
            }
        }.execute(projNum);
    }
}
