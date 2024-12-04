package com.insurance.policy.unit.application.service;

import com.insurance.policy.application.domain.model.InsurancePolicy;
import com.insurance.policy.application.domain.model.enumType.PolicyStatus;
import com.insurance.policy.application.domain.service.InsurancePolicyService;
import com.insurance.policy.application.exception.ResourceNotFoundException;
import com.insurance.policy.application.port.in.InsurancePolicyServicePort;
import com.insurance.policy.application.port.out.InsurancePolicyRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InsurancePolicyServiceTest {

    private InsurancePolicyRepositoryPort repository;
    private InsurancePolicyServicePort service;

    @BeforeEach
    void setup() {
        repository = mock(InsurancePolicyRepositoryPort.class);
        service = new InsurancePolicyService(repository);
    }

    @Test
    void getPolicyById_ShouldReturnPolicy_WhenPolicyExists() {
        InsurancePolicy policy = new InsurancePolicy(
                1, "Policy A", PolicyStatus.ACTIVE,
                LocalDateTime.now(), LocalDateTime.now().plusDays(10),
                LocalDateTime.now(), LocalDateTime.now()
        );
        when(repository.findPolicyById(1)).thenReturn(policy);

        InsurancePolicy result = service.getPolicyById(1);

        assertNotNull(result);
        assertEquals("Policy A", result.getPolicyName());
        verify(repository, times(1)).findPolicyById(1);
    }

    @Test
    void getPolicyById_ShouldThrowException_WhenPolicyDoesNotExist() {
        when(repository.findPolicyById(1)).thenThrow(new ResourceNotFoundException("Insurance policy with ID 1 not found"));
        assertThrows(RuntimeException.class, () -> service.getPolicyById(1));
        verify(repository, times(1)).findPolicyById(1);
    }

}
