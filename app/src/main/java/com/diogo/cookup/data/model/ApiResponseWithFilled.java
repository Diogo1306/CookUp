package com.diogo.cookup.data.model;

public class ApiResponseWithFilled<T> {
    private boolean success;
    private boolean filled;
    private T data;

    public boolean isSuccess() { return success; }
    public boolean isFilled() { return filled; }
    public T getData() { return data; }
}

