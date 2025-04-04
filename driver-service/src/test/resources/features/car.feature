Feature: driver-service tests for car

  Scenario: Get car by id
    When get car with id 1
    Then response status is 200
    And response body car
      """
        {
          "id": 1,
          "color": "red",
          "model": "audi",
          "carNumber": "7930AB-7",
          "driverId": 1
        }
      """

  Scenario: Get car by carNumber
    When get car with carNumber "7930AB-7"
    Then response status is 200
    And response body car
      """
        {
          "id": 1,
          "color": "red",
          "model": "audi",
          "carNumber": "7930AB-7",
          "driverId": 1
        }
      """

  Scenario: Create car
    Given create new car body
    """
        {
          "color": "pink",
          "model": "chevrolet",
          "carNumber": "1604KL-5",
          "driverId": 1
        }
    """
    When create new car
    Then response status is 201
    And response body car
      """
        {
          "id": 5,
          "color": "pink",
          "model": "chevrolet",
          "carNumber": "1604KL-5",
          "driverId": 1
        }
      """

  Scenario: Update car by id
    Given update car body
    """
        {
          "color": "blue",
          "model": "chevrolet",
          "carNumber": "1604KL-5",
          "driverId": 1
        }
    """
    When update car by id 5
    Then response status is 200
    And response body car
      """
        {
          "id": 5,
          "color": "blue",
          "model": "chevrolet",
          "carNumber": "1604KL-5",
          "driverId": 1
        }
      """

  Scenario: Delete car by id
    When delete car with id 5
    Then response status is 200