package com.example.han.adding;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

public interface RegistScrumInterface {
    @LambdaFunction
    Boolean addingRegisterScrum(RegisterScrumRequest request);
}
