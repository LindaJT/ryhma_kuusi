
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
    
    @Before
    public void setUp() {
        bookDao = new InMemoryBookDao();   
        inputLines = new ArrayList<>();
        io = new StubIO(inputLines);
        service = new BookmarkService(bookDao, io);
        bookDao.addBook(new Book("Kirja1", "kirjailija1", 100, 1));    
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

}
