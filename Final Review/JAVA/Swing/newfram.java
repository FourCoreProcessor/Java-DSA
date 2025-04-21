import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class newfram extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JTextField userField, passField;
    public JButton loginBtn;
    public JComboBox<String> roleCombo;
    public JLabel userLabel, passLabel;

    public Librarian librarian;
    public LibrarianManager librarianManager;
    public User user;

    public JPanel panel;

    public newfram() {
        initUI();
    }

    public void initUI() {
        String bookPath = "D:/College/Sem 2/Project/DSA and JAVA/java code/final/books.csv";
        String borrowerPath = "D:/College/Sem 2/Project/DSA and JAVA/java code/final/borrower.csv";
        String passwordPath = "D:/College/Sem 2/Project/DSA and JAVA/java code/final/Password_Manager.csv";

        librarian = new Librarian(bookPath, borrowerPath);
        librarianManager = new LibrarianManager(passwordPath);
        user = new User(librarian);

        setTitle("Library Management System");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showLoginScreen();
        setVisible(true);
    }

    public void showLoginScreen() {
        panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        roleCombo = new JComboBox<>(new String[]{"User", "Librarian"});
        userLabel = new JLabel("Username:");
        userField = new JTextField();
        passLabel = new JLabel("Password:");
        passField = new JPasswordField();
        loginBtn = new JButton("Login");

        panel.add(new JLabel("Select Role:"));
        panel.add(roleCombo);
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(loginBtn);

        setContentPane(panel);
        revalidate();

        toggleFields();

        roleCombo.addActionListener(e -> toggleFields());
        loginBtn.addActionListener(e -> handleLogin());
    }

    public void toggleFields() {
        String role = (String) roleCombo.getSelectedItem();
        boolean isLibrarian = role.equals("Librarian");

        userLabel.setVisible(isLibrarian);
        userField.setVisible(isLibrarian);
        passLabel.setVisible(isLibrarian);
        passField.setVisible(isLibrarian);

        revalidate();
        repaint();
    }

    public void handleLogin() {
        String role = (String) roleCombo.getSelectedItem();

        if (role.equals("User")) {
            showUserMenu();
        } else {
            String userId = userField.getText();
            String password = passField.getText();
            if (LibraryManagementSystem.authenticate(
                    librarianManager.credentialsFile, userId, password)) {
                showLibrarianMenu();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        }
    }

    public void showUserMenu() {
        JFrame frame = new JFrame("User Panel");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

        JButton displayBtn = new JButton("Display Books");
        JButton searchTitleBtn = new JButton("Search by Title/Author");
        JButton searchIsbnBtn = new JButton("Search by ISBN");
        JButton exitBtn = new JButton("Exit");

        panel.add(displayBtn);
        panel.add(searchTitleBtn);
        panel.add(searchIsbnBtn);
        panel.add(exitBtn);

        frame.setContentPane(panel);
        frame.setVisible(true);

        displayBtn.addActionListener(e -> user.displayBooks());

        searchTitleBtn.addActionListener(e -> {
            String keyword = JOptionPane.showInputDialog(frame, "Enter Title/Author:");
            if (keyword != null) user.searchBook(keyword);
        });

        searchIsbnBtn.addActionListener(e -> {
            String isbn = JOptionPane.showInputDialog(frame, "Enter ISBN:");
            if (isbn != null) user.searchByIsbn(isbn);
        });

        exitBtn.addActionListener(e -> frame.dispose());
    }

    public void showLibrarianMenu() {
        JFrame frame = new JFrame("Librarian Panel");
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel(new GridLayout(8, 1, 10, 10));

        JButton addBookBtn = new JButton("Add Book");
        JButton delBookBtn = new JButton("Delete Book");
        JButton borrowBookBtn = new JButton("Borrow Book");
        JButton returnBookBtn = new JButton("Return Book");
        JButton displayBooksBtn = new JButton("Display Books");
        JButton addLibrarianBtn = new JButton("Add Librarian");
        JButton delLibrarianBtn = new JButton("Delete Librarian");
        JButton exitBtn = new JButton("Exit");

        panel.add(addBookBtn);
        panel.add(delBookBtn);
        panel.add(borrowBookBtn);
        panel.add(returnBookBtn);
        panel.add(displayBooksBtn);
        panel.add(addLibrarianBtn);
        panel.add(delLibrarianBtn);
        panel.add(exitBtn);

        frame.setContentPane(panel);
        frame.setVisible(true);

        addBookBtn.addActionListener(e -> {
            String title = JOptionPane.showInputDialog("Title:");
            String author = JOptionPane.showInputDialog("Author:");
            String isbn = JOptionPane.showInputDialog("ISBN:");
            librarian.addBook(title, author, isbn);
        });

        delBookBtn.addActionListener(e -> {
            String isbn = JOptionPane.showInputDialog("ISBN to delete:");
            librarian.deleteBook(isbn);
        });

        borrowBookBtn.addActionListener(e -> {
            String isbn = JOptionPane.showInputDialog("ISBN:");
            String name = JOptionPane.showInputDialog("Borrower Name:");
            String contact = JOptionPane.showInputDialog("Contact Number:");
            librarian.markAsBorrowed(isbn, name, contact);
        });

        returnBookBtn.addActionListener(e -> {
            String isbn = JOptionPane.showInputDialog("ISBN:");
            librarian.markAsReturned(isbn);
        });

        displayBooksBtn.addActionListener(e -> user.displayBooks());

        addLibrarianBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("New ID:");
            String pwd = JOptionPane.showInputDialog("Password:");
            librarianManager.addLibrarian(id, pwd);
        });

        delLibrarianBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("ID to remove:");
            librarianManager.deleteLibrarian(id);
        });

        exitBtn.addActionListener(e -> frame.dispose());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(newswing::new);
    }
}
