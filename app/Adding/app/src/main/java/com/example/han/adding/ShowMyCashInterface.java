package com.example.han.adding;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

public interface ShowMyCashInterface {
    @LambdaFunction
    Integer addingShowMyCash(String request);
}
