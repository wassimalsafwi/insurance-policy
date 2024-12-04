package com.insurance.policy.adapter.out.persistence;

import com.insurance.policy.application.domain.model.InsurancePolicy;
import com.insurance.policy.application.domain.model.enumType.PolicyStatus;
import com.insurance.policy.application.exception.ResourceNotFoundException;
import com.insurance.policy.application.port.out.InsurancePolicyRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InsurancePolicyRepositoryAdapter implements InsurancePolicyRepositoryPort {

    private final JdbcTemplate jdbcTemplate;
    private final InsurancePolicyMapper mapper;

    @Autowired
    public InsurancePolicyRepositoryAdapter(JdbcTemplate jdbcTemplate, InsurancePolicyMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    private final RowMapper<InsurancePolicyRow> rowMapper = (rs, rowNum) -> new InsurancePolicyRow(
            rs.getInt("id"),
            rs.getString("policy_name"),
            PolicyStatus.valueOf(rs.getString("policy_status")),
            rs.getTimestamp("start_date").toLocalDateTime(),
            rs.getTimestamp("end_date").toLocalDateTime(),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime()
    );

    @Override
    public List<InsurancePolicy> findAllPolicies() {
        String sql = "SELECT * FROM insurance_policy";
        List<InsurancePolicyRow> rows = jdbcTemplate.query(sql, rowMapper);
        return rows.stream()
                .map(mapper::mapRowToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public InsurancePolicy findPolicyById(Integer id) {
        String sql = "SELECT * FROM insurance_policy WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream()
                .findFirst()
                .map(mapper::mapRowToDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance policy with ID " + id + " not found"));
    }

    @Override
    public void createPolicy(InsurancePolicy policy) {
        InsurancePolicyRow row = mapper.mapDomainToRow(policy);

        String sql = "INSERT INTO insurance_policy (policy_name, policy_status, start_date, end_date, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, row.getPolicyName());
            ps.setObject(2, row.getPolicyStatus().name(), java.sql.Types.OTHER);
            ps.setObject(3, row.getStartDate());
            ps.setObject(4, row.getEndDate());
            ps.setObject(5, row.getCreatedAt());
            ps.setObject(6, row.getUpdatedAt());
            return ps;
        });
    }

    @Override
    public void updatePolicy(Integer id, InsurancePolicy policy) {
        InsurancePolicyRow updatedRow = mapper.mapDomainToRow(policy);

        String sql = "UPDATE insurance_policy SET policy_name = ?, policy_status = ?, start_date = ?, end_date = ?, updated_at = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, updatedRow.getPolicyName());
            ps.setObject(2, updatedRow.getPolicyStatus().name(), java.sql.Types.OTHER);
            ps.setObject(3, updatedRow.getStartDate());
            ps.setObject(4, updatedRow.getEndDate());
            ps.setObject(5, updatedRow.getUpdatedAt());
            ps.setInt(6, id);
            return ps;
        });
    }
}