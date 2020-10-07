package com.example.han.adding;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

import java.util.ArrayList;

public interface ShowMyProjListInterface {
    @LambdaFunction
    ArrayList<PreviewMyProjClass> addingShowMyProject(String request);
}
