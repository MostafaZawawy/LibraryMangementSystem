# Running and Interacting with Library Management System APIs

## Running the Library Management System:

### 1. Setup Environment:
- Ensure JDK is installed.
- Install MySQL.
- Clone/download project source code.

### 2. Configure Database:
- Create a database named `library_db` in MySQL.

### 3. Configure Application Properties:
- Update `application.properties` with database connection details:
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/library_db
  spring.datasource.username=<your_mysql_username>
  spring.datasource.password=<your_mysql_password>
  ```

### 4. Build and Run the Application:
- Navigate to project root directory.
- Build: `./gradlew build`
- Run: `./gradlew bootRun`

### 5. Accessing Swagger UI:
- Open browser and go to `http://localhost:8080/swagger-ui/`.
- Explore available APIs.

## Interacting with the APIs:

1. **Viewing Book Inventory:**
    - Endpoint: `/api/books`
    - Method: GET
    - Retrieves list of all books.

2. **Adding a New Book:**
    - Endpoint: `/api/books`
    - Method: POST
    - Send POST request with book details in request body.

3. **Updating Book Details:**
    - Endpoint: `/api/books/{bookId}`
    - Method: PUT
    - Replace `{bookId}` with book's ID.
    - Send PUT request with updated book details.

4. **Deleting a Book:**
    - Endpoint: `/api/books/{bookId}`
    - Method: DELETE
    - Replace `{bookId}` with book's ID.
    - Send DELETE request to remove book.

5. **Searching for Books:**
    - Endpoint: `/api/books/search`
    - Method: GET
    - Search books by title, author, or category.

6. **Managing Library Users:**
    - CRUD operations for managing library users.

7. **Handling Book Loans and Returns:**
    - Endpoints for managing book loans and returns.

8. **Authentication and Authorization:**
    - Implement authentication and authorization mechanisms if needed.
- ## NOTE:
      When adding/updating
      a book or a patron please send all details required
      in request body
