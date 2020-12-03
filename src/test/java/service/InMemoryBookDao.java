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
import java.util.List;

/**
 *
 * @author linjokin
 */
public class InMemoryBookDao implements BookDao {
    
    private final List<Book> books;
    private List<Tag> tags;
    
    public InMemoryBookDao() {
        this.books = new ArrayList<>();
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
    public void addTag(Tag tag, int i) {
        this.tags.add(tag);
    }

    @Override
    public Tag getTagByName(String name) {
        Tag foundTag = null;
        for (Tag t : this.tags) {
            if (t.getName().equals(name)) {
                foundTag = t;
            }
        }
        return foundTag;
    }
}
