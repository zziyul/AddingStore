package com.example.han.adding;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

import java.util.ArrayList;

public interface ReviewRegisterInterface {
    @LambdaFunction
    boolean addingRegisterEvaluation(ReviewRequest request);
}
