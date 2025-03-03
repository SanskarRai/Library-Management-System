import java.io.*;
import java.util.*;

class Book implements Serializable { // Implements Serializable for file storage
    int id;
    String title;
    String author;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Title: " + title + ", Author: " + author;
    }
}

class Library {
    private final Map<Integer, Book> books = new HashMap<>();
    private int bookIdCounter = 1;
    private final String FILE_NAME = "library_data.dat"; // File to store data

    public Library() {
        loadBooksFromFile(); // Load books when program starts
    }

    // Add a book
    public void addBook(String title, String author) {
        Book book = new Book(bookIdCounter, title, author);
        books.put(bookIdCounter, book);
        bookIdCounter++;
        saveBooksToFile(); // Save after adding
        System.out.println("Book added successfully: " + book);
    }

    // Remove a book
    public void removeBook(int id) {
        if (books.containsKey(id)) {
            books.remove(id);
            saveBooksToFile(); // Save after removal
            System.out.println("Book removed successfully.");
        } else {
            System.out.println("Book not found.");
        }
    }

    // View all books
    public void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
        } else {
            books.values().forEach(System.out::println);
        }
    }

    // Search book by title
    public void searchBook(String title) {
        boolean found = false;
        for (Book book : books.values()) {
            if (book.title.equalsIgnoreCase(title)) {
                System.out.println("Book Found: " + book);
                found = true;
                break;
            }
        }
        if (!found) System.out.println("Book not found.");
    }

    // Save books to file
    private void saveBooksToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(books);
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    // Load books from file
    private void loadBooksFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return; // If file doesn’t exist, no need to load

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Map<Integer, Book> loadedBooks = (Map<Integer, Book>) ois.readObject();
            books.putAll(loadedBooks);
            bookIdCounter = books.isEmpty() ? 1 : Collections.max(books.keySet()) + 1; // Resume ID sequence
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Add Book  2. Remove Book  3. View Books  4. Search Book  5. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author name: ");
                    String author = scanner.nextLine();
                    library.addBook(title, author);
                    break;
                case 2:
                    System.out.print("Enter book ID to remove: ");
                    int id = scanner.nextInt();
                    library.removeBook(id);
                    break;
                case 3:
                    library.viewBooks();
                    break;
                case 4:
                    System.out.print("Enter book title to search: ");
                    String searchTitle = scanner.nextLine();
                    library.searchBook(searchTitle);
                    break;
                case 5:
                    System.out.println("Exiting Library Management System...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
