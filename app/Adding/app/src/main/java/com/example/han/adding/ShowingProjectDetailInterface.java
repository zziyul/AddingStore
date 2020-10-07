package com.example.han.adding;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

public interface ShowingProjectDetailInterface {
    @LambdaFunction
    ProjectDetailResponseClass addingShowProjectDetail(Integer request);
}
