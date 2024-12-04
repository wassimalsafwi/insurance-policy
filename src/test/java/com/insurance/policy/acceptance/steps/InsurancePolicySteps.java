package com.insurance.policy.acceptance.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.insurance.policy.InsurancePolicyManagementApplication;
import com.insurance.policy.application.domain.model.InsurancePolicy;
import com.insurance.policy.application.domain.model.enumType.PolicyStatus;
import com.insurance.policy.adapter.in.web.dto.InsurancePolicyRequest;
import com.insurance.policy.adapter.in.web.dto.InsurancePolicyResponse;
import com.insurance.policy.application.port.out.InsurancePolicyRepositoryPort;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
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
    private InsurancePolicyRepositoryPort insurancePolicyRepositoryPort;

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
                    null,
                    null
            );
            insurancePolicyRepositoryPort.createPolicy(policy);
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
        InsurancePolicyResponse actualPolicy = objectMapper.readValue(response.getBody(), InsurancePolicyResponse.class);

        assertThat(actualPolicy.getId()).isEqualTo(Integer.parseInt(expectedPolicy.get(0).get("id")));
        assertThat(actualPolicy.getPolicyName()).isEqualTo(expectedPolicy.get(0).get("policyName"));
        assertThat(actualPolicy.getPolicyStatus().name()).isEqualTo(expectedPolicy.get(0).get("policyStatus"));
        assertThat(actualPolicy.getStartDate().toString()).isEqualTo(expectedPolicy.get(0).get("startDate"));
        assertThat(actualPolicy.getEndDate().toString()).isEqualTo(expectedPolicy.get(0).get("endDate"));
    }

    @When("I edit the insurance policy with ID {int} with the following details:")
    public void iEditTheInsurancePolicyWithIDWithTheFollowingDetails(int id, List<Map<String, String>> policyDetails) {
        InsurancePolicyRequest request = mapToInsurancePolicyRequest(policyDetails.get(0));
        restTemplate.put("/api/insurance-policies/" + id, request);
    }

    @Then("the database should contain the following insurance policies:")
    public void theDatabaseShouldContainTheFollowingInsurancePolicies(List<Map<String, String>> expectedPolicies) {
        List<InsurancePolicy> actualPolicies = insurancePolicyRepositoryPort.findAllPolicies();

        assertThat(actualPolicies).hasSize(expectedPolicies.size());

        for (int i = 0; i < expectedPolicies.size(); i++) {
            Map<String, String> expected = expectedPolicies.get(i);
            InsurancePolicy actual = actualPolicies.get(i);

            assertThat(actual.getPolicyName()).isEqualTo(expected.get("policyName"));
            assertThat(actual.getPolicyStatus().name()).isEqualTo(expected.get("policyStatus"));
            assertThat(actual.getStartDate().toString()).isEqualTo(expected.get("startDate"));
            assertThat(actual.getEndDate().toString()).isEqualTo(expected.get("endDate"));
        }
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
