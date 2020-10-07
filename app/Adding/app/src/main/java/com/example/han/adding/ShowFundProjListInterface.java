package com.example.han.adding;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

import java.util.ArrayList;

public interface ShowFundProjListInterface {
    @LambdaFunction
    ArrayList<PreviewMyProjClass> addingSponsor(String request);
}
