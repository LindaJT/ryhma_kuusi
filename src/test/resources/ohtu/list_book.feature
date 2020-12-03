Feature: As a user, I can list all bookmarks

   Scenario: user can see list containing books created
      Given command list is selected
      Then list will contain "Id: 1 | Title: Kirja1 | Author: kirjailija1 | Number of pages: 100 | Current page: 1 |  Tags: "