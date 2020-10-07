package com.example.han.adding;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaDataBinder;
import com.amazonaws.util.StringUtils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

public class LambdaDataListBinder implements LambdaDataBinder {

    private final Gson gson;
    Type mType;

    public LambdaDataListBinder(Type type) {
        this.gson = new Gson();
        mType = type;
    }

    @Override
    public <T> T deserialize(byte[] content, Class<T> clazz) {
        if (content == null) {
            return null;
        }
        Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content)));
        return gson.fromJson(reader, mType);
    }

    @Override
    public byte[] serialize(Object object) {
        return gson.toJson(object).getBytes(StringUtils.UTF8);
    }
}
