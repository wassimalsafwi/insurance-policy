package com.insurance.policy.acceptance.steps;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.insurance.policy.InsurancePolicyManagementApplication;
import com.insurance.policy.adapter.out.persistence.InsurancePolicyRepositoryAdapter;
import com.insurance.policy.application.domain.model.InsurancePolicy;
import com.insurance.policy.application.domain.model.PolicyStatus;
import com.insurance.policy.application.dto.InsurancePolicyRequest;
import com.insurance.policy.application.dto.InsurancePolicyResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = InsurancePolicyManagementApplication.class)
public class InsurancePolicySteps {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private InsurancePolicyRepositoryAdapter insurancePolicyRepositoryAdapter;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private ResponseEntity<String> response;

    private List<Integer> generatedIds = new ArrayList<>();

    @Given("the database contains the following insurance policies:")
    public void theDatabaseContainsTheFollowingInsurancePolicies(List<Map<String, String>> policies) {
        jdbcTemplate.execute("TRUNCATE TABLE insurance_policy RESTART IDENTITY CASCADE");
        generatedIds.clear();
        for (Map<String, String> policyData : policies) {
            InsurancePolicy policy = new InsurancePolicy(Integer.parseInt(policyData.get("id")),
                    policyData.get("policyName"),
                    Enum.valueOf(PolicyStatus.class, policyData.get("policyStatus")),
                    LocalDateTime.parse(policyData.get("startDate")),
                    LocalDateTime.parse(policyData.get("endDate")),
                    LocalDateTime.parse(policyData.get("createdAt")),
                    LocalDateTime.parse(policyData.get("updatedAt"))
            );
            insurancePolicyRepositoryAdapter.createPolicy(policy);
            generatedIds.add(Integer.parseInt(policyData.get("id")));
        }
    }

    @Given("the database is empty")
    public void theDatabaseIsEmpty() {
        jdbcTemplate.execute("TRUNCATE TABLE insurance_policy RESTART IDENTITY CASCADE");
    }

    @When("I request to list all insurance policies")
    public void iRequestToListAllInsurancePolicies() {
        response = restTemplate.getForEntity("/api/insurance-policies", String.class);
    }

    @Then("I should receive a list with the following policies:")
    public void iShouldReceiveAListWithTheFollowingPolicies(List<Map<String, String>> expectedPolicies) throws Exception {
        List<Map<String, Object>> actualPolicies = objectMapper.readValue(
                response.getBody(), new TypeReference<>() {}
        );
        assertThat(actualPolicies).hasSize(expectedPolicies.size());
        for (int i = 0; i < expectedPolicies.size(); i++) {
            Map<String, String> expected = expectedPolicies.get(i);
            Map<String, Object> actual = actualPolicies.get(i);
            assertThat(actual.get("policyName")).isEqualTo(expected.get("policyName"));
            assertThat(actual.get("policyStatus")).isEqualTo(expected.get("policyStatus"));
            assertThat(actual.get("startDate")).isEqualTo(expected.get("startDate"));
            assertThat(actual.get("endDate")).isEqualTo(expected.get("endDate"));
            assertThat(actual.get("createdAt")).isEqualTo(expected.get("createdAt"));
            assertThat(actual.get("updatedAt")).isEqualTo(expected.get("updatedAt"));
        }
    }

    @When("I create a new insurance policy with the following details:")
    public void iCreateANewInsurancePolicyWithTheFollowingDetails(List<Map<String, String>> policyDetails) {
        if (policyDetails.isEmpty()) {
            throw new IllegalArgumentException("Policy details cannot be empty");
        }
        Map<String, String> policyData = policyDetails.get(0);
        InsurancePolicyRequest request = mapToInsurancePolicyRequest(policyData);
        restTemplate.postForEntity("/api/insurance-policies", request, String.class);
    }

    @When("I request to read the insurance policy with ID {int}")
    public void iRequestToReadTheInsurancePolicyWithID(int idIndex) {
        int idToRetrieve = generatedIds.get(idIndex - 1);
        response = restTemplate.getForEntity("/api/insurance-policies/" + idToRetrieve, String.class);
    }

    @Then("I should receive the following insurance policy:")
    public void iShouldReceiveTheFollowingInsurancePolicy(List<Map<String, String>> expectedPolicy) throws Exception {
        // Deserialize the response into a single InsurancePolicyEntity
        InsurancePolicyResponse actualPolicy = objectMapper.readValue(response.getBody(), InsurancePolicyResponse.class);

        // Validate the fields
        assertThat(actualPolicy.getId()).isEqualTo(Integer.parseInt(expectedPolicy.get(0).get("id")));
        assertThat(actualPolicy.getPolicyName()).isEqualTo(expectedPolicy.get(0).get("policyName"));
        assertThat(actualPolicy.getPolicyStatus().name()).isEqualTo(expectedPolicy.get(0).get("policyStatus"));
        assertThat(actualPolicy.getStartDate().toString()).isEqualTo(expectedPolicy.get(0).get("startDate"));
        assertThat(actualPolicy.getEndDate().toString()).isEqualTo(expectedPolicy.get(0).get("endDate"));
        assertThat(actualPolicy.getCreatedAt().toString()).isEqualTo(expectedPolicy.get(0).get("createdAt"));
        assertThat(actualPolicy.getUpdatedAt().toString()).isEqualTo(expectedPolicy.get(0).get("updatedAt"));
    }

    @When("I edit the insurance policy with ID {int} with the following details:")
    public void iEditTheInsurancePolicyWithIDWithTheFollowingDetails(int id, List<Map<String, String>> policyDetails) {
        InsurancePolicyRequest request = mapToInsurancePolicyRequest(policyDetails.get(0));
        restTemplate.put("/api/insurance-policies/" + id, request);
    }

    @Then("the database should contain the following insurance policies:")
    public void theDatabaseShouldContainTheFollowingInsurancePolicies(List<Map<String, String>> expectedPolicies) {
        List<InsurancePolicy> actualPolicies = insurancePolicyRepositoryAdapter.findAllPolicies();

        // Assert size matches
        assertThat(actualPolicies).hasSize(expectedPolicies.size());

        // Assert each policy matches
        for (int i = 0; i < expectedPolicies.size(); i++) {
            Map<String, String> expected = expectedPolicies.get(i);
            InsurancePolicy actual = actualPolicies.get(i);

            assertThat(actual.getPolicyName()).isEqualTo(expected.get("policyName"));
            assertThat(actual.getPolicyStatus().name()).isEqualTo(expected.get("policyStatus"));
            assertThat(actual.getStartDate().toString()).isEqualTo(expected.get("startDate"));
            assertThat(actual.getEndDate().toString()).isEqualTo(expected.get("endDate"));
        }
    }

    @Then("The insurance policy with ID {int} should have the following details:")
    public void theInsurancePolicyWithIDShouldHaveTheFollowingDetails(int id, List<Map<String, String>> expectedDetails) {
        ResponseEntity<InsurancePolicyResponse> response = restTemplate.getForEntity(
                "/api/insurance-policies/" + id, InsurancePolicyResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        InsurancePolicyResponse actual = response.getBody();
        Map<String, String> expected = expectedDetails.get(0);

        assertThat(actual.getPolicyName()).isEqualTo(expected.get("policyName"));
        assertThat(actual.getPolicyStatus()).isEqualTo(Enum.valueOf(PolicyStatus.class, expected.get("policyStatus")));
        assertThat(actual.getStartDate()).isEqualTo(LocalDateTime.parse(expected.get("startDate")));
        assertThat(actual.getEndDate()).isEqualTo(LocalDateTime.parse(expected.get("endDate")));
    }

    private InsurancePolicyRequest mapToInsurancePolicyRequest(Map<String, String> policyData) {
        InsurancePolicyRequest request = new InsurancePolicyRequest();
        request.setPolicyName(policyData.get("policyName"));
        request.setPolicyStatus(Enum.valueOf(PolicyStatus.class, policyData.get("policyStatus")));
        request.setStartDate(LocalDateTime.parse(policyData.get("startDate")));
        request.setEndDate(LocalDateTime.parse(policyData.get("endDate")));
        return request;
    }

}
