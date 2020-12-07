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
import java.util.Map;

/**
 *
 * @author linjokin
 */
public class InMemoryBookDao implements BookDao {

    private final List<Book> books;
    private List<Book> taggedBooks;
    private HashMap<Integer, ArrayList<Tag>> tags;
    private ArrayList<Tag> allTags;

    public InMemoryBookDao() {
        this.books = new ArrayList<>();
        this.taggedBooks = new ArrayList<>();
        this.tags = new HashMap<>();
        this.allTags = new ArrayList<>();
    }

    @Override
    public List<Book> listAll() {
        return this.books;
    }

    @Override
    public int addBook(Book book) {
        book.setId(this.books.size() + 1);
        this.books.add(book);
        return book.getId();
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
        Book b = this.getBookById(bookId);
        if (b.getTags() == null) {
            b.setTags(new ArrayList<Tag>());
        }
        b.getTags().add(tag);
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
        List<Tag> foundTags = new ArrayList<>();
        for (Book b : this.books) {
            if (b.getId() == id) {
                foundTags = (ArrayList<Tag>) b.getTags();
            }
        }
        return (ArrayList<Tag>) foundTags;
    }

    @Override
    public boolean removeTagConnection(int tagId, int bookId) {
        if (this.getBookById(bookId) == null) {
            return false;
        }
        Book b = this.getBookById(bookId);

        if (b.getTags() == null) {
            return false;
        }
        List<Tag> bookTags = b.getTags();
        int index = -1;
        for (int i = 0; i < bookTags.size(); i++) {
            if (tagId == (bookTags.get(i).getId())) {
                index = i;
            }
        }
        if (index == -1) {
            return false;
        } else {
            b.getTags().remove(index);
            return true;
        }
    }

    @Override
    public List<Book> listByTag(String tag) {
        taggedBooks.clear();
        for (Map.Entry<Integer, ArrayList<Tag>> entry : tags.entrySet()) {
            for (Tag tag1 : entry.getValue()) {
                if (tag1.getName().equals(tag))
                    taggedBooks.add(getBookById(entry.getKey()));
            }
        }
        return taggedBooks;
    }

    @Override
    public ArrayList<Book> getBooksByTagId(ArrayList<Tag> selectedTags) {
        ArrayList<Book> foundBooks = new ArrayList<>();
        for (Integer bookId : tags.keySet()) {
            for (Book book : this.books) {
                if (bookId == book.getId()) {
                    for (Tag tag : selectedTags) {
                        if (tags.get(bookId).contains(tag)) {
                            foundBooks.add(book);
                        }
                    }
                }
            }
        }
        return foundBooks;
        /*List<Tag> foundTags = new ArrayList<>();
        for (Book b : this.books) {
            if (b.getId() == id) {
                foundTags = (ArrayList<Tag>) b.getTags();
            }
        }
        return (ArrayList<Tag>) foundTags;*/
    }
}
