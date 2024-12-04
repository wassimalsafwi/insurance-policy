package com.insurance.policy.adapter.in.web;

import com.insurance.policy.adapter.out.persistence.InsurancePolicyMapper;
import com.insurance.policy.application.domain.model.InsurancePolicy;
import com.insurance.policy.adapter.in.web.dto.InsurancePolicyRequest;
import com.insurance.policy.adapter.in.web.dto.InsurancePolicyResponse;
import com.insurance.policy.application.port.in.InsurancePolicyServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/insurance-policies")
@Tag(name = "Insurance Policies", description = "API for managing insurance policies")
public class InsurancePolicyController {

    private final InsurancePolicyServicePort service;
    private final InsurancePolicyMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(InsurancePolicyController.class);

    @Autowired
    public InsurancePolicyController(InsurancePolicyServicePort service, InsurancePolicyMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "Get all insurance policies", description = "Fetches a list of all insurance policies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of insurance policies retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No insurance policies found")
    })
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

    @Operation(summary = "Get insurance policy by ID", description = "Fetches details of an insurance policy by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insurance policy retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Insurance policy not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InsurancePolicyResponse> getPolicyById(@PathVariable Integer id) {
        logger.info("Fetching insurance policy with ID {}", id);
        InsurancePolicy policy = service.getPolicyById(id);
        return ResponseEntity.ok(mapper.mapDomainToResponse(policy));
    }

    @Operation(summary = "Create a new insurance policy", description = "Creates a new insurance policy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Insurance policy created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<InsurancePolicyResponse> createPolicy(@Valid @RequestBody InsurancePolicyRequest request) {
        logger.info("Creating a new insurance policy");
        InsurancePolicy createdPolicy = service.createPolicy(mapper.mapRequestToDomain(request));
        InsurancePolicyResponse response = mapper.mapDomainToResponse(createdPolicy);
        return ResponseEntity.created(URI.create("/api/insurance-policies/" + createdPolicy.getId()))
                .body(response);
    }

    @Operation(summary = "Update an existing insurance policy", description = "Updates an insurance policy by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insurance policy updated successfully"),
            @ApiResponse(responseCode = "404", description = "Insurance policy not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<InsurancePolicyResponse> updatePolicy(
            @PathVariable Integer id,
            @Valid @RequestBody InsurancePolicyRequest request) {
        logger.info("Updating insurance policy with ID {}", id);
        InsurancePolicy updatedPolicy = service.updatePolicy(id, mapper.mapRequestToDomain(request));
        return ResponseEntity.ok(mapper.mapDomainToResponse(updatedPolicy));
    }

}