package maids.library.demo.service;

import maids.library.demo.exception.BookNotFoundException;
import maids.library.demo.exception.BorrowingRecordNotFoundException;
import maids.library.demo.exception.PatronNotFoundException;
import maids.library.demo.model.entity.Book;
import maids.library.demo.model.entity.BorrowingRecord;
import maids.library.demo.model.entity.Patron;
import maids.library.demo.repository.BookCrudRepository;
import maids.library.demo.repository.BorrowingRecordCrudRepository;
import maids.library.demo.repository.PatronCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class LibraryService {
    @Autowired
    private final BookCrudRepository bookRepository;
    @Autowired
    private final PatronCrudRepository patronRepository;

    @Autowired
    private final BorrowingRecordCrudRepository borrowingRecordRepository;

    public LibraryService(BookCrudRepository bookRepository, PatronCrudRepository patronRepository, BorrowingRecordCrudRepository borrowingRecordRepository) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.borrowingRecordRepository = borrowingRecordRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }


    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> BookNotFoundException.builder()
                        .message("Book not found with id: " + id)
                        .build());
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book updatedBook) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> BookNotFoundException
                        .builder().message("Book not found with id: " + id)
                        .build());

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setPublicationYear(updatedBook.getPublicationYear());
        existingBook.setIsbn(updatedBook.getIsbn());

        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long id) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> BookNotFoundException.builder().message("Book not found with id: " + id)
                        .build());

        bookRepository.deleteById(id);
    }

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    public Patron getPatronById(Long id) {
        return patronRepository.findById(id)
                .orElseThrow(() -> PatronNotFoundException
                .builder().message("Book not found with id: " + id)
                .build());
    }

    public Patron addPatron(Patron patron) {
        return patronRepository.save(patron);
    }

    public Patron updatePatron(Long id, Patron updatedPatron) {
        Patron existingPatron = patronRepository.findById(id)
                .orElseThrow(() -> new PatronNotFoundException("Patron not found with id: " + id));

        existingPatron.setName(updatedPatron.getName());
        existingPatron.setContactInformation(updatedPatron.getContactInformation());

        return patronRepository.save(existingPatron);
    }

    public void deletePatron(Long id) {
        Patron existingPatron = patronRepository.findById(id)
                .orElseThrow(() -> new PatronNotFoundException("Patron not found with id: " + id));

        patronRepository.deleteById(id);
    }

    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        BookNotFoundException
                                .builder().message("Book not found with id: " + bookId)
                                .build());

        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() ->
                        PatronNotFoundException.builder()
                                .message("Patron not found with id: " + patronId)
                                .build());

        BorrowingRecord borrowingRecord = BorrowingRecord.builder()
                .book(book)
                .patron(patron)
                .build();

       return borrowingRecordRepository.save(borrowingRecord);
    }


    public void returnBook(Long bookId, Long patronId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> BookNotFoundException
                        .builder().message("Book not found with id: " + bookId)
                        .build());

        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> PatronNotFoundException.builder()
                        .message("Patron not found with id: " + patronId)
                        .build());

        BorrowingRecord borrowingRecord = borrowingRecordRepository.findByBookAndPatronAndReturnDateIsNull(book, patron)
                .orElseThrow(() -> BorrowingRecordNotFoundException.builder()
                        .message("No borrowing record found for book ID: " + bookId + " and patron ID: " + patronId)
                        .build());

        borrowingRecord.setReturnDate(LocalDate.now());
        borrowingRecordRepository.save(borrowingRecord);
    }

}