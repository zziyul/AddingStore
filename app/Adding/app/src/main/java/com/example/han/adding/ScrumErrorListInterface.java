package com.example.han.adding;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

import java.util.ArrayList;

public interface ScrumErrorListInterface {
    @LambdaFunction
    ArrayList<ScrumErrorList> addingshowsinquiry(Integer request);
}
