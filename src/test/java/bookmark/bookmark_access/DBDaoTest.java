package bookmark.bookmark_access;

import bookmark.domain.Book;
import bookmark.domain.Tag;
import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class DBDaoTest {
    
    private Book book;
    private Tag tag,tag2;
    private BookDao dbDao;
    
    @Before
    public void setUp() {
        book = new Book("Lost in the Andes!", "Carl Barks", 32, 0);
        tag = new Tag("test");
        tag2 = new Tag("test2");
        this.dbDao = new DBDao("test.db");
    }
    
    @After
    public void tearDown() {
        File file = new File("test.db");
        if(file.delete()){  
        System.out.println(file.getName() + " deleted");   //getting and printing the file name  
        } else {  
            System.out.println("failed");  
        }
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
    
    @Test
    public void deleteBookTest() {
        dbDao.addBook(book);
        dbDao.addTag(tag, 1);
        dbDao.deleteBook(1);
        assertEquals(dbDao.listAll().size(), 0);
    }
}
