package maids.library.demo.repository;

import maids.library.demo.model.entity.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


@Repository
public interface PatronCrudRepository extends JpaRepository<Patron, Long> {
}
