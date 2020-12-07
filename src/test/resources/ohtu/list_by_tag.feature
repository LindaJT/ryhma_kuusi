Feature: As a user, I can search books by tag

   Scenario: user can see list containing books with tag
      Given command taglist is selected
      When valid tag "eka" is entered
      Then list will contain "Id: 2 | Title: Harry Potter | Author: JK Rowling | Number of pages: 100 | Current page: 0 | Tags: eka "

   Scenario: user can search with part of tag
      Given command taglist is selected
      When valid tag "e" is entered
      Then list will contain "Id: 2 | Title: Harry Potter | Author: JK Rowling | Number of pages: 100 | Current page: 0 | Tags: eka "

   Scenario: no books found with nonexisting tag
      Given command taglist is selected
      When nonexisting tag "jännä" is entered
      Then system will respond with "no books found with tag jännä"

