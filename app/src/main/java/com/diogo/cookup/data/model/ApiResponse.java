package com.diogo.cookup.data.model;

public class ApiResponse {
    private boolean success;
    private String message;
    private UserData data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public UserData getData() {
        return data;
    }
}
