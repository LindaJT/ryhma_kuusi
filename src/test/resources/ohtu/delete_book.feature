Feature: As a user I can delete a book

   Scenario: user can delete a book
      Given book "poistettava" with author "poistaja" and "100" pages is created
      And command delete is selected
      When  existing book id "3" is entered
      Then  system will respond with "Book deleted succesfully!"

    Scenario: user can not delete a book if letters are entered for id
      Given command delete is selected
      When incorrect book id "eka" is entered
      Then system will respond with "Error! ID should be number."

    Scenario: user can not delete a book if non-existing id is entered
      Given command delete is selected
      When incorrect book id "32" is entered
      Then system will respond with "Error! Book ID not found."