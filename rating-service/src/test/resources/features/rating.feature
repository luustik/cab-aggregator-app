Feature: rating-service tests

  Scenario: Get rating by id
    When get rating with id 1
    Then response status is 200
    And response body rating
      """
        {
          "id": 1,
          "rideId": 4,
          "userId": 1,
          "rating": 10,
          "comment": "",
          "userRole": "DRIVER"
        }
      """

  Scenario: Get rating by ride id and role
    When get rating with role "DRIVER" and rideId 4
    Then response status is 200
    And response body rating
      """
        {
          "id": 1,
          "rideId": 4,
          "userId": 1,
          "rating": 10,
          "comment": "",
          "userRole": "DRIVER"
        }
      """

  Scenario: Create rating
    Given create new rating body
    """
        {
          "rideId": 2,
          "userId": 1,
          "rating": 10,
          "comment": "",
          "userRole": "DRIVER"
        }
    """
    When create new rating
    Then response status is 201
    And response body rating
      """
        {
          "id": 6,
          "rideId": 2,
          "userId": 1,
          "rating": 10,
          "comment": "",
          "userRole": "DRIVER"
        }
      """

  Scenario: Update rating by id
    Given update rating body
    """
        {
          "rating": 2,
          "comment": "bad"
        }
    """
    When update rating by id 6
    Then response status is 200
    And response body rating
      """
        {
          "id": 6,
          "rideId": 2,
          "userId": 1,
          "rating": 2,
          "comment": "bad",
          "userRole": "DRIVER"
        }
      """

  Scenario: Delete rating by id
    When delete rating with id 6
    Then response status is 204

  Scenario: Calculate avg rating user
    When calculate avg rating user with id 1 and role "DRIVER"
    Then response status is 200
    And response body avg rating
      """
        {
          "id": 1,
          "avgRating": 10
        }
      """