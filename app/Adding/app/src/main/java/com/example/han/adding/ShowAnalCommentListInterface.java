package com.example.han.adding;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

import java.util.ArrayList;

public interface ShowAnalCommentListInterface {
    @LambdaFunction
    ArrayList<CommentAnalData> addingshowfundingmemberlist(Integer request);
}
