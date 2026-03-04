package filippotimo.BookATable.repositories;

import filippotimo.BookATable.entities.GenericUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GenericUserRepository extends JpaRepository<GenericUser, UUID> {

    Optional<GenericUser> findByEmail(String email);

    boolean existsByEmail(String email);

}
