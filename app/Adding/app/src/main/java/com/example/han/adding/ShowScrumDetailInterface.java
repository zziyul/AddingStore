package com.example.han.adding;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

public interface ShowScrumDetailInterface {
    @LambdaFunction
    ScrumDetailResponse addingShowScrumDetail(Integer request);
}
