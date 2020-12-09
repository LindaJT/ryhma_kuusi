# User manual

### Getting the program to run

You can download the newest version of the application from the [releases-page](https://github.com/LindaJT/ryhma_kuusi/releases).

The program is a jar-file. You can run such a file using Java, which itself supports multiple operating systems, such as Windows and Mac OSX among various Linux- and BSD -based systems. Make sure that you have a sensibly modern version of Java installed on your system.

Run the program using your system's command line (for example, in Windows it would be 'cmd.exe'). On the command line, navigate to the folder where you downloaded the jar-file and run the command `java -jar shadowJar.jar` (where the last part is the file's name -- change it if needed). Run the app by command **`gradle run`**
or **`./gradlew run`** depending which works on your computer.

Please note that using the application will cause a database file 'bookmark.db' to be created in the jar-file's folder. This file contains your saved data regarding the bookmarks, so avoid manipulating it in any way.



### Using the application

The functionality of the application is listed when the application is launched. 

Adding a book to bookmarks can be done with the command **`add book`** or the shortcut **`a`**. The app then asks for the title, author and page count of the book. This information is mandatory in order to add book. After that, app asks for current page and tags. These are optional and can be left empty, in which case current page will be 0.

All recorded bookmarks can be listed using the command **`list`** or the shortcut **`l`**. In this case, a list of information for all books is printed, including id, name, author, page number, current page and tags.

The current page number can be modified using the command **`modpage`** or the shortcut **`mp`**. Book selection occurs based on the id which user can find out using the `list` command. The new page number may be at most the stated page count of the book. 

Tags can be added or deleted by usig the command **`modtags`** or the shortcut **`mt`**. Similarly to editing current page, user must first give book's id. After that app lists tags given to the book and asks user to select if they want to add **`a`** or remove **`r`**  tags. Action can be cancelled by leaving selection empty. If add is selected, app will next ask which tag user wants to add. If remove is selected, app will ask which tag user wants to delete. 

User can search books that have certain tag by choosing the command **`taglist`** or the shortcut **`tl`**. The app then asks user to type tag by which books are listed. User can also search books by giving only part of the tag. App will then list books matching the condition or tell that there are no books with that tag.

It is possible to permanently delete a book by choosing the command **`delete`** or the shortcut **`d`**. The app will then ask the id of the book user wants to delete. Leaving selection empty will return to command selection.

The command **`help`** or the shortcut **`h`** prints a list of program functionality and commands to use them.



### Building from source

Use command **`./gradlew ShadowJar`**  in the directory where the code of the application is located.
