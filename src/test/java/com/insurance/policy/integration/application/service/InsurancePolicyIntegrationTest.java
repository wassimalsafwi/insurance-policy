package com.insurance.policy.integration.application.service;

import com.insurance.policy.application.domain.model.InsurancePolicy;
import com.insurance.policy.application.domain.model.enumType.PolicyStatus;
import com.insurance.policy.application.port.in.InsurancePolicyServicePort;
import com.insurance.policy.application.port.out.InsurancePolicyRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class InsurancePolicyIntegrationTest {

    @Autowired
    private InsurancePolicyServicePort insurancePolicyService;

    @Autowired
    private InsurancePolicyRepositoryPort repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE insurance_policy RESTART IDENTITY CASCADE");
    }

    @Test
    void shouldCreateAndRetrieveInsurancePolicy() {
        //Given
        InsurancePolicy policy = new InsurancePolicy(
                1,
                "policy name",
                PolicyStatus.ACTIVE,
                LocalDateTime.now().plusDays(30),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        //When
        InsurancePolicy createdPolicy = insurancePolicyService.createPolicy(policy);

        //Then
        InsurancePolicy savedPolicy = repository.findPolicyById(createdPolicy.getId());
        assertThat(savedPolicy.getPolicyName()).isEqualTo(policy.getPolicyName());
        assertThat(savedPolicy.getPolicyStatus()).isEqualTo(policy.getPolicyStatus());
    }

    @Test
    void shouldRetrieveAllInsurancePolicies() {
        //Given
        InsurancePolicy policy1 = createPolicy(1, "Policy One", PolicyStatus.ACTIVE);
        InsurancePolicy policy2 = createPolicy(2, "Policy Two", PolicyStatus.INACTIVE);
        repository.createPolicy(policy1);
        repository.createPolicy(policy2);

        //When
        List<InsurancePolicy> policies = insurancePolicyService.getAllPolicies();

        //Then
        assertThat(policies).hasSize(2);
        assertThat(policies.get(0).getPolicyName()).isEqualTo(policy1.getPolicyName());
        assertThat(policies.get(1).getPolicyName()).isEqualTo(policy2.getPolicyName());
    }

    @Test
    void shouldUpdateInsurancePolicy() {
        //Given
        InsurancePolicy existingPolicy = createPolicy(1, "Policy To Update", PolicyStatus.ACTIVE);
        repository.createPolicy(existingPolicy);

        InsurancePolicy updatedPolicy = new InsurancePolicy(
                existingPolicy.getId(),
                "Updated Policy Name",
                PolicyStatus.INACTIVE,
                existingPolicy.getStartDate(),
                existingPolicy.getEndDate(),
                existingPolicy.getStartDate(),
                existingPolicy.getEndDate()
        );

        //When
        InsurancePolicy updated = insurancePolicyService.updatePolicy(updatedPolicy.getId(), updatedPolicy);

        //Then
        InsurancePolicy savedPolicy = repository.findPolicyById(updated.getId());
        assertThat(savedPolicy.getPolicyName()).isEqualTo(updatedPolicy.getPolicyName());
        assertThat(savedPolicy.getPolicyStatus()).isEqualTo(updatedPolicy.getPolicyStatus());
    }

    @Test
    void shouldReturnEmptyListWhenNoPoliciesExist() {
        List<InsurancePolicy> policies = insurancePolicyService.getAllPolicies();

        assertThat(policies).isEmpty();
    }

    @Test
    void shouldAutomaticallySetTimestampsOnCreate() {
        //Given
        InsurancePolicy policy = new InsurancePolicy(
                null,
                "Policy with Auto Timestamps",
                PolicyStatus.ACTIVE,
                LocalDateTime.now().plusDays(30),
                LocalDateTime.now(),
                null,
                null
        );

        //When
        insurancePolicyService.createPolicy(policy);

        //Then
        InsurancePolicy savedPolicy = repository.findPolicyById(1);
        assertThat(savedPolicy.getCreatedAt()).isNotNull();
        assertThat(savedPolicy.getUpdatedAt()).isNotNull();
        assertThat(savedPolicy.getCreatedAt()).isEqualTo(savedPolicy.getUpdatedAt());
    }

    @Test
    void shouldUpdateUpdatedAtButNotCreatedAt() throws InterruptedException {
        //Given
        InsurancePolicy policy = new InsurancePolicy(
                null,
                "Policy with Auto Timestamps",
                PolicyStatus.ACTIVE,
                LocalDateTime.now().plusDays(30),
                LocalDateTime.now(),
                null,
                null
        );

        //When
        insurancePolicyService.createPolicy(policy);
        InsurancePolicy savedPolicy = repository.findPolicyById(1);
        Thread.sleep(10);
        savedPolicy.setPolicyName("Updated Policy");
        insurancePolicyService.updatePolicy(savedPolicy.getId(), savedPolicy);

        //Then
        InsurancePolicy updatedPolicy = repository.findPolicyById(1);
        assertThat(updatedPolicy.getCreatedAt()).isEqualTo(savedPolicy.getCreatedAt());
        assertThat(updatedPolicy.getUpdatedAt()).isAfter(savedPolicy.getCreatedAt());
    }


    private InsurancePolicy createPolicy(int id, String policyName, PolicyStatus status) {
        return new InsurancePolicy(id, policyName, status, LocalDateTime.now(), LocalDateTime.now().plusDays(30), LocalDateTime.now(), LocalDateTime.now());
    }
}
