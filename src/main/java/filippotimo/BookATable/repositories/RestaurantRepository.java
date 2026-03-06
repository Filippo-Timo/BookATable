package filippotimo.BookATable.repositories;

import filippotimo.BookATable.entities.Restaurant;
import filippotimo.BookATable.entities.enums.RestaurantType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    List<Restaurant> findByCityIgnoreCase(String city);

    List<Restaurant> findByCityIgnoreCaseAndRestaurantType(
            String city,
            RestaurantType restaurantType
    );

    Optional<Restaurant> findByNameIgnoreCaseAndOwnerId(String name, UUID ownerId);


}
