Feature: The task should convert XML to HTML correctly
  The given XML file should be correctly converted to HTML

  Scenario: Using the default XSL file
    Given I initialise the task with the default file
    And I load the XML file testCheckstyle.xml
    And I set the HTML report file to testCheckstyle.html
    When I run the task
    Then The resulting file should equal expectedDefaultCheckstyle.html

  Scenario: Using the default XSL file with no HTML destination
    Given I initialise the task with the default file
    And I load the XML file testCheckstyle.xml
    When I run the task
    Then The state should be skipped with message 'SKIPPED: HTML report destination is undefined'

  Scenario: Using a custom XSL file
    Given I initialise the task with the anotherXSLFile.xsl file
    And I load the XML file testCheckstyle.xml
    And I set the HTML report file to anotherTestCheckstyle.html
    When I run the task
    Then The resulting file should equal expectedAnotherCheckstyle.html

  Scenario: Using the default XSL file with no XML report
    Given I initialise the task with the default file
    And I set the HTML report file to noTestCheckstyle.html
    When I run the task
    Then The state should be skipped with message 'SKIPPED: No report to transform'
