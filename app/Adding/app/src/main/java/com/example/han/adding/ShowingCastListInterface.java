package com.example.han.adding;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

import java.util.ArrayList;
public interface ShowingCastListInterface {
    @LambdaFunction
    ArrayList<PreviewClass> addingShowCast(CastRequestClass request);
}
