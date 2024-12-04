package com.insurance.policy.application.port.out;

import com.insurance.policy.application.domain.model.InsurancePolicy;

import java.util.List;

public interface InsurancePolicyRepositoryPort {

    List<InsurancePolicy> findAllPolicies();

    InsurancePolicy findPolicyById(Integer id);

    void createPolicy(InsurancePolicy policy);

    void updatePolicy(Integer id, InsurancePolicy policy);

}
