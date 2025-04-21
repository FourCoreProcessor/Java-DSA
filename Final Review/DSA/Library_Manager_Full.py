import csv

class Book:
    def __init__(self, title, author, isbn, available=True):
        self.title = title
        self.author = author
        self.isbn = str(isbn).strip()
        self.available = available

    def __str__(self):
        status = "Available" if self.available else "Borrowed"
        return f"{self.title} by {self.author} (ISBN: {self.isbn}) - {status}"


class Node:
    def __init__(self, book):
        self.book = book
        self.next = None

class Librarian:
    def __init__(self, csv_file, borrower_file):
        self.head = None
        self.csv_file = csv_file  
        self.borrower_file = borrower_file
        self.load_books_from_csv()

    # Complexity: O(n)
    def add_book(self, title, author, isbn):
        if self.search_by_isbn(isbn):
            print(f"Book with ISBN {isbn} already exists.")
            return

        new_book = Book(title, author, isbn)
        new_node = Node(new_book)
        if self.head is None:
            self.head = new_node
        else:
            new_node.next = self.head
            self.head = new_node
        self.save_books_to_csv()  

    # Complexity: O(n)
    def delete_book(self, isbn):
        isbn = str(isbn).strip()  

        if not self.head:
            print("The library is empty. No book to delete.")
            return
    
        if self.head.book.isbn.strip() == isbn:
            print(f"Book '{self.head.book.title}' removed successfully!")
            self.head = self.head.next
            self.save_books_to_csv()
            return
            
        current = self.head
        while current.next:
            if current.next.book.isbn.strip() == isbn:
                print(f"Book '{current.next.book.title}' removed successfully!")
                current.next = current.next.next  
                self.save_books_to_csv()
                return
            current = current.next

        print(f"No book found with ISBN {isbn}.")
   
    # Complexity: O(n+m)
    def mark_as_borrowed(self, isbn, borrower_name, contact_number):
        book = self.search_by_isbn(isbn)
        if book:
            if book.available:
                book.available = False
                print(f"Book '{book.title}' marked as borrowed by {borrower_name}.")
                self.save_books_to_csv()
                self.save_borrower_details(borrower_name, contact_number, isbn)
            else:
                print(f"Book '{book.title}' is already borrowed.")
            return
        print(f"No book found with ISBN {isbn}.")

    # Complexity: O(n+m)
    def mark_as_returned(self, isbn):
        book = self.search_by_isbn(isbn)
        if book:
            if not book.available:
                book.available = True
                print(f"Book '{book.title}' marked as returned.")
                self.save_books_to_csv()
                self.remove_borrower_details(isbn)
            else:
                print(f"Book '{book.title}' was not borrowed.")
            return 
        print(f"No book found with ISBN {isbn}.")

    # Complexity: O(n)
    def save_borrower_details(self, name, contact, isbn):
        try:
            with open(self.borrower_file, mode='a', newline='') as file:
                writer = csv.writer(file)
                writer.writerow([name, contact, isbn])
        except Exception as e:
            print(f"An error occurred while saving borrower details: {e}")
    
    # Complexity: O(n)
    def remove_borrower_details(self, isbn):
        try:
            rows = []
            with open(self.borrower_file, mode='r') as file: 
                reader = csv.reader(file)
                rows = [row for row in reader if row[2] != isbn]
            with open(self.borrower_file, mode='w', newline='') as file:
                writer = csv.writer(file)
                writer.writerows(rows)
        except Exception as e:
            print(f"An error occurred while updating borrower details: {e}")
    
    # Complexity: O(n)
    def load_books_from_csv(self):
        try:
            with open(self.csv_file, mode='r') as file:
                reader = csv.DictReader(file)
                for row in reader:
                    title = row.get('title')
                    author = row.get('author')
                    isbn = row.get('isbn')
                    available = row.get('available', 'True').lower() == 'true'  
                    
                    if not self.search_by_isbn(isbn):
                        new_book = Book(title,author,isbn)
                        new_node = Node(new_book)

                        if self.head is None:
                            self.head = new_node
                        else:
                            new_node.next = self.head
                            self.head = new_node
            print("Books loaded successfully from the dataset!")
        except FileNotFoundError:
            print(f"File '{self.csv_file}' not found. Starting with an empty library.")
        except Exception as e:
            print(f"An error occurred while loading the dataset: {e}")

    # Complexity: O(n)
    def save_books_to_csv(self):
        try:
            with open(self.csv_file, mode='w', newline='') as file:
                writer = csv.writer(file)
                writer.writerow(['title', 'author', 'isbn', 'available'])  
                current = self.head
                while current:
                    writer.writerow([
                        current.book.title,
                        current.book.author,
                        current.book.isbn,
                        str(current.book.available).lower()
                    ])
                    current = current.next
        except Exception as e:
            print(f"An error occurred while saving the dataset: {e}")

    # Complexity: O(n)        
    def search_by_isbn(self, isbn):
        isbn = str(isbn).strip()
        current = self.head
        while current:
            if str(current.book.isbn).strip() == isbn:
                return current.book
            current = current.next
        return None

class LibrarianManagement:
    def __init__(self,csv_file):
        self.csv_file = csv_file

    # Complexity: O(n)
    def add_librarian(self,user_id,password):
        try:
            with open(self.csv_file,mode='a',newline='') as file:
                dict = {}
                fields = ["User_ID","Password"]
                dict["User_ID"] = user_id
                dict["Password"] = password 
                w = csv.DictWriter(file,fieldnames=fields)
                w.writerow(dict)
                print("Librarian details added!")
        except Exception as e:
            print(f"An error has ocurred: {e}")
    
    # Complexity: O(n)
    def delete_librarian(self,user_id):
        try:
            updated_data = []
            with open(self.csv_file,mode='r') as file:
                r = csv.DictReader(file)
                data = []
                for row in r:
                    data.append(row)
                updated_data = [dict for dict in data if dict['User_ID'] != user_id]
            with open(self.csv_file,mode='w') as file:
                fields = ["User_ID","Password"]
                w = csv.DictWriter(file,fieldnames=fields)
                w.writeheader()
                w.writerows(updated_data)
                print("Librarian details deleted!")
        
        except Exception as e:
            print(f"An error has occured: {e}")

class User:
    def __init__(self, librarian):
        self.librarian = librarian

    # Complexity: O(n)
    def search_book(self, keyword):
        current = self.librarian.head
        found = False
        while current:
            if keyword.lower() in current.book.title.lower() or keyword.lower() in current.book.author.lower():
                print(current.book)
                found = True
            current = current.next
        if not found:
            print(f"No books found matching '{keyword}'.")

    # Complexity: O(n)
    def search_by_isbn(self, isbn):
        isbn = str(isbn).strip()
        current = self.librarian.head
        while current:
            if str(current.book.isbn).strip() == isbn:
                return current.book
            current = current.next
        return f"Book with isbn {isbn} not found"

    # Complexity: O(n)
    def display_books(self):
        if not self.librarian.head:
            print("The library is empty.")
            return
        current = self.librarian.head
        while current:
            print(current.book)
            current = current.next

librarian = Librarian("C:\\Users\\jkyog\\Desktop\\data.csv", "C:\\Users\\jkyog\\Desktop\\borrower.csv")  
user = User(librarian)
librarian_manager = LibrarianManagement("C:\\Users\\jkyog\\Desktop\\Password_Manager.csv")

print("\n")

print("LIBRARY MANAGEMENT \n")

ch = input("Are you Librarian or User? (Enter 'l' for libraian and 'u' for user): ")
if ch == 'l':
    try:
        with open("C:\\Users\\jkyog\\Desktop\\Password_Manager.csv", mode='r+') as verifier:
            r = csv.reader(verifier)
            librarians = {}
            for rows in r:
                if len(rows) == 0:
                    continue
                else:
                    librarians[rows[0]] = rows[1]
            user_id = input("Enter User ID: ")
            password = input("Enter Password: ")
            if user_id in librarians:
                if librarians[user_id] == password:
                    while True:
                        print("What would you like to do? \n")
                        print("1. Display Books")
                        print("2. Search Book by Author/Title")
                        print("3. Search book by ISBN")
                        print("4. Add Book")
                        print("5. Delete Book")
                        print("6. Borrow a Book")
                        print("7. Return a Book")
                        print("8. Add Librarian")
                        print("9. Delete Librarian")
                        print("10. Exit")

                        ch = int(input("Enter your choice: "))
                        print("\n")

                        if ch == 1:
                            user.display_books()
    
                        elif ch == 2:
                            keyword = input("Enter a keyword (author name or book title): ")
                            user.search_book(keyword)

                        elif ch == 3:
                            isbn = int(input("Enter the book's ISBN: "))
                            print(user.search_by_isbn(isbn))

                        elif ch == 4:
                            title = input("Enter book title: ")
                            author = input("Enter author name: ")
                            isbn = int(input("Enter ISBN: "))
                            librarian.add_book(title,author,isbn)

                        elif ch == 5:
                            isbn = input("Enter ISBN of book to delete: ")
                            librarian.delete_book(isbn)
    
                        elif ch == 6:
                            isbn = int(input("Enter ISBN of book to borrow: "))
                            name = input("Enter borrower name: ")
                            contact = int(input("Enter borrower phone number: "))
                            librarian.mark_as_borrowed(isbn,name,contact)

                        elif ch == 7:
                            isbn = int(input("Enter ISBN of book to be returned: "))
                            librarian.mark_as_returned(isbn)

                        elif ch == 8:
                            print("Creating new Librarian login credentials")
                            user_id = input("Enter user ID: ")
                            password = input("Enter password: ")
                            librarian_manager.add_librarian(user_id,password)

                        elif ch == 9:
                            user_id = input("Enter user ID of librarian to delete: ")
                            librarian_manager.delete_librarian(user_id)

                        elif ch == 10:
                            print("Exiting. Bye!")
                            break

                        else:
                            print("Invalid input. Enter correct choice")
                            continue
                else:
                    print("Invalid Credentials")
            else:
                print("Invalid Credentials")
                    
    except Exception as e:
        print(f"There has been an error: {e}")

elif ch == 'u':
    print("Welcome to the library! \n")

    while True: 
        print("What would you like to do? \n")

        print("1. Display Books")
        print("2. Search Book by Author/Title")
        print("3. Search book by ISBN")
        print("4. Exit")

        ch = int(input("Enter your choice: "))
        print("\n")

        if ch == 1:
            user.display_books()
    
        elif ch == 2:
            keyword = input("Enter a keyword (author name or book title): ")
            user.search_book(keyword)

        elif ch == 3:
            isbn = input("Enter the ISBN of the book to search: ")
            print(user.search_by_isbn(isbn))

        elif ch == 4:
            print("Exiting. Bye!")
            break

        else:
            print("Invalid input. Enter correct choice")
            continue
