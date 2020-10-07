package com.example.han.adding;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyError {
    @SerializedName("errorType")
    private String errorType;

    @SerializedName("errorMessage")
    private String errorMessage;

    @SerializedName("stackTrace")
    private List<String> stacTrace;

    public MyError(String errorType, String errorMessage, List<String> stacTrace) {
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.stacTrace = stacTrace;
    }

    public MyError() {
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<String> getStacTrace() {
        return stacTrace;
    }

    public void setStacTrace(List<String> stacTrace) {
        this.stacTrace = stacTrace;
    }

}
