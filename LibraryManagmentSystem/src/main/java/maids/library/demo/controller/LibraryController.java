package maids.library.demo.controller;

import jakarta.validation.Valid;
import maids.library.demo.exception.BookNotFoundException;
import maids.library.demo.exception.PatronNotFoundException;
import maids.library.demo.model.entity.Book;
import maids.library.demo.model.entity.Patron;
import maids.library.demo.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LibraryController {
    @Autowired
    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @Operation(summary = "Retrieve a list of all books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of books retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No books found")
    })
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = libraryService.getAllBooks();
        if (books.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Retrieve details of a specific book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping(value = "/books/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book existingBook = libraryService.getBookById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity.ok().headers(headers).body(existingBook);
    }

    @Operation(summary = "Add a new book to the library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book added successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Book addedBook = libraryService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);
    }

    @Operation(summary = "Update an existing book's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id,@Valid @RequestBody Book updatedBook) {
        Book book = libraryService.updateBook(id, updatedBook);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Remove a book from the library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        libraryService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retrieve a list of all patrons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of patrons retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No patrons found")
    })
    @GetMapping("/patrons")
    public ResponseEntity<List<Patron>> getAllPatrons() {
        List<Patron> patrons = libraryService.getAllPatrons();
        if (patrons.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(patrons);
    }

    @Operation(summary = "Retrieve details of a specific patron by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patron details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Patron not found")
    })
    @GetMapping("/patrons/{id}")
    public ResponseEntity<Patron> getPatronById(@PathVariable Long id) {
        Patron patron = libraryService.getPatronById(id);
        return ResponseEntity.ok(patron);
    }

    @Operation(summary = "Add a new patron to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patron added successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/patrons")
    public ResponseEntity<Patron> addPatron(@Valid @RequestBody Patron patron) {
        Patron addedPatron = libraryService.addPatron(patron);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedPatron);
    }

    @Operation(summary = "Update an existing patron's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patron updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Patron not found")
    })
    @PutMapping("/patrons/{id}")
    public ResponseEntity<Patron> updatePatron(@PathVariable Long id, @Valid @RequestBody Patron updatedPatron) {
        Patron patron = libraryService.updatePatron(id, updatedPatron);
        return ResponseEntity.ok(patron);
    }

    @Operation(summary = "Remove a patron from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patron deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Patron not found")
    })
    @DeleteMapping("/patrons/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {
        libraryService.deletePatron(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Allow a patron to borrow a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book borrowed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Book or patron not found")
    })
    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<Void> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        libraryService.borrowBook(bookId, patronId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Record the return of a borrowed book by a patron")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book returned successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Book or patron not found")
    })
    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<Void> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        libraryService.returnBook(bookId, patronId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(PatronNotFoundException.class)
    public ResponseEntity<Object> handlePatronNotFoundException(PatronNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
