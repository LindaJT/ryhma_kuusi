Feature: As a user I can add and delete tags to books

   Scenario: user can add a book with tags
      Given command add book is selected
      When  valid title "Harry Potter" and author "JK Rowling" and pages "100" and tag "tägi" are entered
      Then  system will respond with "Book added successfully"