package com.insurance.policy.application.domain.service;

import com.insurance.policy.application.domain.model.InsurancePolicy;
import com.insurance.policy.application.port.in.InsurancePolicyServicePort;
import com.insurance.policy.application.port.out.InsurancePolicyRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public class InsurancePolicyService implements InsurancePolicyServicePort {

    private final InsurancePolicyRepositoryPort repository;

    @Autowired
    public InsurancePolicyService(InsurancePolicyRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<InsurancePolicy> getAllPolicies() {
        return repository.findAllPolicies();
    }

    @Override
    public InsurancePolicy getPolicyById(Integer id) {
        return repository.findPolicyById(id);
    }

    @Override
    public InsurancePolicy createPolicy(InsurancePolicy policy) {
        LocalDateTime now = LocalDateTime.now();
        policy.setCreatedAt(now);
        policy.setUpdatedAt(now);
        repository.createPolicy(policy);
        return policy;
    }

    @Override
    @Transactional
    public InsurancePolicy updatePolicy(Integer id, InsurancePolicy policy) {
        InsurancePolicy existingPolicy = repository.findPolicyById(id);
        policy.setCreatedAt(existingPolicy.getCreatedAt());
        policy.setUpdatedAt(LocalDateTime.now());
        repository.updatePolicy(id, policy);
        return policy;
    }
}