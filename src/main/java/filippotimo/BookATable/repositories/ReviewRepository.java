package filippotimo.BookATable.repositories;

import filippotimo.BookATable.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByRestaurantId(UUID restaurantId);

    List<Review> findByUserId(UUID userId);

    boolean existsByUserIdAndRestaurantId(UUID userId, UUID restaurantId);

}
