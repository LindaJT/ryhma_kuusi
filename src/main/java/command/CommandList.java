package command;

import java.util.ArrayList;

class Command { 

    private String name;
    private String shortcut;
    private String description;

    Command(String commandName,
            String commandShortcut, String commandDescription) {
        this.name = commandName;
        this.shortcut = commandShortcut;
        this.description = commandDescription;
    }

        @Override
        public String toString() {
            return name + shortcut + description;
        } 
}

    public class CommandList {

        private ArrayList<Command> commands;

        public CommandList() {
            this.commands = new ArrayList<>();
            commands.add(new Command("| Command   |", " Shortcut  |", " Description"));
            commands.add(new Command("|===========|", "===========|", "========================================="));
            commands.add(new Command("| add book  |", " a         |", " Add a book to bookmarks."));
            commands.add(new Command("| list      |", " l         |", " List all bookmarks."));
            commands.add(new Command("| modpage   |", " mp        |", " Modify current page."));
            commands.add(new Command("| modtags   |", " mt        |", " Modify a bookmark's tags."));
            commands.add(new Command("| help      |", " h         |", " Show commands, shortcuts and descriptions."));
            commands.add(new Command("| delete    |", " d         |", " Delete book permanently"));
        }

        public void printAllCommands() {
            
            for (int i = 0; i < commands.size(); i++) {
                System.out.println(commands.get(i).toString()); //commandListing += commands[i].toString() + "\n";
            }
        }
    }
