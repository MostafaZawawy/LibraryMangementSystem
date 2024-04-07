package maids.library.demo;

import maids.library.demo.exception.BookNotFoundException;
import maids.library.demo.exception.BorrowingRecordNotFoundException;
import maids.library.demo.exception.PatronNotFoundException;
import maids.library.demo.model.entity.Book;
import maids.library.demo.model.entity.BorrowingRecord;
import maids.library.demo.model.entity.Patron;
import maids.library.demo.repository.BookCrudRepository;
import maids.library.demo.repository.BorrowingRecordCrudRepository;
import maids.library.demo.repository.PatronCrudRepository;
import maids.library.demo.service.LibraryService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import static maids.library.demo.TestUtils.buildBook;
import static maids.library.demo.TestUtils.buildBorrowingRecord;
import static maids.library.demo.TestUtils.buildPatron;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class LibraryServiceTest {
    @Mock
    private BookCrudRepository bookRepository;

    @Mock
    private PatronCrudRepository patronRepository;

    @Mock
    private BorrowingRecordCrudRepository borrowingRecordRepository;

    @InjectMocks
    private LibraryService libraryService;

    @Test
    public void testReturnBook_Success() {
        Book book = buildBook();
        Patron patron = buildPatron(1L);
        BorrowingRecord borrowingRecord = buildBorrowingRecord(book, patron);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findByBookAndPatronAndReturnDateIsNull(any(), any())).thenReturn(Optional.of(borrowingRecord));

        libraryService.returnBook(1L, 1L);

        assertNotNull(borrowingRecord.getReturnDate());
    }

    @Test
    public void testReturnBook_BookNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> libraryService.returnBook(1L, 1L));
    }

    @Test
    public void testReturnBook_PatronNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(buildBook()));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PatronNotFoundException.class, () -> libraryService.returnBook(1L, 1L));
    }

    @Test
    public void testReturnBook_BorrowingRecordNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(buildBook()));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(buildPatron(1L)));
        when(borrowingRecordRepository.findByBookAndPatronAndReturnDateIsNull(any(), any())).thenReturn(Optional.empty());

        assertThrows(BorrowingRecordNotFoundException.class, () -> libraryService.returnBook(1L, 1L));
    }


    @Test
    public void testBorrowBook_Success() {
        Book book = buildBook();
        Patron patron = buildPatron(1L);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));

        assertDoesNotThrow(() -> libraryService.borrowBook(1L, 1L));
    }

    @Test
    public void testBorrowBook_BookNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> libraryService.borrowBook(1L, 1L));
    }

    @Test
    public void testBorrowBook_PatronNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(buildBook()));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PatronNotFoundException.class, () -> libraryService.borrowBook(1L, 1L));
    }

    @Test
    public void testDeletePatron_Success() {
        Patron patron = buildPatron(1L);

        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));

        assertDoesNotThrow(() -> libraryService.deletePatron(1L));
    }

    @Test
    public void testDeletePatron_PatronNotFound() {
        when(patronRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PatronNotFoundException.class, () -> libraryService.deletePatron(1L));
    }

    @Test
    public void testUpdatePatron_Success() {

        Patron patron=buildPatron(1L);
        when(patronRepository.findById(1L)).thenReturn(Optional.ofNullable(patron));
        when(patronRepository.save(patron)).thenReturn(patron);
        Patron updatedPatron = buildUpdatedPatron(patron);
        Patron returnedPatron = libraryService.updatePatron(patron.getId(), updatedPatron);



        assertEquals(updatedPatron.getName(), returnedPatron.getName());
    }

    private Patron buildUpdatedPatron(Patron existingPatron) {
        return Patron.builder()
                .id(existingPatron.getId())
                .name("Updated Name")
                .contactInformation("Updated Contact Information").build();
    }
    @Test
    public void testAddPatron_Success() {
        Patron patron = buildPatron(1L);

        when(patronRepository.save(any())).thenReturn(patron);

        assertDoesNotThrow(() -> libraryService.addPatron(patron));
    }

    @Test
    public void testGetPatronById_Success() {
        Patron patron = buildPatron(1L);

        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));

        assertEquals(patron, libraryService.getPatronById(1L));
    }

    @Test
    public void testGetPatronById_PatronNotFound() {
        when(patronRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PatronNotFoundException.class, () -> libraryService.getPatronById(1L));
    }

    @Test
    public void testGetAllPatrons() {
        Patron patron = buildPatron(1L);
        List<Patron> patrons = Collections.singletonList(patron);

        when(patronRepository.findAll()).thenReturn(patrons);

        assertEquals(patrons, libraryService.getAllPatrons());
    }

    @Test
    public void testDeleteBook_Success() {
        Book book = buildBook();

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        assertDoesNotThrow(() -> libraryService.deleteBook(1L));
    }

    @Test
    public void testDeleteBook_BookNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> libraryService.deleteBook(1L));
    }

    @Test
    public void testUpdateBook_Success() {
        Book book = buildBook();
        when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(book));
        when(bookRepository.save(book)).thenReturn(book);

        Book updatedBook = buildUpdatedBook(book);

        Book returnedBook = libraryService.updateBook(book.getId(), updatedBook);

        assertEquals(updatedBook.getTitle(), returnedBook.getTitle());
    }
    private Book buildUpdatedBook(Book existingBook) {
        return Book.builder()
                .id(existingBook.getId())
                .title("Updated Book")
                .author("Updated Author")
                .isbn("Updated Isbn")
                .publicationYear("2024")
                .build();
    }

    @Test
    public void testGetAllBooks() {
        Book book = buildBook();
        List<Book> books = Collections.singletonList(book);

        when(bookRepository.findAll()).thenReturn(books);

        assertEquals(books, libraryService.getAllBooks());
    }

    @Test
    public void testGetBookById_Success() {
        Book book = buildBook();

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        assertEquals(book, libraryService.getBookById(1L));
    }

    @Test
    public void testGetBookById_BookNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> libraryService.getBookById(1L));
    }

    @Test
    public void testAddBook_Success() {
        Book book = buildBook();

        when(bookRepository.save(any())).thenReturn(book);

        assertDoesNotThrow(() -> libraryService.addBook(book));
    }

}
