Feature: Insurance Policy Management

  Scenario: List all insurance policies
    Given the database contains the following insurance policies:
      | id | policyName  | policyStatus | startDate              | endDate                | createdAt              | updatedAt              |
      | 1  | Policy One  | ACTIVE       | 2024-12-01T00:00:00    | 2024-12-31T23:59:59    | 2024-12-31T23:59:59    | 2024-12-31T23:59:59    |
      | 2  | Policy Two  | INACTIVE     | 2024-11-01T00:00:00    | 2024-11-30T23:59:59    | 2024-12-31T23:59:59    | 2024-12-31T23:59:59    |
    When I request to list all insurance policies
    Then I should receive a list with the following policies:
      | policyName  | policyStatus | startDate              | endDate                | createdAt              | updatedAt              |
      | Policy One  | ACTIVE       | 2024-12-01T00:00:00    | 2024-12-31T23:59:59    | 2024-12-31T23:59:59    | 2024-12-31T23:59:59    |
      | Policy Two  | INACTIVE     | 2024-11-01T00:00:00    | 2024-11-30T23:59:59    | 2024-12-31T23:59:59    | 2024-12-31T23:59:59    |

  Scenario: Create a new insurance policy
    Given the database is empty
    When I create a new insurance policy with the following details:
      | policyName | policyStatus | startDate          | endDate            |
      | Policy A   | ACTIVE       | 2024-01-01T00:00   | 2024-12-31T23:59   |
    Then the database should contain the following insurance policies:
      | id | policyName | policyStatus | startDate          | endDate            | createdAt              | updatedAt              |
      | 1  | Policy A   | ACTIVE       | 2024-01-01T00:00   | 2024-12-31T23:59   | 2024-12-31T23:59:59    | 2024-12-31T23:59:59    |

  Scenario: Read an existing insurance policy
    Given the database contains the following insurance policies:
      | id | policyName | policyStatus | startDate          | endDate            | createdAt              | updatedAt              |
      | 1  | Policy A   | ACTIVE       | 2024-01-01T00:00   | 2024-12-31T23:59   | 2024-12-31T23:59:59    | 2024-12-31T23:59:59    |
    When I request to read the insurance policy with ID 1
    Then I should receive the following insurance policy:
      | id | policyName | policyStatus | startDate          | endDate            | createdAt              | updatedAt              |
      | 1  | Policy A   | ACTIVE       | 2024-01-01T00:00   | 2024-12-31T23:59   | 2024-12-31T23:59:59    | 2024-12-31T23:59:59    |

  Scenario: Edit an existing insurance policy
    Given the database contains the following insurance policies:
      | id | policyName | policyStatus | startDate          | endDate            | createdAt              | updatedAt              |
      | 1  | Policy A   | ACTIVE       | 2024-01-01T00:00  | 2024-12-31T23:59    | 2024-12-31T23:59:59    | 2024-12-31T23:59:59    |
    When I edit the insurance policy with ID 1 with the following details:
      | policyName | policyStatus | startDate          | endDate            | createdAt              | updatedAt              |
      | Policy B   | INACTIVE     | 2025-01-01T00:00   | 2025-12-31T23:59   | 2024-12-31T23:59:59    | 2024-12-31T23:59:59    |
    Then The insurance policy with ID 1 should have the following details:
      | policyName | policyStatus | startDate          | endDate            | createdAt              | updatedAt              |
      | Policy B   | INACTIVE     | 2025-01-01T00:00   | 2025-12-31T23:59   | 2024-12-31T23:59:59    | 2024-12-31T23:59:59    |
    And the database should contain the following insurance policies:
      | id | policyName | policyStatus | startDate          | endDate            | createdAt              | updatedAt              |
      | 1  | Policy B   | INACTIVE     | 2025-01-01T00:00   | 2025-12-31T23:59   | 2024-12-31T23:59:59    | 2024-12-31T23:59:59    |

