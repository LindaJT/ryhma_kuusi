
package bookmark.cucumber;

import bookmark.App;
import bookmark.bookmark_access.BookDao;
import bookmark.bookmark_access.DBDao;
import bookmark.domain.Book;
import bookmark.io.StubIO;
import bookmark.services.BookmarkService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.After;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;

public class Stepdefs {
    
    private BookDao bookDao;
    private App app;
    private List<String> inputLines;
    private BookmarkService service;
    private StubIO io;
    
    
    @Before
    public void setUp() {
        bookDao = new DBDao("cucumberTest.db");   
        inputLines = new ArrayList<>(); 
        bookDao.addBook(new Book("Kirja1", "kirjailija1", 100, 1)); //id 1, book with id 2 will be created in add book feature   
    }
    
    @After
    public void tearDown() {
        File file = new File("cucumberTest.db");
        file.deleteOnExit();
    }
    
    @Given("command add book is selected")
    public void commandAddBookSelected() {
        inputLines.add("add book");
    }
     
    
    @When("valid title {string} and author {string} and pages {string} and current page {string} are entered")
    public void validTitleAuthorAndPagesAreEntered(String title, String author, String pages, String currentPage) {
        inputLines.add(title);
        inputLines.add(author);
        inputLines.add(pages);
        inputLines.add(currentPage);
        runApp();
    }
    
    @When("valid title {string} and author {string} and invalid pages {string} and current page {string} are entered")
    public void validTitleAuthorAndInvalidPagesAreEntered(String title, String author, String pages, String currentPage) {
        inputLines.add(title);
        inputLines.add(author);
        inputLines.add(pages);
        inputLines.add(currentPage);
        runApp();
    }
    
    @When("invalid title {string} and  valid author {string} and valid pages {string} and current page {string} are entered")
    public void invalidTitleValidAuthorAndValidPagesAreEntered(String title, String author, String pages, String currentPage) {
        inputLines.add("add book");
        inputLines.add(title);
        inputLines.add(author);
        inputLines.add(pages);
        inputLines.add(currentPage);
        runApp();
    }
    @When("valid title {string} and  invalid author {string} and valid pages {string} and current page {string} are entered")
    public void validTitleInvalidAuthorAndValidPagesAreEntered(String title, String author, String pages, String currentPage) {
        inputLines.add("add book");
        inputLines.add(title);
        inputLines.add(author);
        inputLines.add(pages);
        inputLines.add(currentPage);
        runApp();
    }
    
    @When("valid title {string} and author {string} and valid pages {string} and invalid current page {string} are entered")
    public void validTitleAuthorPageInvalidCurrentpageAreEntered(String title, String author, String pages, String currentPage) {
        inputLines.add(title);
        inputLines.add(author);
        inputLines.add(pages);
        inputLines.add(currentPage);
        runApp();
    }  
    
    @Then("system will respond with {string}")
    public void systemRespondsWithExpectedOutput(String expectedOutput) {
        System.out.println("tulostus " + io.getPrints());
        assertTrue(io.getPrints().contains(expectedOutput));
    }


    @When("command list is selected")
    public void commandListIsSelected() {
        inputLines.add("list");
        runApp();
        
    }
    @Given("command modpage is selected")
    public void commandModifyPage() {
        inputLines.add("modpage");
        
    }

    @When("command {string} is selected")
    public void customCommandSelected(String command) {
        inputLines.add(command);
        
        runApp();
    }
    
    @When("book's id {string} and current page {string} is entered")
    public void getBookByIdAndEditPage(String id, String currentPage) {
        int bookId = Integer.parseInt(id);
        int page = Integer.parseInt(currentPage);
        bookDao.modifyCurrentPage(bookId, page);
        inputLines.add(id);
        inputLines.add(currentPage);
        runApp();
    }
    @When("book's id {string} and invalid current page {string} is entered")
    public void invalidCurrentPageIsEntered(String id, String currentPage) {
        inputLines.add(id);
        inputLines.add(currentPage);
        runApp();
    }
    
    @When("invalid book's id {string} and valid current page {string} is entered")
    public void invalidIdIsEntered(String id, String currentPage) {
        inputLines.add(id);
        inputLines.add(currentPage);
        runApp();
    }   
    
    
    @Then("list will contain {string}")
    public void listContainsBook(String expected) {
        System.out.println("tulostus " + io.getPrints());
        assertTrue(io.getPrints().contains(expected));
    }
    
    @Given("command delete is selected")
    public void commandDeleteSelected() {
        inputLines.add("delete");
    }
    
    @Given("book {string} with author {string} and {string} pages is created")
    public void bookIsAdded(String title, String author, String pages) {
        inputLines.add(title);
        inputLines.add(author);
        inputLines.add(pages);
    }
    
    @When("existing book id {string} is entered")
    public void validBookIdIsEntered(String id) {
        inputLines.add(id);
        runApp();
    }
    
    @When("incorrect book id {string} is entered")
    public void incorrectBookIdIsEntered(String id) {
        inputLines.add(id);
        runApp();
    }

    
    private void runApp() {
        io = new StubIO(inputLines); 
        service = new BookmarkService(bookDao, io);
        app = new App(io, service);
        app.run();
    }

}
