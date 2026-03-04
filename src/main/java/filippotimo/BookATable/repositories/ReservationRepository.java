package filippotimo.BookATable.repositories;

import filippotimo.BookATable.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    List<Reservation> findByUserId(UUID userId);

    List<Reservation> findByRestaurantId(UUID restaurantId);

    List<Reservation> findByRestaurantIdAndDate(UUID restaurantId, LocalDate date);

}
