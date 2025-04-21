
import java.io.*;
import java.util.*;
class Book {
    String title, author, isbn;
    boolean available;

    public Book(String title, String author, String isbn, boolean available) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = available;
    }

    @Override
    public String toString() {
        String status = available ? "Available" : "Borrowed";
        return title + " by " + author + " (ISBN: " + isbn + ") - " + status;
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

class Librarian {
    Node head;
    String bookFile, borrowerFile;

    public Librarian(String bookFile, String borrowerFile) {
        this.bookFile = bookFile;
        this.borrowerFile = borrowerFile;
        loadBooksFromCSV();
    }

    public void addBook(String title, String author, String isbn) {
        if (searchByIsbn(isbn) != null) return;
        Book newBook = new Book(title, author, isbn, true);
        Node newNode = new Node(newBook);
        newNode.next = head;
        head = newNode;
        saveBooksToCSV();
    }

    public boolean deleteBook(String isbn) {
        if (head == null) return false;
        if (head.book.isbn.equals(isbn)) {
            head = head.next;
            saveBooksToCSV();
            return true;
        }
        Node curr = head;
        while (curr.next != null && !curr.next.book.isbn.equals(isbn)) {
            curr = curr.next;
        }
        if (curr.next != null) {
            curr.next = curr.next.next;
            saveBooksToCSV();
        }
		return false;
    }

    public void markAsBorrowed(String isbn, String borrowerName, String contact) {
        Book book = searchByIsbn(isbn);
        if (book != null && book.available) {
            book.available = false;
            saveBooksToCSV();
            saveBorrowerDetails(borrowerName, contact, isbn);
        }
    }

    public void markAsReturned(String isbn) {
        Book book = searchByIsbn(isbn);
        if (book != null && !book.available) {
            book.available = true;
            saveBooksToCSV();
            removeBorrowerDetails(isbn);
        }
    }

    public Book searchByIsbn(String isbn) {
        Node curr = head;
        while (curr != null) {
            if (curr.book.isbn.equals(isbn)) return curr.book;
            curr = curr.next;
        }
        return null;
    }

    public void loadBooksFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(bookFile))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String title = parts[0];
                    String author = parts[1];
                    String isbn = parts[2];
                    boolean available = parts[3].trim().equalsIgnoreCase("true");
                    Book book = new Book(title, author, isbn, available);
                    Node newNode = new Node(book);
                    newNode.next = head;
                    head = newNode;
                }
            }
        } catch (IOException e) {}
    }

    public void saveBooksToCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(bookFile))) {
            pw.println("title,author,isbn,available");
            Node curr = head;
            while (curr != null) {
                Book b = curr.book;
                pw.println(b.title + "," + b.author + "," + b.isbn + "," + b.available);
                curr = curr.next;
            }
        } catch (IOException e) {}
    }

    public void saveBorrowerDetails(String name, String contact, String isbn) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(borrowerFile, true))) {
            pw.println(name + "," + contact + "," + isbn);
        } catch (IOException e) {}
    }

    public void removeBorrowerDetails(String isbn) {
        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(borrowerFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.endsWith("," + isbn)) {
                        lines.add(line);
                    }
                }
            }
            try (PrintWriter pw = new PrintWriter(new FileWriter(borrowerFile))) {
                for (String l : lines) {
                    pw.println(l);
                }
            }
        } catch (IOException e) {}
    }
}
class LibrarianManager {
    String credentialsFile;

    public LibrarianManager(String credentialsFile) {
        this.credentialsFile = credentialsFile;
    }

    public boolean addLibrarian(String userId, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(credentialsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(userId)) {
                    System.out.println("Librarian already exists.");
                    return false;
                }
            }
        } catch (IOException e) {}

        try (PrintWriter pw = new PrintWriter(new FileWriter(credentialsFile, true))) {
            pw.println(userId + "," + password);
            System.out.println("Librarian added.");
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
        return false;
    }

    public boolean deleteLibrarian(String userId) {
        try {
            List<String> lines = new ArrayList<>();
            boolean found = false;

            try (BufferedReader br = new BufferedReader(new FileReader(credentialsFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.startsWith(userId + ",")) {
                        lines.add(line);
                    } else {
                        found = true;
                    }
                }
            }

            try (PrintWriter pw = new PrintWriter(new FileWriter(credentialsFile))) {
                for (String l : lines) {
                    pw.println(l);
                }
            }

            if (found) {
                System.out.println("Librarian deleted.");
                return true;
            } else {
                System.out.println("Librarian not found.");
            }

        } catch (IOException e) {
            System.out.println("Error accessing file.");
        }
        return false;
    }
}

class User {
    Librarian librarian;

    public User(Librarian librarian) {
        this.librarian = librarian;
    }

    public void displayBooks() {
        Node curr = librarian.head;
        while (curr != null) {
            System.out.println(curr.book);
            curr = curr.next;
        }
    }

    public void searchBook(String keyword) {
        Node curr = librarian.head;
        boolean found = false;
        while (curr != null) {
            if (curr.book.title.toLowerCase().contains(keyword.toLowerCase()) ||
                curr.book.author.toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(curr.book);
                found = true;
            }
            curr = curr.next;
        }
        if (!found) {
            System.out.println("No books found with keyword: " + keyword);
        }
    }

    public void searchByIsbn(String isbn) {
        Book book = librarian.searchByIsbn(isbn);
        if (book != null) {
            System.out.println(book);
        } else {
            System.out.println("Book with ISBN " + isbn + " not found.");
        }
    }
}

public class LibraryManagementSystem {
    public static boolean authenticate(String file, String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // skip header if present
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return false;
    }

    public static void userMenu(User user, Scanner sc) {
        while (true) {
            System.out.println("\nUser Menu:");
            System.out.println("1. Display Books");
            System.out.println("2. Search by Title/Author");
            System.out.println("3. Search by ISBN");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> user.displayBooks();
                case 2 -> {
                    System.out.print("Enter keyword: ");
                    String kw = sc.nextLine();
                    user.searchBook(kw);
                }
                case 3 -> {
                    System.out.print("Enter ISBN: ");
                    String isbn = sc.nextLine();
                    user.searchByIsbn(isbn);
                }
                case 4 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    public static void librarianMenu(Librarian librarian, LibrarianManager librarianManager, User user, Scanner sc) {
        while (true) {
            System.out.println("\nLibrarian Menu:");
            System.out.println("1. Display Books");
            System.out.println("2. Search by Title/Author");
            System.out.println("3. Search by ISBN");
            System.out.println("4. Borrow a Book");
            System.out.println("5. Return a Book");
            System.out.println("6. Add a Book");
            System.out.println("7. Delete a Book");
            System.out.println("8. Add Librarian");
            System.out.println("9. Delete Librarian");
            System.out.println("10. Exit");

            System.out.print("Enter choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> user.displayBooks();
                case 2 -> {
                    System.out.print("Enter keyword: ");
                    String kw = sc.nextLine();
                    user.searchBook(kw);
                }
                case 3 -> {
                    System.out.print("Enter ISBN: ");
                    String isbn = sc.nextLine();
                    user.searchByIsbn(isbn);
                }
                case 4 -> {
                    System.out.print("Enter ISBN: ");
                    String isbn = sc.nextLine();
                    System.out.print("Borrower Name: ");
                    String name = sc.nextLine();
                    System.out.print("Contact Number: ");
                    String contact = sc.nextLine();
                    librarian.markAsBorrowed(isbn, name, contact);
                }
                case 5 -> {
                    System.out.print("Enter ISBN: ");
                    String isbn = sc.nextLine();
                    librarian.markAsReturned(isbn);
                }
                case 6 -> {
                    System.out.print("Title: ");
                    String title = sc.nextLine();
                    System.out.print("Author: ");
                    String author = sc.nextLine();
                    System.out.print("ISBN: ");
                    String isbn = sc.nextLine();
                    librarian.addBook(title, author, isbn);
                }
                case 7 -> {
                    System.out.print("ISBN to delete: ");
                    String isbn = sc.nextLine();
                    librarian.deleteBook(isbn);
                }
                case 8 -> {
                    System.out.println("Creating new Librarian login credentials");
                    System.out.print("Enter user ID: ");
                    String newId = sc.nextLine();
                    System.out.print("Enter password: ");
                    String newPwd = sc.nextLine();
                    librarianManager.addLibrarian(newId, newPwd);
                }
                case 9 -> {
                    System.out.print("Enter user ID of librarian to delete: ");
                    String delId = sc.nextLine();
                    librarianManager.deleteLibrarian(delId);
                }
                case 10 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String bookPath = "D:/College/Sem 2/Project/DSA and JAVA/java code/final code/data.csv";
        String borrowerPath = "D:/College/Sem 2/Project/DSA and JAVA/java code/final code/borrower.csv";
        String passwordPath = "D:/College/Sem 2/Project/DSA and JAVA/java code/final code/Password_Manager.csv";

        Librarian librarian = new Librarian(bookPath, borrowerPath);
        LibrarianManager librarianManager = new LibrarianManager(passwordPath);
        User user = new User(librarian);

        System.out.println("LIBRARY MANAGEMENT\n");
        System.out.print("Are you Librarian or User? (Enter 'l' for librarian and 'u' for user): ");
        String role = sc.nextLine().trim();

        if (role.equalsIgnoreCase("l")) {
            System.out.print("Enter User ID: ");
            String uid = sc.nextLine();
            System.out.print("Enter Password: ");
            String pwd = sc.nextLine();

            if (authenticate(passwordPath, uid, pwd)) {
                System.out.println("Login successful!");
                librarianMenu(librarian, librarianManager, user, sc);
            } else {
                System.out.println("Invalid credentials. Exiting...");
            }
        } else if (role.equalsIgnoreCase("u")) {
            userMenu(user, sc);
        } else {
            System.out.println("Invalid role entered. Exiting...");
        }

        sc.close();
    }
}
