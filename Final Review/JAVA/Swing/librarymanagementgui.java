import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class librarymanagementgui extends JFrame {
    private static final long serialVersionUID = 1L;
    private Librarian librarian;
    private LibrarianManager librarianManager;
    private User user;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String passwordPath = "D:/College/Sem 2/Project/DSA and JAVA/eclipse-workspace/swing/src/swing/Password_Manager.csv";
    private String bookPath = "D:/College/Sem 2/Project/DSA and JAVA/eclipse-workspace/swing/src/swing/books.csv";
    private String borrowerPath = "D:/College/Sem 2/Project/DSA and JAVA/eclipse-workspace/swing/src/swing/borrower.csv";

  
    private static final Color PRIMARY = new Color(58, 100, 190);
    private static final Color PRIMARY_DARK = new Color(38, 70, 130);
    private static final Color ACCENT = new Color(120, 180, 255);
    private static final Color BACKGROUND = new Color(245, 248, 255);
    private static final Color PANEL_BG = new Color(255, 255, 255);
    private static final Color BUTTON_BG = new Color(58, 100, 190);
    private static final Color BUTTON_FG = Color.WHITE;
    private static final Color TABLE_HEADER_BG = new Color(230, 240, 255);
    private static final Color TABLE_HEADER_FG = PRIMARY_DARK;

    static {
   
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            
            UIManager.put("control", PANEL_BG);
            UIManager.put("info", PANEL_BG);
            UIManager.put("nimbusBase", PRIMARY);
            UIManager.put("nimbusBlueGrey", PRIMARY_DARK);
            UIManager.put("nimbusFocus", ACCENT);
            UIManager.put("nimbusLightBackground", BACKGROUND);
            UIManager.put("nimbusSelectionBackground", ACCENT);
            UIManager.put("text", PRIMARY_DARK);
            UIManager.put("Table.background", PANEL_BG);
            UIManager.put("Table.alternateRowColor", new Color(240,245,255));
            UIManager.put("Table.selectionBackground", ACCENT);
            UIManager.put("Table.selectionForeground", PRIMARY_DARK);
            UIManager.put("TableHeader.background", TABLE_HEADER_BG);
            UIManager.put("TableHeader.foreground", TABLE_HEADER_FG);
            UIManager.put("Button.background", BUTTON_BG);
            UIManager.put("Button.foreground", BUTTON_FG);
            UIManager.put("Panel.background", BACKGROUND);
        } catch (Exception e) {
           
        }
    }

    public librarymanagementgui() {
        setTitle("Library Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLocationRelativeTo(null);

        librarian = new Librarian(bookPath, borrowerPath);
        librarianManager = new LibrarianManager(passwordPath);
        user = new User(librarian);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(loginPanel(), "login");
        mainPanel.add(userPanel(), "user");
        mainPanel.add(librarianPanel(), "librarian");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
    }

    private JPanel loginPanel() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        JPanel bgPanel = new JPanel(new GridBagLayout());
        bgPanel.setBackground(BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new GridBagLayout());
        boxPanel.setBackground(PANEL_BG);
        boxPanel.setBorder(new CompoundBorder(
            new TitledBorder(new LineBorder(PRIMARY, 2, true), "Login", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 22), PRIMARY),
            new EmptyBorder(30, 40, 30, 40)
        ));
        boxPanel.setPreferredSize(new Dimension(400, 350));

        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.insets = new Insets(0, 0, 25, 0);
        boxPanel.add(titleLabel, gbc);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"User", "Librarian"});
        roleBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridy = 1; gbc.gridwidth = 1; gbc.insets = new Insets(0, 0, 15, 10);
        boxPanel.add(roleLabel, gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 15, 0);
        boxPanel.add(roleBox, gbc);

        JLabel userLabel = new JLabel("User ID:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JTextField userField = new JTextField(15);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 2; gbc.insets = new Insets(0, 0, 15, 10);
        boxPanel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 15, 0);
        boxPanel.add(userField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JPasswordField passField = new JPasswordField(15);
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 3; gbc.insets = new Insets(0, 0, 15, 10);
        boxPanel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 15, 0);
        boxPanel.add(passField, gbc);

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginBtn.setBackground(BUTTON_BG);
        loginBtn.setForeground(BUTTON_FG);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorder(new RoundedBorder(10));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.insets = new Insets(10, 0, 0, 0);
        boxPanel.add(loginBtn, gbc);

        roleBox.addActionListener(e -> {
            boolean isLibrarian = roleBox.getSelectedIndex() == 1;
            userLabel.setVisible(isLibrarian);
            userField.setVisible(isLibrarian);
            passLabel.setVisible(isLibrarian);
            passField.setVisible(isLibrarian);
        });
        roleBox.setSelectedIndex(0);

        loginBtn.addActionListener(e -> {
            if (roleBox.getSelectedIndex() == 1) {
                String uid = userField.getText().trim();
                String pwd = new String(passField.getPassword());
                if (authenticate(passwordPath, uid, pwd)) {
                    cardLayout.show(mainPanel, "librarian");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials.");
                }
            } else {
                cardLayout.show(mainPanel, "user");
            }
        });

        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        bgPanel.add(boxPanel, gbc);

        return bgPanel;
    }

  
    private boolean authenticate(String filePath, String uid, String pwd) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split(",", 2);
                if (parts.length < 2) continue;
                
                String storedUid = parts[0].trim();
                String storedPwd = parts[1].trim();
                
                if (storedUid.equals(uid) && storedPwd.equals(pwd)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    static class RoundedBorder extends AbstractBorder {
        private int radius;
        RoundedBorder(int radius) {
            this.radius = radius;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(PRIMARY);
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+1, this.radius+1);
        }
        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = this.radius+1;
            return insets;
        }
    }

    private JPanel userPanel() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(PANEL_BG);

        JPanel top = new JPanel();
        top.setBackground(PANEL_BG);
        top.setBorder(new EmptyBorder(16, 16, 16, 16));
        JButton viewBtn = styledButton("View Books");
        JButton searchBtn = styledButton("Search Title/Author");
        JButton searchIsbnBtn = styledButton("Search ISBN");
        JButton logoutBtn = styledButton("Logout");
        top.add(viewBtn); top.add(searchBtn); top.add(searchIsbnBtn); top.add(logoutBtn);

        JTextArea output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        output.setBackground(PANEL_BG);
        JScrollPane scroll = new JScrollPane(output);
        centerPanel.add(scroll, BorderLayout.CENTER);

        viewBtn.addActionListener(e -> {
            centerPanel.removeAll();
            String[] columns = {"Title", "Author", "ISBN", "Available"};
            ArrayList<Object[]> dataList = new ArrayList<>();
            Node curr = librarian.head;
            while (curr != null) {
                Book b = curr.book;
                String available = b.available ? "Yes" : "No";
                dataList.add(new Object[]{b.title, b.author, b.isbn, available});
                curr = curr.next;
            }
            Object[][] data = dataList.toArray(new Object[0][]);
            JTable table = styledTable(data, columns);
            JScrollPane tableScroll = new JScrollPane(table);
            centerPanel.add(tableScroll, BorderLayout.CENTER);
            centerPanel.revalidate();
            centerPanel.repaint();
        });

        searchBtn.addActionListener(e -> {
            String kw = JOptionPane.showInputDialog(this, "Enter keyword:");
            if (kw != null) {
                centerPanel.removeAll();
                String[] columns = {"Title", "Author", "ISBN", "Available"};
                ArrayList<Object[]> dataList = new ArrayList<>();
                Node curr = librarian.head;
                while (curr != null) {
                    if (curr.book.title.toLowerCase().contains(kw.toLowerCase()) ||
                        curr.book.author.toLowerCase().contains(kw.toLowerCase())) {
                        Book b = curr.book;
                        String available = b.available ? "Yes" : "No";
                        dataList.add(new Object[]{b.title, b.author, b.isbn, available});
                    }
                    curr = curr.next;
                }
                if (dataList.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No books found with keyword: " + kw, "Not Found", JOptionPane.ERROR_MESSAGE);
                } else {
                    Object[][] data = dataList.toArray(new Object[0][]);
                    JTable table = styledTable(data, columns);
                    JScrollPane tableScroll = new JScrollPane(table);
                    centerPanel.add(tableScroll, BorderLayout.CENTER);
                }
                centerPanel.revalidate();
                centerPanel.repaint();
            }
        });

        searchIsbnBtn.addActionListener(e -> {
            String isbn = JOptionPane.showInputDialog(this, "Enter ISBN:");
            if (isbn != null) {
                centerPanel.removeAll();
                Book book = librarian.searchByIsbn(isbn);
                if (book != null) {
                    String[] columns = {"Title", "Author", "ISBN", "Available"};
                    Object[][] data = {
                        {book.title, book.author, book.isbn, book.available ? "Yes" : "No"}
                    };
                    JTable table = styledTable(data, columns);
                    JScrollPane tableScroll = new JScrollPane(table);
                    centerPanel.add(tableScroll, BorderLayout.CENTER);
                } else {
                    JOptionPane.showMessageDialog(this, "Book not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
                }
                centerPanel.revalidate();
                centerPanel.repaint();
            }
        });

        logoutBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        panel.add(top, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel librarianPanel() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(PANEL_BG);

        JPanel top = new JPanel();
        top.setBackground(PANEL_BG);
        top.setBorder(new EmptyBorder(16, 16, 16, 16));
        JButton viewBtn = styledButton("View Books");
        JButton searchBtn = styledButton("Search Title/Author");
        JButton searchIsbnBtn = styledButton("Search ISBN");
        JButton borrowBtn = styledButton("Borrow Book");
        JButton returnBtn = styledButton("Return Book");
        JButton addBtn = styledButton("Add Book");
        JButton delBtn = styledButton("Delete Book");
        JButton addLibrarianBtn = styledButton("Add Librarian");
        JButton delLibrarianBtn = styledButton("Delete Librarian");
        JButton logoutBtn = styledButton("Logout");
        top.add(viewBtn); top.add(searchBtn); top.add(searchIsbnBtn);
        top.add(borrowBtn); top.add(returnBtn); top.add(addBtn); top.add(delBtn);
        top.add(addLibrarianBtn); top.add(delLibrarianBtn); top.add(logoutBtn);

        JTextArea output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        output.setBackground(PANEL_BG);
        JScrollPane scroll = new JScrollPane(output);
        centerPanel.add(scroll, BorderLayout.CENTER);

        viewBtn.addActionListener(e -> {
            centerPanel.removeAll();
            String[] columns = {"Title", "Author", "ISBN", "Available"};
            ArrayList<Object[]> dataList = new ArrayList<>();
            Node curr = librarian.head;
            while (curr != null) {
                Book b = curr.book;
                String available = b.available ? "Yes" : "No";
                dataList.add(new Object[]{b.title, b.author, b.isbn, available});
                curr = curr.next;
            }
            Object[][] data = dataList.toArray(new Object[0][]);

            JTable table = styledTable(data, columns);
            JScrollPane tableScroll = new JScrollPane(table);
            centerPanel.add(tableScroll, BorderLayout.CENTER);
            centerPanel.revalidate();
            centerPanel.repaint();
        });

        searchBtn.addActionListener(e -> {
            String kw = JOptionPane.showInputDialog(this, "Enter keyword:");
            if (kw != null) {
                centerPanel.removeAll();
                String[] columns = {"Title", "Author", "ISBN", "Available"};
                ArrayList<Object[]> dataList = new ArrayList<>();
                Node curr = librarian.head;
                while (curr != null) {
                    if (curr.book.title.toLowerCase().contains(kw.toLowerCase()) ||
                        curr.book.author.toLowerCase().contains(kw.toLowerCase())) {
                        Book b = curr.book;
                        String available = b.available ? "Yes" : "No";
                        dataList.add(new Object[]{b.title, b.author, b.isbn, available});
                    }
                    curr = curr.next;
                }
                if (dataList.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No books found with keyword: " + kw, "Not Found", JOptionPane.ERROR_MESSAGE);
                } else {
                    Object[][] data = dataList.toArray(new Object[0][]);
                    JTable table = styledTable(data, columns);
                    JScrollPane tableScroll = new JScrollPane(table);
                    centerPanel.add(tableScroll, BorderLayout.CENTER);
                }
                centerPanel.revalidate();
                centerPanel.repaint();
            }
        });

        searchIsbnBtn.addActionListener(e -> {
            String isbn = JOptionPane.showInputDialog(this, "Enter ISBN:");
            if (isbn != null) {
                centerPanel.removeAll();
                Book book = librarian.searchByIsbn(isbn);
                if (book != null) {
                    String[] columns = {"Title", "Author", "ISBN", "Available"};
                    Object[][] data = {
                        {book.title, book.author, book.isbn, book.available ? "Yes" : "No"}
                    };
                    JTable table = styledTable(data, columns);
                    JScrollPane tableScroll = new JScrollPane(table);
                    centerPanel.add(tableScroll, BorderLayout.CENTER);
                } else {
                    JOptionPane.showMessageDialog(this, "Book not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
                }
                centerPanel.revalidate();
                centerPanel.repaint();
            }
        });

        borrowBtn.addActionListener(e -> {
            String isbn = JOptionPane.showInputDialog(this, "Enter ISBN:");
            if (isbn != null) {
                Book book = librarian.searchByIsbn(isbn);
                if (book != null && book.available) {
                    String name = JOptionPane.showInputDialog(this, "Borrower Name:");
                    String contact = JOptionPane.showInputDialog(this, "Contact Number:");
                    librarian.markAsBorrowed(isbn, name, contact);
                    centerPanel.removeAll();
                    JTextArea actionOutput = new JTextArea("Book borrowed.");
                    actionOutput.setEditable(false);
                    centerPanel.add(new JScrollPane(actionOutput), BorderLayout.CENTER);
                } else {
                    JOptionPane.showMessageDialog(this, "Book not available or not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
                }
                centerPanel.revalidate();
                centerPanel.repaint();
            }
        });

        returnBtn.addActionListener(e -> {
            String isbn = JOptionPane.showInputDialog(this, "Enter ISBN:");
            if (isbn != null) {
                Book book = librarian.searchByIsbn(isbn);
                if (book != null && !book.available) {
                    librarian.markAsReturned(isbn);
                    centerPanel.removeAll();
                    JTextArea actionOutput = new JTextArea("Book returned.");
                    actionOutput.setEditable(false);
                    centerPanel.add(new JScrollPane(actionOutput), BorderLayout.CENTER);
                } else {
                    JOptionPane.showMessageDialog(this, "Book not found or not borrowed.", "Not Found", JOptionPane.ERROR_MESSAGE);
                }
                centerPanel.revalidate();
                centerPanel.repaint();
            }
        });

        addBtn.addActionListener(e -> {
            String title = JOptionPane.showInputDialog(this, "Title:");
            String author = JOptionPane.showInputDialog(this, "Author:");
            String isbn = JOptionPane.showInputDialog(this, "ISBN:");
            if (title != null && author != null && isbn != null) {
                librarian.addBook(title, author, isbn);
                centerPanel.removeAll();
                JTextArea actionOutput = new JTextArea("Book added.");
                actionOutput.setEditable(false);
                centerPanel.add(new JScrollPane(actionOutput), BorderLayout.CENTER);
                centerPanel.revalidate();
                centerPanel.repaint();
            }
        });

        delBtn.addActionListener(e -> {
            String isbn = JOptionPane.showInputDialog(this, "ISBN to delete:");
            if (isbn != null) {
                boolean deleted = librarian.deleteBook(isbn);
                centerPanel.removeAll();
                if (deleted) {
                    JTextArea actionOutput = new JTextArea("Book deleted.");
                    actionOutput.setEditable(false);
                    centerPanel.add(new JScrollPane(actionOutput), BorderLayout.CENTER);
                } else {
                    JOptionPane.showMessageDialog(this, "Book not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
                }
                centerPanel.revalidate();
                centerPanel.repaint();
            }
        });

        addLibrarianBtn.addActionListener(e -> {
            String uid = JOptionPane.showInputDialog(this, "Enter new Librarian ID:");
            if(uid != null && !uid.trim().isEmpty()) {
                String pwd = JOptionPane.showInputDialog(this, "Enter password:");
                if(pwd != null && !pwd.trim().isEmpty()) {
                    try (FileWriter fw = new FileWriter(passwordPath, true)) {
                        if(librarianExists(uid)) {
                            JOptionPane.showMessageDialog(this, "Librarian ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        fw.append(uid + "," + pwd + "\n");
                        JOptionPane.showMessageDialog(this, "Librarian added successfully!");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error saving librarian: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        delLibrarianBtn.addActionListener(e -> {
            String uid = JOptionPane.showInputDialog(this, "Enter Librarian ID to delete:");
            if(uid != null && !uid.trim().isEmpty()) {
                try {
                    File inputFile = new File(passwordPath);
                    File tempFile = new File("temp.csv");

                    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                    String currentLine;
                    boolean found = false;

                    while((currentLine = reader.readLine()) != null) {
                        String[] parts = currentLine.split(",");
                        if(parts[0].equals(uid)) {
                            found = true;
                            continue;
                        }
                        writer.write(currentLine + "\n");
                    }

                    writer.close();
                    reader.close();

                    if(found) {
                        inputFile.delete();
                        tempFile.renameTo(inputFile);
                        JOptionPane.showMessageDialog(this, "Librarian deleted successfully!");
                    } else {
                        tempFile.delete();
                        JOptionPane.showMessageDialog(this, "Librarian not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting librarian: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        logoutBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        panel.add(top, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    private boolean librarianExists(String uid) {
        try (BufferedReader br = new BufferedReader(new FileReader(passwordPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts.length > 0 && parts[0].equals(uid)) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error checking librarians: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(BUTTON_BG);
        btn.setForeground(BUTTON_FG);
        btn.setFocusPainted(false);
        btn.setBorder(new RoundedBorder(10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JTable styledTable(Object[][] data, String[] columns) {
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(TABLE_HEADER_BG);
        table.getTableHeader().setForeground(TABLE_HEADER_FG);
        table.setGridColor(new Color(220, 230, 245));
        table.setSelectionBackground(ACCENT);
        table.setSelectionForeground(PRIMARY_DARK);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        return table;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            librarymanagementgui frame = new librarymanagementgui();
            frame.setVisible(true);
        });
    }
}
