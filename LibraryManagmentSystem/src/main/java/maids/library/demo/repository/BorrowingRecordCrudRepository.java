package maids.library.demo.repository;
import maids.library.demo.model.entity.Book;
import maids.library.demo.model.entity.BorrowingRecord;
import maids.library.demo.model.entity.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowingRecordCrudRepository extends JpaRepository<BorrowingRecord, Long> {
    List<BorrowingRecord> findByPatron(Patron patron);
    Optional<BorrowingRecord> findByBookAndPatronAndReturnDateIsNull(Book book, Patron patron);
}
