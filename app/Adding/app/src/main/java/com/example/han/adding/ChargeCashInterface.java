package com.example.han.adding;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

public interface ChargeCashInterface {
    @LambdaFunction
    Integer addingChangeMyCash(ChargeCashRequest request);
}
