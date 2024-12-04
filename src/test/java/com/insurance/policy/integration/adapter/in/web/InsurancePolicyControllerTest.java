package com.insurance.policy.integration.adapter.in.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class InsurancePolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnBadRequestForInvalidInput() throws Exception {
        String invalidRequest = """
            {
                "policyName": null,
                "policyStatus": null,
                "startDate": null,
                "endDate": null
            }
        """;

        mockMvc.perform(post("/api/insurance-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Error"))
                .andExpect(jsonPath("$.detail").value("One or more fields are invalid. See validationErrors for details."))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors.policyName").value("Policy name must be filled and with at least a non-whitespace character"))
                .andExpect(jsonPath("$.validationErrors.policyStatus").value("Policy status must not be null"))
                .andExpect(jsonPath("$.validationErrors.startDate").value("Start date must not be null"))
                .andExpect(jsonPath("$.validationErrors.endDate").value("End date must not be null"));


    }

    @Test
    void shouldReturnBadRequestForInvalidPolicyName() throws Exception {
        String invalidRequest = """
        {
            "policyName": "   ",
            "policyStatus": "ACTIVE",
            "startDate": "2024-01-01T00:00:00",
            "endDate": "2024-12-31T23:59:59"
        }
    """;

        mockMvc.perform(post("/api/insurance-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Error"))
                .andExpect(jsonPath("$.detail").value("One or more fields are invalid. See validationErrors for details."))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors.policyName").value("Policy name must be filled and with at least a non-whitespace character"));

    }
}
