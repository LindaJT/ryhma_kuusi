/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookmark.services;

import bookmark.bookmark_access.BookDao;
import bookmark.domain.Book;
import java.util.Collections;
import java.util.List;
import bookmark.io.IO;


public class BookmarkService {

    private final IO io;
    private final BookDao bookDao;

    public BookmarkService(BookDao bookDao, IO io) {
        this.bookDao = bookDao;
        this.io = io;
    }

    /**
     * calls BookDao's method add to add book
     *
     * @param inputTitle title user entered
     * @param inputAuthor author user entered
     * @param inputPages pages user entered
     * @param currentPage Page number user is on
     * @return return true if inputs were valid and book was added, otherwise
     * false
     */
    public boolean addBook(String inputTitle, String inputAuthor, String inputPages, String currentPage) {
        
        if (isBlankOrEmpty(currentPage)) {
            currentPage = "0";
        }

        String regex = "^[0-9]+";

        if (!inputPages.matches(regex) || !currentPage.matches(regex)) {
            return false;
        } else if (Integer.parseInt(inputPages) < Integer.parseInt(currentPage)) {
            return false;
        } else if (Integer.parseInt(currentPage) < 0) {
            return false;
        } else if (isBlankOrEmpty(inputTitle) || isBlankOrEmpty(inputAuthor) || isBlankOrEmpty(inputPages)) {
            return false;
        } else {
            int pages = Integer.parseInt(inputPages);
            int curPage = Integer.parseInt(currentPage);
            Book book = new Book(inputTitle, inputAuthor, pages, curPage);
            bookDao.add(book);

            return true;
        }
    }

    public String modifyCurrentPage(String idString, String pageString) {

        int id;
        int page;
        Book book;

        try {
            id = Integer.parseInt(idString);
            page = Integer.parseInt(pageString);
        } catch (NumberFormatException e) {
            return "Error! ID and page should be numbers.";
        }

        try {
            book = bookDao.getBookById(id);
            if (book.equals(null)) {
                return "placeholder text";
            }
        } catch (Exception e) {
            return "Error! Book ID not found.";
        }

        if (book.getNumberOfPages() < page) {
            return "Error! Current page cannot be higher than the number of pages.";
        } else if (page < 0) {
            return "Error! Current page cannot lower than zero.";
        } else {
            bookDao.modifyCurrentPage(id, page);
            return "Book's progress successfully updated!";
        }
    }
    /**
     * deletes book from database
     * * @param id user entered
     */
    public String deleteBook(String idString) {
        
        int id;
        Book book;
        
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            return "Error! ID should be number.";
        }
        
        try {
            book = bookDao.getBookById(id);
            if (book.equals(null)) {
                return "placeholder text";
            }
        } catch (Exception e) {
            return "Error! Book ID not found.";
        }    
        
        bookDao.deleteBook(id);
        return "Book deleted succesfully!";
        
        
    }

    /**
     * calls BookDao's method listAll to list books
     */
    public void listBooks() {

        List<Book> bookList = bookDao.listAll();
        Collections.sort(bookList);
        bookList.forEach((book) -> {
            io.print("Id: " + book.getId()
                    + " | Title: " + book.getTitle()
                    + " | Author: " + book.getAuthor()
                    + " | Number of pages: " + book.getNumberOfPages()
                    + " | Current page: " + book.getCurrentPage());
        });
    }

    private boolean isBlankOrEmpty(String input) {
        if (input.trim().isEmpty() || input == null) {
            return true;
        }
        return false;
    }
}
