Feature: As a user I can add and delete tags to books

   Scenario: user can add a new book with tags
      Given command add book is selected
      When  valid title "Harry Potter" and author "JK Rowling" and pages "100" and tag "eka" are entered
      Then  system will respond with "Book added successfully"

    Scenario: user can add a tag to an existing book
      Given command mt is selected
      When valid book id 2 and valid tag name "toka" are entered and a is selected
      Then system will respond with "Tag added"

    Scenario: user can not add a tag to a non-existing book 
      Given command mt is selected
      When not valid book id 50 and valid tag name "kolmas" are entered and a is selected
      Then system will respond with "Invalid id. Aborted."

    Scenario: user can remove existing tag from an existing book
      Given command mt is selected
      When valid book id 2 and existing tag name "toka" are entered and r is selected
      Then system will respond with "Tag removed."

    Scenario: user can not remove non-existing tag from an existing book
      Given command mt is selected
      When valid book id 2 and non-existing tag name "kolmas" are entered and r is selected
      Then system will respond with "Error! Something went wrong!"

    Scenario: user can not remove existing tag from an non-existing book
      Given command mt is selected
      When invalid book id 50 and existing tag name "eka" are entered and r is selected
      Then system will respond with "Invalid id. Aborted."

    Scenario: error message is given if command is not a or r
      Given command mt is selected
      When valid book id 2 and existing tag name "toka" are entered and o is selected
      Then system will respond with "Invalid command. Aborted."