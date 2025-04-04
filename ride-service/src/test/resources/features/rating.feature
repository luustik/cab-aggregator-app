Feature: ride-service tests

  Scenario: Get ride by id
    When get ride with id 1
    Then response status is 200
    And response body ride for get method
      """
        {
          "id": 1,
          "driverId": 1,
          "passengerId": 3,
          "departureAddress": "Independency 173",
          "arrivalAddress": "Yakuba Kolasa 53",
          "status": "COMPLETED",
          "orderDateTime": "2024-10-07T15:45:30",
          "cost": "34.00"
        }
      """

  Scenario: Create ride
    Given create new ride body
    """
        {
          "driverId": 1,
          "passengerId": 1,
          "departureAddress": "Test 1",
          "arrivalAddress": "Test 2"
        }
    """
    When create new ride
    Then response status is 201
    And response body ride
      """
        {
          "id": 6,
          "driverId": 1,
          "passengerId": 1,
          "departureAddress": "Test 1",
          "arrivalAddress": "Test 2",
          "status": "CREATED",
          "orderDateTime": "2024-01-01T00:00:00",
          "cost": "1.00"
        }
      """

  Scenario: Update ride by id
    Given update ride body
    """
        {
    "driverId": 1,
    "passengerId": 1,
    "departureAddress": "Test 2",
    "arrivalAddress": "Test 1"
}
    """
    When update ride by id 6
    Then response status is 200
    And response body ride
      """
        {
          "id": 6,
          "driverId": 1,
          "passengerId": 1,
          "departureAddress": "Test 2",
          "arrivalAddress": "Test 1",
          "status": "CREATED",
          "orderDateTime": "2024-01-01T00:00:00",
          "cost": "1.00"
        }
      """

  Scenario: Update ride status by id
    Given updated ride status
    """
    "CANCELLED"
    """
    When update ride status by id 6
    Then response status is 200
    And response body ride
      """
        {
          "id": 6,
          "driverId": 1,
          "passengerId": 1,
          "departureAddress": "Test 2",
          "arrivalAddress": "Test 1",
          "status": "CANCELLED",
          "orderDateTime": "2024-01-01T00:00:00",
          "cost": "1.00"
        }
      """

  Scenario: Delete ride by id
    When delete ride with id 6
    Then response status is 200