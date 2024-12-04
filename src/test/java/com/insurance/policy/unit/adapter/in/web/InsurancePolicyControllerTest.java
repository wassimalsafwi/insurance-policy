package com.insurance.policy.unit.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.insurance.policy.adapter.in.web.GlobalExceptionHandler;
import com.insurance.policy.adapter.in.web.InsurancePolicyController;
import com.insurance.policy.adapter.out.persistence.InsurancePolicyMapper;
import com.insurance.policy.application.domain.model.InsurancePolicy;
import com.insurance.policy.application.domain.model.PolicyStatus;
import com.insurance.policy.application.dto.InsurancePolicyRequest;
import com.insurance.policy.application.dto.InsurancePolicyResponse;
import com.insurance.policy.application.exception.ResourceNotFoundException;
import com.insurance.policy.application.port.in.InsurancePolicyServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class InsurancePolicyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InsurancePolicyServicePort service;

    @Mock
    private InsurancePolicyMapper mapper;

    @InjectMocks
    private InsurancePolicyController controller;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()) // Add the exception handler here
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllPolicies_ShouldReturn200_WhenPoliciesExist() throws Exception {
        List<InsurancePolicy> policies = List.of(
                new InsurancePolicy(1, "Policy A", PolicyStatus.ACTIVE,
                        LocalDateTime.now(), LocalDateTime.now().plusDays(10),
                        LocalDateTime.now(), LocalDateTime.now())
        );
        when(service.getAllPolicies()).thenReturn(policies);

        mockMvc.perform(get("/api/insurance-policies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllPolicies_ShouldReturn204_WhenNoPoliciesExist() throws Exception {
        when(service.getAllPolicies()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/insurance-policies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getPolicyById_ShouldReturn200_WhenPolicyExists() throws Exception {
        InsurancePolicy policy = new InsurancePolicy(1, "Policy A", PolicyStatus.ACTIVE,
                LocalDateTime.now(), LocalDateTime.now().plusDays(10),
                LocalDateTime.now(), LocalDateTime.now());

        InsurancePolicyResponse policyResponse = new InsurancePolicyResponse(1, "Policy A", PolicyStatus.ACTIVE,
                LocalDateTime.now(), LocalDateTime.now().plusDays(10),
                LocalDateTime.now(), LocalDateTime.now());

        when(service.getPolicyById(1)).thenReturn(policy);
        when(mapper.mapDomainToResponse(policy)).thenReturn(policyResponse);

        mockMvc.perform(get("/api/insurance-policies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.policyName").value("Policy A"))
                .andExpect(jsonPath("$.policyStatus").value("ACTIVE"));
    }


    @Test
    void getPolicyById_ShouldReturn404_WhenPolicyDoesNotExist() throws Exception {
        when(service.getPolicyById(1)).thenThrow(new ResourceNotFoundException("Policy not found"));

        mockMvc.perform(get("/api/insurance-policies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.detail").value("Policy not found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getPolicyById_ShouldReturn500_WhenUnexpectedErrorOccurs() throws Exception {
        when(service.getPolicyById(1)).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/insurance-policies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.title").value("Internal Server Error"))
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred."))
                .andExpect(jsonPath("$.status").value(500));
    }


    @Test
    void createPolicy_ShouldReturn201_WhenPolicyIsCreated() throws Exception {
        InsurancePolicyRequest request = new InsurancePolicyRequest("Policy A", PolicyStatus.ACTIVE,
                LocalDateTime.now(), LocalDateTime.now().plusDays(10));

        InsurancePolicy policy = new InsurancePolicy(1, "Policy A", PolicyStatus.ACTIVE,
                LocalDateTime.now(), LocalDateTime.now().plusDays(10),
                LocalDateTime.now(), LocalDateTime.now());

        when(service.createPolicy(any())).thenReturn(policy);

        mockMvc.perform(post("/api/insurance-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createPolicy_ShouldReturn400_WhenRequestIsInvalid() throws Exception {
        InsurancePolicyRequest request = new InsurancePolicyRequest("", PolicyStatus.ACTIVE,
                LocalDateTime.now(), LocalDateTime.now().plusDays(10));

        mockMvc.perform(post("/api/insurance-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPolicy_ShouldReturn400_WhenRequestBodyIsEmpty() throws Exception {
        mockMvc.perform(post("/api/insurance-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Request Body"))
                .andExpect(jsonPath("$.detail").value("The request body could not be read or parsed."))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void updatePolicy_ShouldReturn200_WhenPolicyIsUpdated() throws Exception {
        InsurancePolicyRequest request = new InsurancePolicyRequest("Policy B", PolicyStatus.INACTIVE,
                LocalDateTime.now(), LocalDateTime.now().plusDays(5));

        InsurancePolicy policy = new InsurancePolicy(1, "Policy B", PolicyStatus.INACTIVE,
                LocalDateTime.now(), LocalDateTime.now().plusDays(5),
                LocalDateTime.now(), LocalDateTime.now());

        when(service.updatePolicy(eq(1), any())).thenReturn(policy);

        mockMvc.perform(put("/api/insurance-policies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void updatePolicy_ShouldReturn404_WhenPolicyDoesNotExist() throws Exception {
        InsurancePolicyRequest request = new InsurancePolicyRequest("Policy B", PolicyStatus.INACTIVE,
                LocalDateTime.now(), LocalDateTime.now().plusDays(5));

        when(service.updatePolicy(eq(1), any())).thenThrow(new ResourceNotFoundException("Policy not found"));

        mockMvc.perform(put("/api/insurance-policies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePolicy_ShouldReturn400_WhenRequestIsInvalid() throws Exception {
        InsurancePolicyRequest request = new InsurancePolicyRequest("", PolicyStatus.INACTIVE,
                LocalDateTime.now(), LocalDateTime.now().plusDays(5));

        mockMvc.perform(put("/api/insurance-policies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

}
