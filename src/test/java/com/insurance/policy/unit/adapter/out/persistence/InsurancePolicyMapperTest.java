package com.insurance.policy.unit.adapter.out.persistence;


import com.insurance.policy.adapter.out.persistence.InsurancePolicyMapper;
import com.insurance.policy.adapter.out.persistence.InsurancePolicyRow;
import com.insurance.policy.application.domain.model.InsurancePolicy;
import com.insurance.policy.application.domain.model.PolicyStatus;
import com.insurance.policy.application.dto.InsurancePolicyRequest;
import com.insurance.policy.application.dto.InsurancePolicyResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class InsurancePolicyMapperTest {

    @Autowired
    private InsurancePolicyMapper mapper;

    @Test
    void shouldMapRowToDomain() {
        // Given
        InsurancePolicyRow row = new InsurancePolicyRow(
                1,
                "Test Policy",
                PolicyStatus.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );

        // When
        InsurancePolicy domain = mapper.mapRowToDomain(row);

        // Then
        assertThat(domain.getId()).isEqualTo(row.getId());
        assertThat(domain.getPolicyName()).isEqualTo(row.getPolicyName());
        assertThat(domain.getPolicyStatus()).isEqualTo(row.getPolicyStatus());
        assertThat(domain.getStartDate()).isEqualTo(row.getStartDate());
        assertThat(domain.getEndDate()).isEqualTo(row.getEndDate());
        assertThat(domain.getCreatedAt()).isEqualTo(row.getCreatedAt());
        assertThat(domain.getUpdatedAt()).isEqualTo(row.getUpdatedAt());
    }

    @Test
    void shouldMapDomainToRow() {
        // Given
        InsurancePolicy policy = new InsurancePolicy(
                1,
                "Test Policy",
                PolicyStatus.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );

        // When
        InsurancePolicyRow row = mapper.mapDomainToRow(policy);

        // Then
        assertThat(row.getId()).isEqualTo(policy.getId());
        assertThat(row.getPolicyName()).isEqualTo(policy.getPolicyName());
        assertThat(row.getPolicyStatus()).isEqualTo(policy.getPolicyStatus());
        assertThat(row.getStartDate()).isEqualTo(policy.getStartDate());
        assertThat(row.getEndDate()).isEqualTo(policy.getEndDate());
        assertThat(row.getCreatedAt()).isEqualTo(policy.getCreatedAt());
        assertThat(row.getUpdatedAt()).isEqualTo(policy.getUpdatedAt());
    }

    @Test
    void shouldMapRequestToDomain() {
        // Given
        InsurancePolicyRequest request = new InsurancePolicyRequest(
                "Test Policy",
                PolicyStatus.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30)
        );

        // When
        InsurancePolicy domain = mapper.mapRequestToDomain(request);

        // Then
        assertThat(domain.getId()).isNull(); // ID is not set in the request
        assertThat(domain.getPolicyName()).isEqualTo(request.getPolicyName());
        assertThat(domain.getPolicyStatus()).isEqualTo(request.getPolicyStatus());
        assertThat(domain.getStartDate()).isEqualTo(request.getStartDate());
        assertThat(domain.getEndDate()).isEqualTo(request.getEndDate());
        assertThat(domain.getCreatedAt()).isNotNull(); // CreatedAt is set to the current time
        assertThat(domain.getUpdatedAt()).isNotNull(); // UpdatedAt is set to the current time
    }

    @Test
    void shouldMapDomainToResponse() {
        // Given
        InsurancePolicy policy = new InsurancePolicy(
                1,
                "Test Policy",
                PolicyStatus.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );

        // When
        InsurancePolicyResponse response = mapper.mapDomainToResponse(policy);

        // Then
        assertThat(response.getId()).isEqualTo(policy.getId());
        assertThat(response.getPolicyName()).isEqualTo(policy.getPolicyName());
        assertThat(response.getPolicyStatus()).isEqualTo(policy.getPolicyStatus());
        assertThat(response.getStartDate()).isEqualTo(policy.getStartDate());
        assertThat(response.getEndDate()).isEqualTo(policy.getEndDate());
        assertThat(response.getCreatedAt()).isEqualTo(policy.getCreatedAt());
        assertThat(response.getUpdatedAt()).isEqualTo(policy.getUpdatedAt());
    }
}
