import java.io.*;
import java.util.*;

class Book {
    String title, author, isbn;
    boolean available;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = true; 
    }

    @Override
    public String toString() {
        return title + " by " + author + " (ISBN: " + isbn + ") - " + (available ? "Available" : "Borrowed");
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
    String csvFile = "D:/College/Sem 2/Project/DSA and JAVA/dsa code/books.csv", borrowerFile = "D:/College/Sem 2/Project/DSA and JAVA/data/borrower.csv";

    public Library() {
        loadBooksFromCSV();
    }

    public void addBook(String title, String author, String isbn) {
        if (searchByISBN(isbn) != null) {
            System.out.println("Book with ISBN " + isbn + " already exists.");
            return;
        }
        Book newBook = new Book(title, author, isbn);
        Node newNode = new Node(newBook);
        if (head == null) head = newNode;
        else {
            Node temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = newNode;
        }
        saveBooksToCSV();
    }

    public void deleteBook(String isbn) {
        if (head == null) {
            System.out.println("Library is empty.");
            return;
        }
        if (head.book.isbn.equals(isbn)) {
            head = head.next;
            saveBooksToCSV();
            System.out.println("Book deleted.");
            return;
        }
        Node temp = head;
        while (temp.next != null && !temp.next.book.isbn.equals(isbn)) temp = temp.next;
        if (temp.next != null) {
            temp.next = temp.next.next;
            saveBooksToCSV();
            System.out.println("Book deleted.");
        } else {
            System.out.println("Book not found.");
        }
    }

    public void markAsBorrowed(String isbn, String borrowerName, String contactNumber) {
        Book book = searchByISBN(isbn);
        if (book != null && book.available) {
            book.available = false;
            saveBooksToCSV();
            saveBorrowerDetails(borrowerName, contactNumber, isbn);
            System.out.println("Book borrowed.");
        } else {
            System.out.println("Book not available.");
        }
    }

    public void markAsReturned(String isbn) {
        Book book = searchByISBN(isbn);
        if (book != null && !book.available) {
            book.available = true;
            saveBooksToCSV();
            removeBorrowerDetails(isbn);
            System.out.println("Book returned.");
        } else {
            System.out.println("Book was not borrowed.");
        }
    }

    public void loadBooksFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    Book book = new Book(data[0], data[1], data[2]);
                    book.available = Boolean.parseBoolean(data[3]);
                    addBook(data[0], data[1], data[2]);
                }
            }
        } catch (IOException e) {
            System.out.println("Books file not found.");
        }
    }

    public void saveBooksToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            bw.write("title,author,isbn,available\n");
            Node temp = head;
            while (temp != null) {
                bw.write(temp.book.title + "," + temp.book.author + "," + temp.book.isbn + "," + temp.book.available + "\n");
                temp = temp.next;
            }
        } catch (IOException e) {
            System.out.println("Error saving books.");
        }
    }

    public void saveBorrowerDetails(String name, String contact, String isbn) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(borrowerFile, true))) {
            bw.write(name + "," + contact + "," + isbn + "\n");
        } catch (IOException e) {
            System.out.println("Error saving borrower details.");
        }
    }

    public void removeBorrowerDetails(String isbn) {
        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(borrowerFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.contains(isbn)) lines.add(line);
                }
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(borrowerFile))) {
                for (String line : lines) bw.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating borrower details.");
        }
    }

    public Book searchByISBN(String isbn) {
        Node temp = head;
        while (temp != null) {
            if (temp.book.isbn.equals(isbn)) return temp.book;
            temp = temp.next;
        }
        return null;
    }

    public void displayBooks() {
        if (head == null) {
            System.out.println("No books available.");
            return;
        }
        Node temp = head;
        while (temp != null) {
            System.out.println(temp.book);
            temp = temp.next;
        }
    }
}

class User {
    Library library;

    public User(Library library) {
        this.library = library;
    }

    public void searchBook(String keyword) {
        Node temp = library.head;
        boolean found = false;
        while (temp != null) {
            if (temp.book.title.toLowerCase().contains(keyword.toLowerCase()) || temp.book.author.toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(temp.book);
                found = true;
            }
            temp = temp.next;
        }
        if (!found) System.out.println("No books found.");
    }

    public void displayBooks() {
        library.displayBooks();
    }
}

public class LibraryManager {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library library = new Library();
        User user = new User(library);

        System.out.println("LIBRARY MANAGEMENT SYSTEM");
        System.out.print("Are you Librarian or User? (Enter 'l' for Librarian, 'u' for User): ");
        char role = sc.next().charAt(0);
        sc.nextLine();
        if (role == 'l') {
            while (true) {
                System.out.println("\n1. Display Books\n2. Search Book\n3. Add Book\n4. Delete Book\n5. Borrow Book\n6. Return Book\n7. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                sc.nextLine();
                if (choice == 1) user.displayBooks();
                else if (choice == 2) {
                    System.out.print("Enter keyword: ");
                    user.searchBook(sc.nextLine());
                } else if (choice == 3) {
                    System.out.print("Title: "); String title = sc.nextLine();
                    System.out.print("Author: "); String author = sc.nextLine();
                    System.out.print("ISBN: "); String isbn = sc.nextLine();
                    library.addBook(title, author, isbn);
                } else if (choice == 4) {
                    System.out.print("Enter ISBN to delete: ");
                    library.deleteBook(sc.nextLine());
                } else if (choice == 5) {
                    System.out.print("ISBN: "); String isbn = sc.nextLine();
                    System.out.print("Borrower Name: "); String name = sc.nextLine();
                    System.out.print("Contact: "); String contact = sc.nextLine();
                    library.markAsBorrowed(isbn, name, contact);
                } else if (choice == 6) {
                    System.out.print("Enter ISBN to return: ");
                    library.markAsReturned(sc.nextLine());
                } else if (choice == 7) break;
            }
        }
        else if(role=='u'){
            System.out.println("Welcome to the library! \n");

        while (true) {
            System.out.println("What would you like to do? \n");
            System.out.println("1. Display Books");
            System.out.println("2. Search Book by Author/Title");
            System.out.println("3. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); 

            System.out.println();

            if (choice == 1) {
                user.displayBooks(); 
            } else if (choice == 2) {
                System.out.print("Enter a keyword (author name or book title): ");
                String keyword = sc.nextLine();
                user.searchBook(keyword); 
            } else if (choice == 3) {
                System.out.println("Exiting. Bye!");
                break;
            } else {
                System.out.println("Invalid input. Enter correct choice.");
            }
        }
        sc.close();
    }
}
}
