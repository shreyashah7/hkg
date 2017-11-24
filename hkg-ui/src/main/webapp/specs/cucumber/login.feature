Feature: Validate Login Feature.

Scenario: When I enter login credential, it should be on dashboard
    Given I am on login page
    When I enter login credential
    And click 'Sign in' button
    Then I should navigate to dashboard

Scenario: When I enter no login credential data, it should display error message
    Given I am on login page
    When click 'Sign in' button
    Then It should display error messages
