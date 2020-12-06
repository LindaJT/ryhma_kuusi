
package service;

import bookmark.bookmark_access.BookDao;
import bookmark.domain.Book;
import bookmark.io.StubIO;
import bookmark.services.BookmarkService;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author linjokin
 */
public class BookmarkServiceTest {
    
    private StubIO io;
    private BookDao bookDao;
    private List<String> inputLines;
    private BookmarkService service;
    private ArrayList<String> emptyTagList;
    
    @Before
    public void setUp() {
        bookDao = new InMemoryBookDao();   
        inputLines = new ArrayList<>();
        io = new StubIO(inputLines);
        service = new BookmarkService(bookDao, io);
        bookDao.addBook(new Book("Kirja1", "kirjailija1", 100, 1));
        emptyTagList = new ArrayList<>();
    }
    
    @Test
    public void commandAddBookAddsABookToDatabase() {
        Boolean success = service.addBook("testi", "testaaja", "100", "0");
        assertTrue(success);
        assertEquals(2, bookDao.listAll().size());
    }
    
    @Test
    public void cannotAddBookWithEmptyInput() {
        Boolean success = service.addBook("", "testaaja", "100", "0");
        assertFalse(success);
        success = service.addBook("testi", "", "100", "0");
        assertFalse(success);
        assertEquals(1, bookDao.listAll().size());
    }
    
    @Test 
    public void cannotAddBookWithLettersInPages() {
        Boolean success = service.addBook("testi", "testaaja", "sata", "0");
        assertFalse(success);
        assertEquals(1, bookDao.listAll().size());
    }
    
    @Test
    public void addBookWithEmptyCurrentPageAddsZeroToCurrentPage() {
        Boolean success = service.addBook("testi", "testaaja", "100", "");
        assertTrue(success);
        assertEquals(2, bookDao.listAll().size());
    }
    
    @Test
    public void cannotAddBookWithNegativeCurrentPage() {
        Boolean success = service.addBook("testi", "testaaja", "100", "-10");
        assertFalse(success);
        assertEquals(1, bookDao.listAll().size());
    }

    @Test
    public void modifyingBookWithNonExcistingIdReturnsErrorMessage() {
        String answer = service.modifyCurrentPage("2", "32");
        assertEquals("Error! Book ID not found.", answer);
    }
    
    @Test
    public void addBookTagsCanAddBookWithEmptyTagsList() {
        Boolean success = service.addBookTags("test", "testaaja", "100", "1", emptyTagList);
        assertTrue(success);
        assertEquals(2, bookDao.listAll().size());
    }
    
    @Test
    public void addBookTagsCanAddBookWithMultipleTags() {
        ArrayList<String> tags = new ArrayList<>();
        tags.add("test");
        tags.add("endToEndTests");
        Boolean success = service.addBookTags("test", "testaaja", "100", "1", tags);
        assertTrue(success);
        assertEquals(2, bookDao.listAll().size());
    }
    
    @Test
    public void addBookTagsCanAddBookIfCurrentPageEmpty() {
        Boolean success = service.addBookTags("test", "testaaja", "100", "", emptyTagList);
        assertTrue(success);
        assertEquals(2, bookDao.listAll().size());
    }
    
    @Test
    public void addBookTagsCannotAddNegativeCurrentPage() {
        Boolean success = service.addBookTags("test", "testaaja", "100", "-1", emptyTagList);
        assertFalse(success);
        assertEquals(1, bookDao.listAll().size());
    }
    
    @Test
    public void addBookTagsCannotAddBookWithLettersInPages() {
        Boolean success = service.addBookTags("test", "testaaja", "10s", "", emptyTagList);
        assertFalse(success);
        assertEquals(1, bookDao.listAll().size());
    }
    
    @Test
    public void addBookTagsCannotAddBookWithLettersInCurrentPages() {
        Boolean success = service.addBookTags("test", "testaaja", "10", "10s", emptyTagList);
        assertFalse(success);
        assertEquals(1, bookDao.listAll().size());
    }
    
    @Test
    public void addBookTagsCannotAddBookIFCurrentPageGreaterThanPages() {
        Boolean success = service.addBookTags("test", "testaaja", "10", "11", emptyTagList);
        assertFalse(success);
        assertEquals(1, bookDao.listAll().size());
    }
    
    @Test
    public void addBookTagsCannotAddBookIFNoTitle() {
        Boolean success = service.addBookTags(" ", "testaaja", "10", "1", emptyTagList);
        assertFalse(success);
        assertEquals(1, bookDao.listAll().size());
    }
    
    @Test
    public void addBookTagsCannotAddBookIFNoAuthor() {
        Boolean success = service.addBookTags("testbook", " ", "10", "1", emptyTagList);
        assertFalse(success);
        assertEquals(1, bookDao.listAll().size());
    }
    
    @Test
    public void canDeleteBook() {
        String answer = service.deleteBook("0");
        assertEquals("Book deleted succesfully!", answer);
        assertEquals(0, bookDao.listAll().size());
    }
    
    @Test
    public void cannotDeleteNonExistingBook() {
        String answer = service.deleteBook("1");
        assertEquals("Error! Book ID not found.", answer);
        assertEquals(1, bookDao.listAll().size());
    }
    
    @Test
    public void cannotDeleteBookIfUserInputContainsText() {
        String answer = service.deleteBook("10s");
        assertEquals("Error! ID should be number.", answer);
        assertEquals(1, bookDao.listAll().size());
    }
    
    @Test
    public void addTagToBookAddsTagToExistingBook() {
        Boolean success = service.addTagToBook(1, "testitagi");
        assertTrue(success);
    }

}
