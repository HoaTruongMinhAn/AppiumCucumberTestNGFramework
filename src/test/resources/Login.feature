Feature: Login scenarios

  @test
  Scenario Outline: Login with invalid username
    When I enter username as "<username>"
    And I enter password as "<password>"
    And I login
    Then login should fail with an error "<err>"
    Examples:
      | username | password     | err                                                          |
      | aaa      | secret_sauce | Username and password do not match any user in this service. |
      | bbb      | secret_sauce | Username and password do not match any user in this service. |

  Scenario Outline: Login with invalid password
    When I enter username as "<username>"
    And I enter password as "<password>"
    And I login
    Then login should fail with an error "<err>"
    Examples:
      | username      | password | err                                                                  |
      | standard_user | aaa      | Username and password do not match any user in this service. xxx     |
      | standard_user | bbb      | Username and password do not match any user in this service. yyyyyyy |

  Scenario Outline: Login with valid username and password
    When I enter username as "<username>"
    And I enter password as "<password>"
    And I login
    Then I should see Setting button displayed
    Examples:
      | username      | password     |
      | standard_user | secret_sauce |