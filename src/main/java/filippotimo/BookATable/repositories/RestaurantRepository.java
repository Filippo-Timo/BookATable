package filippotimo.BookATable.repositories;

import filippotimo.BookATable.entities.Restaurant;
import filippotimo.BookATable.entities.enums.RestaurantType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    // Trova tutti i ristoranti per città (case insensitive)
    List<Restaurant> findByCityIgnoreCase(String city);

    // Trova tutti i ristoranti per città e tipo (case insensitive)
    List<Restaurant> findByCityIgnoreCaseAndRestaurantType(
            String city,
            RestaurantType restaurantType
    );

    // Controlla se esiste già un ristorante con lo stesso nome per lo stesso owner
    Optional<Restaurant> findByNameIgnoreCaseAndOwnerId(String name, UUID ownerId);

    // Trova tutti i ristoranti di un owner
    List<Restaurant> findByOwnerId(UUID ownerId);

    // Trova tutti i ristoranti per tipo con paginazione
    Page<Restaurant> findByRestaurantType(RestaurantType restaurantType, Pageable pageable);

}