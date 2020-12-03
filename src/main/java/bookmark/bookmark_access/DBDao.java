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

/**
 *
 * @author kaila
 */
public class DBDao implements BookDao {

    private final String url;
    private final List<Book> books;

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
            System.err.println(e.getMessage());
        } finally {
            closeConnection(connection);
        }
        return books;
    }

    /**
     * @param book - Book object to be added
     */
    @Override
    public int addBook(Book book) {
        Connection connection = connect();
        int bookId = addBookStatement(connection, book);
        closeConnection(connection);
        return bookId;
    }
    
    @Override
    public void addTag(Tag tag, int bookId) {
        Connection connection = connect();
        int tagId = addTagStatement(connection, tag);
        insertIntoBookTagMappingTable(bookId, tagId);
        closeConnection(connection);
    }

    @Override
    public Book getBookById(int id) {
        
        Connection connection = connect();
        ResultSet rs;
        Book book = null;

        try {
            Statement statement = connection.createStatement();
            String sql = "select * from book WHERE id = " + id;
            rs = statement.executeQuery(sql);
            book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getInt("pages"),
                    rs.getInt("currentpage"));
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        } finally {
            closeConnection(connection);
        }
        
        return book;

    }

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
            System.err.println(e.getMessage());
        } finally {
            closeConnection(connection);
        }
    }
    @Override
    public void deleteBook(int id) {
        
        Connection connection = connect();
        
        try {
            PreparedStatement p = connection.prepareStatement("DELETE FROM Book WHERE id = (?)");
            p.setInt(1, id);
            p.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            closeConnection(connection);
        }
        
    }

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
                System.err.println(e.getMessage());
            } finally {
                closeConnection(connection);
            }
        }
    }

    private Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return connection;
    }
    
    private void createBookTable(Statement statement) throws SQLException {
        statement.execute("CREATE TABLE Book ("
            + "id INTEGER PRIMARY KEY, "
            + "title TEXT NOT NULL, "
            + "author TEXT NOT NULL, "
            + "pages INTEGER, "
            + "currentpage INTEGER)");
    }
    
    private void createTagTable(Statement statement) throws SQLException {
        statement.execute("CREATE TABLE Tag ("
            + "id INTEGER PRIMARY KEY, "
            + "name TEXT NOT NULL, "
            + "UNIQUE(name))");
    }
    
    private void createBookTagMappingTaple(Statement statement) throws SQLException {
        statement.execute("CREATE TABLE Book_tag_mapping ("
            + "book_id INTEGER, "
            + "tag_id INTEGER)");
    }

    private int addBookStatement(Connection connection, Book book) {
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
            System.err.println(e.getMessage());
        }
        return 0;
    }
    
    private int addTagStatement(Connection connection, Tag tag) {
        final int eka = 1;
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
            int tagId = getTagId(connection, tag);
            if (tagId != 0) {
                return tagId;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }
    
    private int getTagId(Connection connection, Tag tag) {
         final int eka = 1;
        try {
            String query = "select * Tag WHERE name = (?)";
            PreparedStatement p = connection
                .prepareStatement(query);
            p.setString(eka, tag.getName());
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }
    
    private ArrayList<Tag> getTagsByBookId(Connection connection, int id) {
        final int eka = 1;
        ArrayList<Tag> tags = new ArrayList<>();
        try {
            String query = "SELECT tag.name FROM Tag JOIN Book_tag_mapping ON tag_id = (?)";
            PreparedStatement p = connection
                .prepareStatement(query);
            p.setInt(eka, id);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                tags.add(new Tag(rs.getString("name")));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return tags;
    }
    
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
            System.err.println(e.getMessage());
        }
        closeConnection(connection);
        return tag;
    }

    private ResultSet getBooksResultSet(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery("select * from book");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    private void insertIntoBookTagMappingTable(int bookId, int tagId) {
        final int eka = 1;
        final int toka = 2;
        Connection connection = connect();
        try {
            String query = "INSERT INTO Book_tag_mapping (book_id, tag_id) VALUES (?, ?)";
            PreparedStatement p = connection
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            p.setInt(eka, bookId);
            p.setInt(toka, tagId);
            p.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
