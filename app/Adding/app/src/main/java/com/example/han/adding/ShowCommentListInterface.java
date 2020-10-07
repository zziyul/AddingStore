package com.example.han.adding;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

import java.util.ArrayList;

public interface ShowCommentListInterface {
    @LambdaFunction
    ArrayList<CommentData> addingShowCommentList(Integer request);
}
