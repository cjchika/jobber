package com.cjchika.jobber.user.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponse(boolean success, String message, T data){
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    //    Helper methods for quick ResponseEntity creation
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message, HttpStatus status){
        return ResponseEntity.status(status).body(new ApiResponse<>(true, message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message, String locationUrl) {
        return ResponseEntity.created(URI.create(locationUrl))
                .body(new ApiResponse<>(true, message, data));
    }

    public static ResponseEntity<ApiResponse<Void>> error(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new ApiResponse<>(false, message, null));
    }
}
