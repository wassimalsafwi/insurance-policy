package com.insurance.policy.application.port.in;

import com.insurance.policy.application.domain.model.InsurancePolicy;

import java.util.List;

public interface InsurancePolicyServicePort {

    List<InsurancePolicy> getAllPolicies();

    InsurancePolicy getPolicyById(Integer id);

    InsurancePolicy createPolicy(InsurancePolicy policy);

    InsurancePolicy updatePolicy(Integer id, InsurancePolicy policy);

}