Feature: passenger-service tests

  Scenario: Get passenger by id
    When get passenger with id 1
    Then response status is 200
    And response body passenger
      """
        {
          "id": 1,
          "name": "Kirill",
          "email": "osfbeobneo@knbeo.vneojb",
          "phone": "+375(44)5567853"
        }
      """

  Scenario: Get passenger by phone
    When get passenger with phone "+375(44)5567853"
    Then response status is 200
    And response body passenger
      """
        {
          "id": 1,
          "name": "Kirill",
          "email": "osfbeobneo@knbeo.vneojb",
          "phone": "+375(44)5567853"
        }
      """

  Scenario: Get passenger by email
    When get passenger with email "osfbeobneo@knbeo.vneojb"
    Then response status is 200
    And response body passenger
      """
        {
          "id": 1,
          "name": "Kirill",
          "email": "osfbeobneo@knbeo.vneojb",
          "phone": "+375(44)5567853"
        }
      """

  Scenario: Create passenger
    Given create new passenger body
    """
        {
          "name": "Milana",
          "email": "mila@kfjbn.snb",
          "phone": "+375(29)7892516"
        }
    """
    When create new passenger
    Then response status is 201
    And response body passenger
      """
        {
          "id": 5,
          "name": "Milana",
          "email": "mila@kfjbn.snb",
          "phone": "+375(29)7892516"
        }
      """

  Scenario: Update passenger by id
    Given update passenger body
    """
        {
          "name": "Milana",
          "email": "milka@kfjbn.snb",
          "phone": "+375(29)7892516"
        }
    """
    When update passenger by id 5
    Then response status is 200
    And response body passenger
      """
        {
          "id": 5,
          "name": "Milana",
          "email": "milka@kfjbn.snb",
          "phone": "+375(29)7892516"
        }
      """

  Scenario: soft delete passenger by id
    When soft delete passenger with id 2
    Then response status is 200

  Scenario: Delete passenger by id
    When delete passenger with id 5
    Then response status is 200