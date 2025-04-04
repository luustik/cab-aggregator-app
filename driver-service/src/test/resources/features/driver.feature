Feature: driver-service tests for driver

  Scenario: Get driver by id
    When get driver with id 1
    Then response status is 200
    And response body contain driver
      """
        {
          "id": 1,
          "name": "Pasha",
          "email": "kkbhe@kfjbn.snb",
          "phoneNumber": "+375(29)1234567",
          "gender": "MALE"
        }
      """

  Scenario: Create driver
    Given create new driver body
    """
        {
          "name": "Milana",
          "email": "mila@kfjbn.snb",
          "phoneNumber": "+375(29)7892516",
          "gender": "FEMALE"
        }
    """
    When create new driver
    Then response status is 201
    And response body contain driver
      """
        {
          "id": 6,
          "name": "Milana",
          "email": "mila@kfjbn.snb",
          "phoneNumber": "+375(29)7892516",
          "gender": "FEMALE"
        }
      """

  Scenario: Update driver by id
    Given update driver body
    """
        {
          "name": "Eugenia",
          "email": "eugenia@kfjbn.snb",
          "phoneNumber": "+375(25)2525252",
          "gender": "FEMALE"
        }
    """
    When update driver by id 4
    Then response status is 200
    And response body contain driver
      """
        {
          "id": 4,
          "name": "Eugenia",
          "email": "eugenia@kfjbn.snb",
          "phoneNumber": "+375(25)2525252",
          "gender": "FEMALE"
        }
      """

  Scenario: Safe delete driver by id
    When safe delete driver with id 2
    Then response status is 200

  Scenario: Delete driver by id
    When delete driver with id 5
    Then response status is 200