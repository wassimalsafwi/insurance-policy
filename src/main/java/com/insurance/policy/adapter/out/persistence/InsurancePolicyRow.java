package com.insurance.policy.adapter.out.persistence;

import com.insurance.policy.application.domain.model.enumType.PolicyStatus;

import java.time.LocalDateTime;

public class InsurancePolicyRow {

    private Integer id;
    private String policyName;
    private PolicyStatus policyStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public InsurancePolicyRow() {
    }

    public InsurancePolicyRow(Integer id, String policyName, PolicyStatus policyStatus, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.policyName = policyName;
        this.policyStatus = policyStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public void setPolicyStatus(PolicyStatus policyStatus) {
        this.policyStatus = policyStatus;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}