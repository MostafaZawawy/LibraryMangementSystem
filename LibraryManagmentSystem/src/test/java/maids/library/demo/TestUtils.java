package maids.library.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import maids.library.demo.model.entity.Book;
import maids.library.demo.model.entity.BorrowingRecord;
import maids.library.demo.model.entity.Patron;

public class TestUtils {
    public static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Book buildBook() {
        return Book.builder()
                .id(1L)
                .title("Dummy Book")
                .author("Dummy Author")
                .title("book1")
                .build();
    }

    public static Patron buildPatron(Long id) {
        return Patron.builder()
                .id(id)
                .name("Dummy Patron")
                .build();
    }

    public static BorrowingRecord buildBorrowingRecord(Book book, Patron patron) {
        return BorrowingRecord.builder()
                .id(1L)
                .book(book)
                .patron(patron)
                .build();
    }
    public static Patron buildUpdatedPatron(Long id) {
        return Patron.builder()
                .id(id)
                .name("Updated Name")
                .contactInformation("Updated Contact Information").build();
    }

}
