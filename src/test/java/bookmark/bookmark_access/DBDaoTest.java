package bookmark.bookmark_access;

import bookmark.domain.Book;
import bookmark.domain.Tag;
import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


/**
 *
 * @author kaila
 */
public class DBDaoTest {
    
    private Book book;
    private Tag tag;
    private BookDao dbDao;
    
    @Before
    public void setUp() {
        book = new Book("Lost in the Andes!", "Carl Barks", 32, 0);
        tag = new Tag("test");
        this.dbDao = new DBDao("test.db");
    }
    
    @After
    public void tearDown() {
        File file = new File("test.db");
        file.deleteOnExit();
    }
    
    @Test
    public void addBookTest() {
        dbDao.addBook(book);
        assertEquals(dbDao.listAll().get(0).getAuthor(), "Carl Barks");
    }
    
    @Test
    public void addTagTest() {
        dbDao.addBook(book);
        dbDao.addTag(tag, 1);
        assertEquals(dbDao.getTagByName("test").getName(), "test");
    }

}
