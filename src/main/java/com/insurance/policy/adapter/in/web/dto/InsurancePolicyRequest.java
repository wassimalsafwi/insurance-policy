package com.insurance.policy.adapter.in.web.dto;

import com.insurance.policy.application.domain.model.enumType.PolicyStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public final class InsurancePolicyRequest {

    @NotBlank(message = "Policy name must be filled and with at least a non-whitespace character")
    @Size(min = 1, max = 255, message = "Policy name must be between 1 and 255 characters")
    private final String policyName;

    @NotNull(message = "Policy status must not be null")
    private final PolicyStatus policyStatus;

    @NotNull(message = "Start date must not be null")
    private final LocalDateTime startDate;

    @NotNull(message = "End date must not be null")
    private final LocalDateTime endDate;

    public InsurancePolicyRequest(String policyName, PolicyStatus policyStatus, LocalDateTime startDate, LocalDateTime endDate) {
        this.policyName = policyName;
        this.policyStatus = policyStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getPolicyName() {
        return policyName;
    }

    public PolicyStatus getPolicyStatus() {
        return policyStatus;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
