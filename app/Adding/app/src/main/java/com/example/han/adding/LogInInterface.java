package com.example.han.adding;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;
public interface LogInInterface {
    @LambdaFunction
    AccountClass addingLogIn(LogInRequestClass request);
}
