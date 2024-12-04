package com.insurance.policy.adapter.out.persistence;

import com.insurance.policy.application.domain.model.InsurancePolicy;
import com.insurance.policy.adapter.in.web.dto.InsurancePolicyRequest;
import com.insurance.policy.adapter.in.web.dto.InsurancePolicyResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InsurancePolicyMapper {

    public InsurancePolicy mapRowToDomain(InsurancePolicyRow row) {
        return new InsurancePolicy(
                row.getId(),
                row.getPolicyName(),
                row.getPolicyStatus(),
                row.getStartDate(),
                row.getEndDate(),
                row.getCreatedAt(),
                row.getUpdatedAt()
        );
    }

    public InsurancePolicyRow mapDomainToRow(InsurancePolicy policy) {
        return new InsurancePolicyRow(
                policy.getId(),
                policy.getPolicyName(),
                policy.getPolicyStatus(),
                policy.getStartDate(),
                policy.getEndDate(),
                policy.getCreatedAt(),
                policy.getUpdatedAt()
        );
    }

    public InsurancePolicy mapRequestToDomain(InsurancePolicyRequest request) {
        return new InsurancePolicy(
                null,
                request.getPolicyName(),
                request.getPolicyStatus(),
                request.getStartDate(),
                request.getEndDate(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public InsurancePolicyResponse mapDomainToResponse(InsurancePolicy policy) {
        return new InsurancePolicyResponse(
                policy.getId(),
                policy.getPolicyName(),
                policy.getPolicyStatus(),
                policy.getStartDate(),
                policy.getEndDate(),
                policy.getCreatedAt(),
                policy.getUpdatedAt()
        );
    }
}
