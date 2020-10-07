package com.example.han.adding;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

public interface CreatingAccountInterface {
    @LambdaFunction
    Boolean addingCreateAccount(AccountClass request);
}
