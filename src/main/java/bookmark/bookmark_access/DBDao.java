package bookmark.bookmark_access;

import bookmark.domain.Book;
import bookmark.domain.Tag;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class DBDao implements BookDao {

    private final String url;
    private final List<Book> books;

    /**
     * Create database with specific name
     *
     * @param dbName
     */
    public DBDao(String dbName) {
        url = "jdbc:sqlite:" + dbName;
        books = new ArrayList<>();
        createDatabaseAndTablesIfDoNotExists(dbName);
    }

    /**
     * @return the list containing all books
     */
    @Override
    public List<Book> listAll() {
        books.clear();
        Connection connection = connect();
        try {
            ResultSet rs = getBooksResultSet(connection);
            while (rs.next()) {
                ArrayList<Tag> tags = getTagsByBookId(connection, rs.getInt("id"));
                Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getInt("pages"),
                        rs.getInt("currentpage"), tags);
                books.add(book);
            }
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        } finally {
            closeConnection(connection);
        }
        return books;
    }

    /**
     * @param book - Book object to be added
     * @return books id
     */
    @Override
    public int addBook(Book book) {
        Connection connection = connect();
        int bookId = 0;
        try {
            bookId = addBookStatement(connection, book);
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        } finally {
            closeConnection(connection);
        }
        return bookId;
    }
    
    /**
     * Add a tag to database and
     * create connection between book and tag using bookid
     * 
     * @param tag
     * @param bookId
     */
    @Override
    public void addTag(Tag tag, int bookId) {
        Connection connection = connect();
        try {
            int tagId = addTagStatement(connection, tag);
            insertIntoBookTagMappingTable(connection, bookId, tagId);
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        } finally {
            closeConnection(connection);
        }
    }
    
    /**
     * Find book by id
     * @param id
     * @return foudend book
     */
    @Override
    public Book getBookById(int id) {
        Connection connection = connect();
        ResultSet rs;
        Book book = null;
        final int eka = 1;
        try {
            PreparedStatement p = connection.prepareStatement("select * from book WHERE id = (?)");
            p.setInt(eka, id);
            rs = p.executeQuery();
            book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getInt("pages"),
                    rs.getInt("currentpage"));
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        } finally {
            closeConnection(connection);
        }
        return book;
    }
    
    /**
     * Modify books current page
     * @param id book id
     * @param page current page number to set
     */
    @Override
    public void modifyCurrentPage(int id, int page) {
        Connection connection = connect();
        try {
            PreparedStatement p = connection.prepareStatement("UPDATE Book "
                    + "SET currentpage = (?) WHERE id = (?)");
            p.setInt(1, page);
            p.setInt(2, id);
            p.executeUpdate();
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        } finally {
            closeConnection(connection);
        }
    }
    
    /**
     * Delete book from database
     * @param id book id
     */
    @Override
    public void deleteBook(int id) { 
        Connection connection = connect();
        try {
            deleteBookFromDatabase(connection, id);
            deleteTagFromDatabase(connection, id);
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        } finally {
            closeConnection(connection);
        }
    }
    
    /**
     * Find tag from database by name
     * @param tagName
     * @return founded tag
     */
    @Override
    public Tag getTagByName(String tagName) {
        final int eka = 1;
        Tag tag = null;
        ResultSet rs;
        Connection connection = connect();
        try {
            PreparedStatement p = connection
                .prepareStatement("select * FROM tag WHERE name = (?)");
            p.setString(eka, tagName);
            rs = p.executeQuery();
            tag = new Tag(rs.getString("name"));
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        } finally {
            closeConnection(connection);
        }
        return tag;
    }
    
     /**
      * Connect to database
      * @return connection
      */
    private Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        }
        return connection;
    }

    /**
     * Create database with specific name and
     * create book, tag and bookTagMapping tables
     * @param newDBname 
     */
    private void createDatabaseAndTablesIfDoNotExists(String newDBname) {
        Connection connection = null;
        File file = new File(newDBname);
        if (!file.exists()) {
            try {
                connection = connect();
                Statement statement = connection.createStatement();
                createBookTable(statement);
                createTagTable(statement);
                createBookTagMappingTaple(statement);
            } catch (SQLException e) {
                //System.err.println(e.getMessage());
            } finally {
                closeConnection(connection);
            }
        }
    }
    
    /**
     * Function to create book table
     * @param statement
     * @throws SQLException 
     */
    private void createBookTable(Statement statement) throws SQLException {
        statement.execute("CREATE TABLE Book ("
            + "id INTEGER PRIMARY KEY, "
            + "title TEXT NOT NULL, "
            + "author TEXT NOT NULL, "
            + "pages INTEGER, "
            + "currentpage INTEGER)");
    }
    
    /**
     * Function to create tag table
     * @param statement
     * @throws SQLException 
     */
    private void createTagTable(Statement statement) throws SQLException {
        statement.execute("CREATE TABLE Tag ("
            + "id INTEGER PRIMARY KEY, "
            + "name TEXT NOT NULL, "
            + "UNIQUE(name))");
    }
    
    /**
     * Function to create bookTagMapping table
     * @param statement
     * @throws SQLException 
     */
    private void createBookTagMappingTaple(Statement statement) throws SQLException {
        statement.execute("CREATE TABLE Book_tag_mapping ("
            + "book_id INTEGER, "
            + "tag_id INTEGER)");
    }

    /**
     * Function to add a book to database
     * @param connection
     * @param book
     * @return 
     */
    private int addBookStatement(Connection connection, Book book) throws SQLException {
        final int eka = 1;
        final int toka = 2;
        final int kolmas = 3;
        final int neljas = 4;
        try {
            String query = "INSERT INTO Book (title,author,pages,currentpage) VALUES (?,?,?,?)";
            PreparedStatement p = connection
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            p.setString(eka, book.getTitle());
            p.setString(toka, book.getAuthor());
            p.setInt(kolmas, book.getNumberOfPages());
            p.setInt(neljas, book.getCurrentPage());
            p.executeUpdate();
            ResultSet rs = p.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        }
        return 0;
    }
    
    /**
     * Function to add a tag to database
     * @param connection
     * @param tag
     * @return 
     */
    private int addTagStatement(Connection connection, Tag tag) throws SQLException {
        final int eka = 1;
        int tagId = getTagId(connection, tag);
        if(tagId != 0) {
            return tagId;
        }
        try {
            String query = "INSERT INTO Tag (name) VALUES (?)";
            PreparedStatement p = connection
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            p.setString(eka, tag.getName());
            p.executeUpdate();
            ResultSet rs = p.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        }
        return 0;
    }
    
    /**
     * Function to get tag id by tag name
     * @param connection
     * @param tag
     * @return 
     */
    private int getTagId(Connection connection, Tag tag) {
        final int eka = 1;
        try {
            String query = "select * FROM Tag WHERE name = (?)";
            PreparedStatement p = connection
                .prepareStatement(query);
            p.setString(eka, tag.getName());
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        }
        return 0;
    }
    
    /**
     * Function to retrieve book tags  by book id from database
     * @param connection
     * @param id
     * @return list filled with book tags
     */
    private ArrayList<Tag> getTagsByBookId(Connection connection, int id) {
        final int eka = 1;
        ArrayList<Tag> tags = new ArrayList<>();
        try {
            String query = "SELECT tag.name FROM Tag JOIN Book_tag_mapping ON Tag.id = Book_tag_mapping.tag_id WHERE book_id = (?)";
            PreparedStatement p = connection
                .prepareStatement(query);
            p.setInt(eka, id);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                tags.add(new Tag(rs.getString("name")));
            }
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        }
        return tags;
    }
    
    /**
     * Helper function to get all books from book table
     * @param connection
     * @return statement to get books
     */
    private ResultSet getBooksResultSet(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery("select * from book");
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        }
        return null;
    }
    
    private void insertIntoBookTagMappingTable(Connection connection, int bookId, int tagId) {
        final int eka = 1;
        final int toka = 2;
        try {
            String query = "INSERT INTO Book_tag_mapping (book_id, tag_id) VALUES (?, ?)";
            PreparedStatement p = connection
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            p.setInt(eka, bookId);
            p.setInt(toka, tagId);
            p.executeUpdate();
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        }
    }
    
    /**
     * Close connection
     * @param connection 
     */
    private void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        }
    }
    
    private void deleteBookFromDatabase(Connection connection, int bookId) throws SQLException {
        PreparedStatement p = connection.prepareStatement("DELETE FROM Book WHERE id = (?)");
        p.setInt(1, bookId);
        p.execute();
    }
    
    private void deleteTagFromDatabase(Connection connection, int bookId) throws SQLException {
        try {
            PreparedStatement p = connection.prepareStatement("DELETE FROM Book_tag_mapping WHERE book_id = (?)");
            p.setInt(1, bookId);
            p.execute();
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        }
    }
}
