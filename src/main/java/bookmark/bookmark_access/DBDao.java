package bookmark.bookmark_access;

import bookmark.domain.Book;
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
                Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getInt("pages"),
                        rs.getInt("currentpage"));
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
    public void add(Book book) {
        Connection connection = connect();
        addBookStatement(connection, book);
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
                statement.execute("CREATE TABLE Book (id INTEGER PRIMARY KEY, "
                        + "title TEXT NOT NULL, author TEXT NOT NULL, pages INTEGER, currentpage INTEGER)");
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

    private void addBookStatement(Connection connection, Book book) {
        final int eka = 1;
        final int toka = 2;
        final int kolmas = 3;
        final int neljas = 4;
        try {
            PreparedStatement p = connection
                    .prepareStatement("INSERT INTO Book (title,author,pages, currentpage) VALUES (?,?,?,?)");
            p.setString(eka, book.getTitle());
            p.setString(toka, book.getAuthor());
            p.setInt(kolmas, book.getNumberOfPages());
            p.setInt(neljas, book.getCurrentPage());
            p.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
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
