package bookmark;

import bookmark.io.IO;
//import bookmark.bookmark_access.InMemoryBookDao;
import bookmark.bookmark_access.BookDao;
import bookmark.bookmark_access.DBDao;
import bookmark.services.BookmarkService;
import bookmark.io.ConsoleIO;
import command.CommandList;
import java.util.ArrayList;

public class App {

    private final IO io;
    private final BookmarkService service;
    private final CommandList commandList;

    public App(IO io, BookmarkService service) {
        this.io = io;
        this.service = service;
        this.commandList = new CommandList();
    }

    /**
     * run app
     */
    public void run() {

        commandList.printAllCommands();

        while (true) {

            System.out.println("");

            String nextCommand = io.readLine("give a command or leave empty to quit:");

            if (nextCommand.isEmpty()) {
                break;
            }

            switch (nextCommand) {
                case "add book":
                    addBook();
                    break;
                case "a":
                    addBook();
                    break;
                case "list":
                    service.listBooks();
                    break;
                case "l":
                    service.listBooks();
                    break;
                case "modpage":
                    modifyPage();
                    break;
                case "m":
                    modifyPage();
                    break;
                case "help":
                    commandList.printAllCommands();
                    break;
                case "h":
                    commandList.printAllCommands();
                    break;
                case "delete":
                    deleteBook();
                    break;
                case "d":
                    deleteBook();
                    break;    
                default:
                    io.print("unknown command");
                    break;
            }
        }
    }

    public void modifyPage() {
        io.print("If necessary, check id with the command 'list'.");
        String id = io.readLine("Book's id (empty to return):");
        if (id.equals("")) {
            return;
        }
        String page = io.readLine("Page you are currently on (empty to return):");
        if (page.equals("")) {
            return;
        }
        io.print(service.modifyCurrentPage(id, page));
    }
    public void deleteBook() {
        io.print("If necessary, check id with the command 'list'.");
        String id = io.readLine("Book's id (empty to return):");
        if (id.equals("")) {
            return;
        }
        io.print(service.deleteBook(id));
    }

    public void addBook() {
        String title = io.readLine("Book's title: ");
        String author = io.readLine("Author: ");
        String pages = io.readLine("Number of pages: ");
        String currentPage = io.readLine("Page you're currently on: ");
        ArrayList<String> tags = new ArrayList<>();
        while (true) {
            String tag = io.readLine("Enter tag or leave empty to continue");
            if (tag.trim().isEmpty()) {
                break;
            }
            tags.add(tag.trim());
        }
        if (!tags.isEmpty()) {
            tagFeature(title, author, pages, currentPage, tags);
        } else if (service.addBook(title, author, pages, currentPage)) {
            io.print("Book added successfully");

        } else {
            io.print("Error in adding the bookmark");
        }
    }
    
    private void tagFeature(String title, String author, String pages, String currentPage, ArrayList<String> tags) {
        if (service.addBookTags(title, author, pages, currentPage, tags)) {
                io.print("Book added successfully");
            } else {
                io.print("Error in adding the bookmark");
            }
    }

    public static void main(String[] args) {
        IO io = new ConsoleIO();

        BookDao dbDao = new DBDao("bookmark.db");
        BookmarkService service = new BookmarkService(dbDao, io);
        System.out.println("Welcome to BookMarkApp!");
        new App(io, service).run();
    }

}
