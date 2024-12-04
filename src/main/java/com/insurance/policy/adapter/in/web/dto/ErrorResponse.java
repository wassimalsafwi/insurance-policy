package com.insurance.policy.adapter.in.web.dto;

import java.util.Map;

public class ErrorResponse {
    private final String title;
    private final String detail;
    private final int status;
    private final Map<String, String> validationErrors;

    public ErrorResponse(String title, String detail, int status, Map<String, String> validationErrors) {
        this.title = title;
        this.detail = detail;
        this.status = status;
        this.validationErrors = validationErrors;
    }

    public ErrorResponse(String title, String detail, int status) {
        this(title, detail, status, null);
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }
}
