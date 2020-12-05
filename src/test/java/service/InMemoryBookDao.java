/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bookmark.bookmark_access.BookDao;
import bookmark.domain.Book;
import bookmark.domain.Tag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author linjokin
 */
public class InMemoryBookDao implements BookDao {
    
    private final List<Book> books;
    private HashMap<Integer, ArrayList<Tag>> tags;
    private ArrayList<Tag> allTags;
    
    public InMemoryBookDao() {
        this.books = new ArrayList<>();
        this.tags = new HashMap<>();
        this.allTags = new ArrayList<>();
    }

    @Override
    public List<Book> listAll() {
        return this.books;
    }

    @Override
    public int addBook(Book book) {
        this.books.add(book);
        return 0;
    }

    @Override
    public void modifyCurrentPage(int id, int page) {
        Book book = getBookById(id);
        book.setCurrentPage(page);
    }

    @Override
    public Book getBookById(int id) {
        Book foundBook = null;
        for (Book b : this.books) {
            if (b.getId() == id) {
                foundBook = b;
            }
        }
        return foundBook;
    }
    
    @Override
    public void deleteBook(int id) {
        Book book = getBookById(id);
        books.remove(book);
    }

    @Override
    public void addTag(Tag tag, int bookId) {
        if (tags.containsKey(bookId)) {
            tags.get(bookId).add(tag);
        } else {
            allTags.add(tag);
            ArrayList<Tag> newTagsList = new ArrayList<>();
            this.tags.put(bookId, newTagsList);
        }
    }

    @Override
    public Tag getTagByName(String name) {
        Tag foundTag = null;
        for (Tag tag : allTags) {
            if (tag.getName().equals(name)) {
                foundTag = tag;
            }
        }
        return foundTag;
    }

    @Override
    public ArrayList<Tag> getTagsByBookId(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeTagConnection(int tagId, int bookId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
