package bookmark.bookmark_access;

import bookmark.domain.Book;
import java.util.List;

public interface BookDao {
    
    List<Book> listAll();
    void add(Book book);
    void modifyCurrentPage(int id, int page);
    void deleteBook(int id);
    Book getBookById(int id);
}
