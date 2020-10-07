package com.example.han.adding;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

public interface ScrumErrorSendInterface {
    @LambdaFunction
    boolean addingcreatesinquiry(ScrumErrorSendRequest request);
}
