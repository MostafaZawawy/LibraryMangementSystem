package maids.library.demo.repository;

import maids.library.demo.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookCrudRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
    long countByAuthor(String author);
    List<Book> findByAuthor(String author);
}
