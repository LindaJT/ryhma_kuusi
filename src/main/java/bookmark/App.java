package bookmark;

import bookmark.io.IO;
import bookmark.bookmark_access.BookDao;
import bookmark.bookmark_access.DBDao;
import bookmark.services.BookmarkService;
import bookmark.io.ConsoleIO;
import cli.Cli;

public class App {
    
    private final Cli cli;

    public App(IO io, BookmarkService service) {
        this.cli = new Cli(io, service);
    }
    
    public void run() {
        cli.run();
    }

    public static void main(String[] args) {
        IO io = new ConsoleIO();
        BookDao dbDao = new DBDao("bookmark.db");
        BookmarkService service = new BookmarkService(dbDao, io);
        System.out.println("Welcome to BookMarkApp!");
        new App(io, service).run();
    }
}
