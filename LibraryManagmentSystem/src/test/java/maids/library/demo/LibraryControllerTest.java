package maids.library.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import maids.library.demo.controller.LibraryController;
import maids.library.demo.exception.BookNotFoundException;
import maids.library.demo.exception.PatronNotFoundException;
import maids.library.demo.model.entity.Book;
import maids.library.demo.model.entity.BorrowingRecord;
import maids.library.demo.model.entity.Patron;
import maids.library.demo.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static maids.library.demo.TestUtils.asJsonString;
import static maids.library.demo.TestUtils.buildBook;
import static maids.library.demo.TestUtils.buildPatron;
import static maids.library.demo.TestUtils.buildUpdatedPatron;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibraryController.class)
@ExtendWith(SpringExtension.class)
public class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryService libraryService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void givenValidBookId_whenGetBookById_thenRetrieveBookDetails() throws Exception {
        Book book = buildBook();

        given(libraryService.getBookById(book.getId())).willReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(book.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("book1"));
    }

    @Test
    public void givenNonExistingBookId_whenGetBookById_thenReturnNotFound() throws Exception {
        long bookId = 1L;
        given(libraryService.getBookById(bookId)).willThrow(BookNotFoundException.builder().build());

        ResultActions response =  mockMvc.perform(MockMvcRequestBuilders.get("/api/books/{id}", bookId)
                .contentType(MediaType.APPLICATION_JSON));
        ;

        response.andExpect(status().isNotFound());
    }

    @Test
    public void givenExistingBooks_whenGetAllBooks_thenReturnBookList() throws Exception {
        Book book = buildBook();
        when(libraryService.getAllBooks()).thenReturn(Collections.singletonList(book));


        mockMvc.perform(get("/api/books").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("book1"));
    }

    @Test
    public void givenNoBooks_whenGetAllBooks_thenReturnNotFound() throws Exception {
        // given
        when(libraryService.getAllBooks()).thenReturn(Collections.emptyList());

        // when
        mockMvc.perform(get("/api/books").contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenNewBook_whenAddBook_thenStatusCreated() throws Exception {
        // given
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        when(libraryService.addBook(any())).thenReturn(book);

        // when
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))


                // then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    public void givenExistingBook_whenUpdateBook_thenStatusOk() throws Exception {
        Long bookId = 1L;
        Book updatedBook = Book.builder()
                .id(bookId)
                .title("Updated Book")
                .author("Updated Author")
                .isbn("Updated Isbn")
                .publicationYear("2024")
                .build();

        when(libraryService.updateBook(any(), any())).thenReturn(updatedBook);

        mockMvc.perform(put("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Updated Book"));
    }
    @Test
    public void givenBookId_whenDeleteBook_thenStatusNoContent() throws Exception {
        Long bookId = 1L;
        doNothing().when(libraryService).deleteBook(bookId);

        mockMvc.perform(delete("/api/books/{id}", bookId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenNoPatrons_whenGetAllPatrons_thenReturnNotFound() throws Exception {

        List<Patron> patrons = Collections.emptyList();
        when(libraryService.getAllPatrons()).thenReturn(patrons);

        mockMvc.perform(get("/api/patrons"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenPatrons_whenGetAllPatrons_thenReturnList() throws Exception {
        List<Patron> patrons =  new ArrayList<>();
        patrons.add(buildPatron(1L));
        patrons.add(buildPatron(2L));
        when(libraryService.getAllPatrons()).thenReturn(patrons);

        mockMvc.perform(get("/api/patrons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Dummy Patron"))
                .andExpect(jsonPath("$[1].name").value("Dummy Patron"));
    }

    @Test
    public void givenPatronId_whenGetPatronById_thenReturnPatronObject() throws Exception {
        Patron patron = buildPatron(1L);
        given(libraryService.getPatronById(1L)).willReturn(patron);

        mockMvc.perform(get("/api/patrons/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(patron.getName()))
                .andExpect(jsonPath("$.contactInformation").value(patron.getContactInformation()));
    }

    @Test
    public void givenNonExistingPatronId_whenGetPatronById_thenReturnNotFound() throws Exception {
        given(libraryService.getPatronById(1L)).willThrow(PatronNotFoundException.builder().build());

        mockMvc.perform(get("/api/patrons/1"))
                .andExpect(status().isNotFound());
    }


    @Test
    public void givenValidPatron_whenAddPatron_thenReturnCreatedStatus() throws Exception {
        Patron patron = buildPatron(1L);
        given(libraryService.addPatron(any(Patron.class))).willReturn(patron);

        String patronJson = "{\"name\": \"John Doe\", \"contactInformation\": \"john@example.com\"}";

        mockMvc.perform(post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patronJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Dummy Patron"));
    }

    @Test
    public void givenInvalidPatron_whenAddPatron_thenReturnBadRequest() throws Exception {
        String invalidPatronJson = "{\"size\": \"john@example.com\"}"; // Missing name field

        mockMvc.perform(post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPatronJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenExistingPatronId_whenUpdatePatron_thenReturnUpdatedPatron() throws Exception {
        long patronId = 1L;
        Patron updatedPatron = buildUpdatedPatron(patronId);
        given(libraryService.updatePatron(patronId, updatedPatron)).willReturn(updatedPatron);

        ResultActions response = mockMvc.perform(put("/api/patrons/{id}", patronId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedPatron)));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(updatedPatron.getName()))
                .andExpect(jsonPath("$.contactInformation").value(updatedPatron.getContactInformation()));
    }

    @Test
    public void givenNonExistingPatronId_whenUpdatePatron_thenReturnNotFound() throws Exception {
        long patronId = 1L;
        Patron updatedPatron = buildUpdatedPatron(patronId);
        given(libraryService.updatePatron(patronId, updatedPatron)).willThrow(PatronNotFoundException.builder().build());

        ResultActions response = mockMvc.perform(put("/api/patrons/{id}", patronId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedPatron)));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void givenExistingPatronId_whenDeletePatron_thenReturnNoContent() throws Exception {
        long patronId = 1L;
        mockMvc.perform(delete("/api/patrons/{id}", patronId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenNonExistingPatronId_whenDeletePatron_thenReturnNotFound() throws Exception {
        long patronId = 1L;
        doThrow(PatronNotFoundException.builder().build()).when(libraryService).deletePatron(patronId);

        mockMvc.perform(delete("/api/patrons/{id}", patronId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenValidBookIdAndPatronId_whenBorrowBook_thenStatusCreated() throws Exception {
        long bookId = 1L;
        long patronId = 1L;

        mockMvc.perform(post("/api/borrow/{bookId}/patron/{patronId}", bookId, patronId))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenInvalidBookId_whenBorrowBook_thenStatusNotFound() throws Exception {
        long bookId = 1L;
        long patronId = 1L;
        given(libraryService.borrowBook(bookId, patronId)).willThrow(BookNotFoundException.builder().build());

        mockMvc.perform(post("/api/borrow/{bookId}/patron/{patronId}", bookId, patronId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenInvalidPatronId_whenBorrowBook_thenStatusNotFound() throws Exception {
        long bookId = 1L;
        long patronId = 1L;
        given(libraryService.borrowBook(bookId, patronId)).willThrow(PatronNotFoundException.builder().build());

        mockMvc.perform(post("/api/borrow/{bookId}/patron/{patronId}", bookId, patronId))
                .andExpect(status().isNotFound());
    }

}
