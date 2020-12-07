package cli;

//import bookmark.App;
//import bookmark.bookmark_access.BookDao;
//import bookmark.bookmark_access.DBDao;
//import bookmark.io.ConsoleIO;
import bookmark.domain.Tag;
import bookmark.io.IO;
import bookmark.services.BookmarkService;
import command.CommandList;
import java.util.ArrayList;
import java.util.List;

public class Cli {

    private final IO io;
    private final CommandList commandList;
    private final BookmarkService service;

    public Cli(IO io, BookmarkService service) {
        this.io = io;
        this.service = service;
        this.commandList = new CommandList();
    }

    public void run() {

        commandList.printAllCommands();

        while (true) {

            System.out.println("");

            String nextCommand = io.readLine("Give a command or leave empty to quit:");

            if (nextCommand.isEmpty()) {
                break;
            }

            switch (nextCommand) {
                case "add book":
                case "a":
                    addBook();
                    break;
                case "list":
                case "l":
                    service.listBooks();
                    break;
                case "modpage":
                case "mp":
                    modifyPage();
                    break;
                case "modtags":
                case "mt":
                    modifyTags();
                    break;
                case "help":
                case "h":
                    commandList.printAllCommands();
                    break;
                case "delete":
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
        tagFeature(title, author, pages, currentPage, tags);
    }

    private void tagFeature(String title, String author, String pages, String currentPage, ArrayList<String> tags) {
        if (service.addBookTags(title, author, pages, currentPage, tags)) {
            io.print("Book added successfully");
        } else {
            io.print("Error in adding the bookmark");
        }
    }

    private void modifyTags() {
        int id = -97531;
        try {
            id = io.readInt("Give the book's id that you want to modify the tags of: ");
        } catch (Exception e) {
            io.print("Invalid input. Aborted.");
            return;
        }
        if (service.getBookById(id) == null) {
            io.print("Invalid id. Aborted.");
            return;
        }
        List<Tag> tagList = service.getTags(id);
        io.print("Current tags: ");
        tagList.forEach((t) -> {
            io.print(t.getName());
        });

        String operation = io.readLine("Do you want to add or remove a tag (a / r)? Leave empty to go back.");
        String tag;

        switch (operation) {
            case "a":
                tag = io.readLine("Type the tag you want to add?");
                service.addTagToBook(id, tag);
                io.print("Tag added");
                break;
            case "r":
                tag = io.readLine("Type the tag you want to remove?");
                if (service.removeTagFromBook(id, tag)) {
                    io.print("Tag removed.");
                } else {
                    io.print("Error! Something went wrong!");
                }
                break;
            case "":
                break;
            default:
                io.print("Invalid command. Aborted.");
                break;
        }
    }
}
