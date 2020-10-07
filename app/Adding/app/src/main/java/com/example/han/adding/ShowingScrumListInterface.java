package com.example.han.adding;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

import java.util.ArrayList;

public interface ShowingScrumListInterface {
    @LambdaFunction
    ArrayList<ShowScrumListResponse> addingshowScrum(Integer request);
}
