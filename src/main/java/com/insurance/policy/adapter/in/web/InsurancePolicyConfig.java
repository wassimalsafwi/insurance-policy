package com.insurance.policy.adapter.in.web;

import com.insurance.policy.adapter.out.persistence.InsurancePolicyMapper;
import com.insurance.policy.application.domain.service.InsurancePolicyService;
import com.insurance.policy.application.port.in.InsurancePolicyServicePort;
import com.insurance.policy.application.port.out.InsurancePolicyRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InsurancePolicyConfig {

    @Bean
    public InsurancePolicyMapper insurancePolicyMapper() {
        return new InsurancePolicyMapper();
    }

    @Bean
    public InsurancePolicyServicePort insurancePolicyServicePort(InsurancePolicyRepositoryPort repository) {
        return new InsurancePolicyService(repository);
    }

}