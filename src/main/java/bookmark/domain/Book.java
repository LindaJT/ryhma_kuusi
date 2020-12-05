package bookmark.domain;

import java.util.List;

/**
 * Representing book.
 */
public class Book extends Bookmark implements Comparable<Bookmark> {
    
    private int id;
    private String author;
    private int numberOfPages;
    private int currentPage;
    private List<Tag> tags;

    /**
     * Create book with specific title.
     *
     * @param newBookTitle
     */
    public Book(final String newBookTitle) {
        super(newBookTitle);
    }

    /**
     * Create book with specific title, author, number of pages and current page.
     *
     * @param newBookTitle
     * @param newAuthor
     * @param newNumberOfPages
     * @param newCurrentPage
     */
    public Book(final String newBookTitle,
            String newAuthor, int newNumberOfPages, int newCurrentPage) {
        super(newBookTitle);
        this.author = newAuthor;
        this.numberOfPages = newNumberOfPages;
        this.currentPage = newCurrentPage;
    }
    
    /**
     * Create book with specific id, title, author, number of pages and current page.
     *
     * @param newId
     * @param newBookTitle
     * @param newAuthor
     * @param newNumberOfPages
     * @param newCurrentPage
     */
    public Book(int newId, final String newBookTitle,
            String newAuthor, int newNumberOfPages, int newCurrentPage) {
        super(newBookTitle);
        this.id = newId;
        this.author = newAuthor;
        this.numberOfPages = newNumberOfPages;
        this.currentPage = newCurrentPage;
    }
    
    /**
     * Create book with specific title, author number of pages, current page and list of tags.
     *
     * @param newBookTitle
     * @param newAuthor
     * @param newNumberOfPages
     * @param newCurrentPage
     * @param newTagsList
     */
    public Book(final String newBookTitle,
            String newAuthor, int newNumberOfPages, int newCurrentPage, List<Tag> newTagsList) {
        super(newBookTitle);
        this.author = newAuthor;
        this.numberOfPages = newNumberOfPages;
        this.currentPage = newCurrentPage;
        this.tags = newTagsList;
    }
    
    /**
     * Create book with specific id, title, author number of pages, current page and list of tags.
     *
     * @param newId
     * @param newBookTitle
     * @param newAuthor
     * @param newNumberOfPages
     * @param newCurrentPage
     * @param newTagsList
     */
    public Book(int newId, final String newBookTitle,
            String newAuthor, int newNumberOfPages, int newCurrentPage, List<Tag> newTagsList) {
        super(newBookTitle);
        this.id = newId;
        this.author = newAuthor;
        this.numberOfPages = newNumberOfPages;
        this.currentPage = newCurrentPage;
        this.tags = newTagsList;
    }

    /**
     * @param newAuthor the author to set
     */
    public void setAuthor(String newAuthor) {
        this.author = newAuthor;
    }

    /**
     * @param newNumberOfPages the number of pages to set
     */
    public void setNumberOfPages(int newNumberOfPages) {
        this.numberOfPages = newNumberOfPages;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @return the number of pages
     */
    public int getNumberOfPages() {
        return numberOfPages;
    }
    
    /**
     * @return the currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }
    
    /**
     * 
     * @param currentPage the currentpage to set
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the tags
     */
    public List<Tag> getTags() {
        return tags;
    }
}