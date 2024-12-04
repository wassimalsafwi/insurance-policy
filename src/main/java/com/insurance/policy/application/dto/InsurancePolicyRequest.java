package com.insurance.policy.application.dto;

import com.insurance.policy.application.domain.model.PolicyStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class InsurancePolicyRequest {

    @NotBlank(message = "Policy name must be filled and with at least a non-whitespace character")
    @Size(min = 1, max = 255, message = "Policy name must be between 1 and 255 characters")
    private String policyName;

    @NotNull(message = "Policy status must not be null")
    private PolicyStatus policyStatus;

    @NotNull(message = "Start date must not be null")
    private LocalDateTime startDate;

    @NotNull(message = "End date must not be null")
    private LocalDateTime endDate;

    public InsurancePolicyRequest() {
    }

    public InsurancePolicyRequest(String policyName, PolicyStatus policyStatus, LocalDateTime startDate, LocalDateTime endDate) {
        this.policyName = policyName;
        this.policyStatus = policyStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public PolicyStatus getPolicyStatus() {
        return policyStatus;
    }

    public void setPolicyStatus(PolicyStatus policyStatus) {
        this.policyStatus = policyStatus;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
