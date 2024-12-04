package com.insurance.policy.adapter.in.web;


import com.insurance.policy.adapter.out.persistence.InsurancePolicyMapper;
import com.insurance.policy.application.domain.model.InsurancePolicy;
import com.insurance.policy.application.dto.InsurancePolicyRequest;
import com.insurance.policy.application.dto.InsurancePolicyResponse;
import com.insurance.policy.application.port.in.InsurancePolicyServicePort;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/insurance-policies")
public class InsurancePolicyController {

    private final InsurancePolicyServicePort service;
    private final InsurancePolicyMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(InsurancePolicyController.class);

    @Autowired
    public InsurancePolicyController(InsurancePolicyServicePort service, InsurancePolicyMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<InsurancePolicyResponse>> getAllPolicies() {
        logger.info("Fetching all insurance policies");
        List<InsurancePolicyResponse> policies = service.getAllPolicies().stream()
                .map(mapper::mapDomainToResponse)
                .toList();

        if (policies.isEmpty()) {
            logger.info("No insurance policies found");
            return ResponseEntity.noContent().build();
        }
        logger.info("Returning {} insurance policies", policies.size());
        return ResponseEntity.ok(policies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsurancePolicyResponse> getPolicyById(@PathVariable Integer id) {
        logger.info("Fetching insurance policy with ID {}", id);
        InsurancePolicy policy = service.getPolicyById(id);
        return ResponseEntity.ok(mapper.mapDomainToResponse(policy));
    }

    @PostMapping
    public ResponseEntity<InsurancePolicyResponse> createPolicy(@Valid @RequestBody InsurancePolicyRequest request) {
        logger.info("Creating a new insurance policy");
        InsurancePolicy createdPolicy = service.createPolicy(mapper.mapRequestToDomain(request));
        InsurancePolicyResponse response = mapper.mapDomainToResponse(createdPolicy);
        return ResponseEntity.created(URI.create("/api/insurance-policies/" + createdPolicy.getId()))
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsurancePolicyResponse> updatePolicy(
            @PathVariable Integer id,
            @Valid @RequestBody InsurancePolicyRequest request) {
        logger.info("Updating insurance policy with ID {}", id);
        InsurancePolicy updatedPolicy = service.updatePolicy(id, mapper.mapRequestToDomain(request));
        return ResponseEntity.ok(mapper.mapDomainToResponse(updatedPolicy));
    }

}