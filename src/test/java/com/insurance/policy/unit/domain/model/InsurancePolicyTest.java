package com.insurance.policy.unit.domain.model;

import com.insurance.policy.application.domain.model.InsurancePolicy;
import com.insurance.policy.application.domain.model.enumType.PolicyStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InsurancePolicyTest {

    @Test
    void shouldCreateInsurancePolicySuccessfully() {
        InsurancePolicy insurancePolicy = new InsurancePolicy(1, "Policy One", PolicyStatus.ACTIVE,
                LocalDateTime.of(2023, 1, 1, 0, 0),
                LocalDateTime.of(2023, 12, 31, 23, 59),
                LocalDateTime.of(2023, 12, 31, 23, 59),
                LocalDateTime.of(2023, 12, 31, 23, 59));

        assertEquals(1, insurancePolicy.getId());
        assertEquals("Policy One", insurancePolicy.getPolicyName());
        assertEquals(PolicyStatus.ACTIVE, insurancePolicy.getPolicyStatus());
    }

    @Test
    void shouldUpdateInsurancePolicySuccessfully() {
        InsurancePolicy policy = new InsurancePolicy(
                1, "Policy One", PolicyStatus.ACTIVE,
                LocalDateTime.of(2023, 1, 1, 0, 0),
                LocalDateTime.of(2023, 12, 31, 23, 59),
                LocalDateTime.of(2023, 12, 31, 23, 59),
                LocalDateTime.of(2023, 12, 31, 23, 59));


        policy.update("Updated Policy", PolicyStatus.INACTIVE,
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 12, 31, 23, 59),
                LocalDateTime.of(2024, 12, 31, 23, 59));

        assertEquals("Updated Policy", policy.getPolicyName());
        assertEquals(PolicyStatus.INACTIVE, policy.getPolicyStatus());
    }

}
