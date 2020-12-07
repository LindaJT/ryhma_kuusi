package bookmark.bookmark_access;

import bookmark.domain.Book;
import bookmark.domain.Tag;
import java.io.File;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class DBDaoTest {
    
    private Book book;
    private Tag tag, tag2;
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
        file.delete();
        /*if(file.delete()){  
        System.out.println(file.getName() + " deleted");   //getting and printing the file name  
        } else {  
            System.out.println("failed");  
        }*/
    }
    
    @Test
    public void addBook() {
        dbDao.addBook(book);
        assertEquals(dbDao.listAll().get(0).getAuthor(), "Carl Barks");
    }
    
    @Test
    public void addTag() {
        dbDao.addBook(book);
        dbDao.addTag(tag, 1);
        assertEquals(dbDao.getTagByName("test").getName(), "test");
    }
    
    @Test
    public void addExistingTagDoesNotAddNewTag() {
        dbDao.addBook(book);
        int bookId = dbDao.listAll().get(0).getId();
        dbDao.addTag(tag, bookId);
        dbDao.addTag(tag, bookId);
        Book foudedBook = dbDao.listAll().get(0);
        assertEquals(foudedBook.getTags().size(), 1);
    }
    
    @Test
    public void canAddMultipleTags() {
        dbDao.addBook(book);
        int bookId = dbDao.listAll().get(0).getId();
        dbDao.addTag(tag, bookId);
        dbDao.addTag(tag2, bookId);
        Book foudedBook = dbDao.listAll().get(0);
        assertEquals(foudedBook.getTags().size(), 2);
    }
    
    @Test
    public void deleteBook() {
        dbDao.addBook(book);
        dbDao.addTag(tag, 1);
        dbDao.deleteBook(1);
        assertEquals(dbDao.listAll().size(), 0);
    }
    
    @Test
    public void getCorrectBookById() {
        dbDao.addBook(book);
        Book foundedBook = dbDao.getBookById(1);
        assertEquals(book.getTitle(), foundedBook.getTitle());
        assertEquals(book.getAuthor(), foundedBook.getAuthor());
        assertEquals(book.getNumberOfPages(), foundedBook.getNumberOfPages());
        assertEquals(book.getCurrentPage(), foundedBook.getCurrentPage());
    }
    @Test
    public void listBooksByTags() {
        dbDao.addBook(book);
        dbDao.addTag(tag, 1);
        List<Book> taggedBooks = dbDao.listByTag("test");
        assertEquals(taggedBooks.size(), 1);
              
    }
    @Test
    public void canDeleteTagsFromBook() {
        dbDao.addBook(book);
        int bookId = dbDao.listAll().get(0).getId();
        dbDao.addTag(tag, bookId);
        dbDao.addTag(tag2, bookId);
        Book foudedBook = dbDao.listAll().get(0);
        assertEquals(foudedBook.getTags().size(), 2);
        
        Tag tagi = dbDao.getTagByName("test");        
        dbDao.removeTagConnection(bookId, tagi.getId());
        foudedBook = dbDao.listAll().get(0);
        assertEquals(foudedBook.getTags().size(), 1);
    }
}
