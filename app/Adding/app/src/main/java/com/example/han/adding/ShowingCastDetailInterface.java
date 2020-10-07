package com.example.han.adding;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

import java.util.ArrayList;

public interface ShowingCastDetailInterface {
    @LambdaFunction
    CastDetailResponseClass addingShowCastDetail(Integer request);
}
