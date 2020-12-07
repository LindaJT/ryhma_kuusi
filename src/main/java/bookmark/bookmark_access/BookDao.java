package bookmark.bookmark_access;

import bookmark.domain.Book;
import bookmark.domain.Tag;
import java.util.ArrayList;
import java.util.List;

public interface BookDao {
    
    List<Book> listAll();
    List<Book> listByTag(String tag);
    int addBook(Book book);
    void modifyCurrentPage(int id, int page);
    void deleteBook(int id);
    Book getBookById(int id);
    void addTag(Tag tag, int id);
    Tag getTagByName(String name);
    ArrayList<Tag> getTagsByBookId(int id);
    ArrayList<Book> getBooksByTagId(ArrayList<Tag> tags);
    boolean removeTagConnection(int tagId, int bookId);
}
