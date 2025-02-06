import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Example Usage
        Library library = new Library("C:\\Users\\jkyog\\OneDrive\\Desktop\\college\\S2\\DSA\\books30.csv");
    }
}

class Book {
    String title;
    String author;
    String isbn;
    boolean available;

    public Book(String title, String author, String isbn, boolean available) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = available;
    }

    // Constructor with default available = true
    public Book(String title, String author, String isbn) {
        this(title, author, isbn, true);
    }

    @Override
    public String toString() {
        String status = this.available ? "Available" : "Borrowed";
        return this.title + " by " + this.author + " (ISBN: " + this.isbn + ") - " + status;
    }
}

class Node {
    Book book;
    Node next;

    public Node(Book book) {
        this.book = book;
        this.next = null;
    }
}

class Library {
    Node head;
    String csv_file;  // Store the CSV file path

    public Library(String csv_file) {
        this.head = null;
        this.csv_file = csv_file;
        load_books_from_csv();  // Load existing books from the CSV file
    }

    public void add_book(String title, String author, String isbn) {
        // Check if a book with the same ISBN already exists
        if (search_by_isbn(isbn) != null) {
            System.out.println("Book with ISBN " + isbn + " already exists.");
            return;
        }
        Book new_book = new Book(title, author, isbn);
        Node new_node = new Node(new_book);
        if (this.head == null) {
            this.head = new_node;
        } else {
            Node current = this.head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = new_node;
        }
        save_books_to_csv();  // Save the updated library to the CSV file
    }

    public void delete_book(String isbn) {
        if (this.head == null) {
            System.out.println("The library is empty. No book to delete.");
            return;
        }
        if (this.head.book.isbn.equals(isbn)) {
            System.out.println("Book '" + this.head.book.title + "' removed successfully!");
            this.head = this.head.next;
            save_books_to_csv();  // Save the updated library to the CSV file
            return;
        }
        Node current = this.head;
        while (current.next != null && !current.next.book.isbn.equals(isbn)) {
            current = current.next;
        }
        if (current.next != null) {
            System.out.println("Book '" + current.next.book.title + "' removed successfully!");
            current.next = current.next.next;
            save_books_to_csv();  // Save the updated library to the CSV file
        } else {
            System.out.println("No book found with ISBN " + isbn + ".");
        }
    }

    public void search_book(String keyword) {
        Node current = this.head;
        boolean found = false;
        while (current != null) {
            if (current.book.title.toLowerCase().contains(keyword.toLowerCase()) ||
                current.book.author.toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(current.book);
                found = true;
            }
            current = current.next;
        }
        if (!found) {
            System.out.println("No books found matching '" + keyword + "'.");
        }
    }

    public Book search_by_isbn(String isbn) {
        isbn = isbn.trim();
        Node current = this.head;
        while (current != null) {
            if (current.book.isbn.trim().equals(isbn)) {
                System.out.println(current.book);
                return current.book;
            }
            current = current.next;
        }
        return null;
    }

    public void display_books() {
        if (this.head == null) {
            System.out.println("The library is empty.");
            return;
        }
        Node current = this.head;
        while (current != null) {
            System.out.println(current.book);
            current = current.next;
        }
    }

    public void mark_as_borrowed(String isbn) {
        Node current = this.head;
        while (current != null) {
            if (current.book.isbn.equals(isbn)) {
                if (current.book.available) {
                    current.book.available = false;
                    System.out.println("Book '" + current.book.title + "' marked as borrowed.");
                    save_books_to_csv();  // Save the updated library to the CSV file
                } else {
                    System.out.println("Book '" + current.book.title + "' is already borrowed.");
                }
                return;
            }
            current = current.next;
        }
        System.out.println("No book found with ISBN " + isbn + ".");
    }

    public void mark_as_returned(String isbn) {
        Node current = this.head;
        while (current != null) {
            if (current.book.isbn.equals(isbn)) {
                if (!current.book.available) {
                    current.book.available = true;
                    System.out.println("Book '" + current.book.title + "' marked as returned.");
                    save_books_to_csv();  // Save the updated library to the CSV file
                } else {
                    System.out.println("Book '" + current.book.title + "' was not borrowed.");
                }
                return;
            }
            current = current.next;
        }
        System.out.println("No book found with ISBN " + isbn + ".");
    }

    public void load_books_from_csv() {
        try (BufferedReader reader = new BufferedReader(new FileReader(this.csv_file))) {
            String headerLine = reader.readLine(); // Read header
            if (headerLine == null) {
                System.out.println("Books loaded successfully from the dataset!");
                return;
            }
            // Parse header to determine indices
            String[] headers = headerLine.split(",");
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i].trim().toLowerCase(), i);
            }
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                String title = headerMap.containsKey("title") && headerMap.get("title") < tokens.length ? tokens[headerMap.get("title")].trim() : null;
                String author = headerMap.containsKey("author") && headerMap.get("author") < tokens.length ? tokens[headerMap.get("author")].trim() : null;
                String isbn = headerMap.containsKey("isbn") && headerMap.get("isbn") < tokens.length ? tokens[headerMap.get("isbn")].trim() : null;
                String availableStr = headerMap.containsKey("available") && headerMap.get("available") < tokens.length ? tokens[headerMap.get("available")].trim() : "True";
                boolean available = availableStr.toLowerCase().equals("true");  // Default to True if not provided
                if (title != null && author != null && isbn != null && !title.isEmpty() && !author.isEmpty() && !isbn.isEmpty()) {
                    // Check if the book already exists before adding
                    if (search_by_isbn(isbn) == null) {
                        add_book(title, author, isbn);
                        if (!available) {
                            mark_as_borrowed(isbn);  // Mark as borrowed if specified in the dataset
                        }
                    }
                } else {
                    System.out.println("Skipping invalid row: " + Arrays.toString(tokens));
                }
            }
            System.out.println("Books loaded successfully from the dataset!");
        } catch (FileNotFoundException e) {
            System.out.println("File '" + this.csv_file + "' not found. Starting with an empty library.");
        } catch (Exception e) {
            System.out.println("An error occurred while loading the dataset: " + e);
        }
    }

    public void save_books_to_csv() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(this.csv_file))) {
            writer.println("title,author,isbn,available");  // Write header
            Node current = this.head;
            while (current != null) {
                writer.println(current.book.title + "," +
                               current.book.author + "," +
                               current.book.isbn + "," +
                               String.valueOf(current.book.available).toLowerCase());
                current = current.next;
            }
        } catch (Exception e) {
            System.out.println("An error occurred while saving the dataset: " + e);
        }
    }
}
