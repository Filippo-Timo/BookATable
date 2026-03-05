package filippotimo.BookATable.services;

import filippotimo.BookATable.entities.GenericUser;
import filippotimo.BookATable.entities.Restaurant;
import filippotimo.BookATable.entities.enums.RestaurantType;
import filippotimo.BookATable.entities.enums.Role;
import filippotimo.BookATable.exceptions.NotFoundException;
import filippotimo.BookATable.exceptions.UnauthorizedException;
import filippotimo.BookATable.payloads.CreateRestaurantDTO;
import filippotimo.BookATable.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    // ---------- CREATE ----------

    public Restaurant create(CreateRestaurantDTO body, GenericUser currentUser) {

        // Controllo che l'utente sia un RESTAURANT_OWNER
        if (currentUser.getRole() != Role.RESTAURANT_OWNER)
            throw new UnauthorizedException("Only restaurant owners can create a restaurant!");

        Restaurant restaurant = new Restaurant(
                currentUser,
                body.city(),
                body.restaurantType(),
                body.maxSeats(),
                body.description(),
                body.availableSeatsIndoor(),
                body.availableSeatsOutdoor(),
                body.phone()
        );

        return restaurantRepository.save(restaurant);
    }

    // ---------- READ ----------

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public Restaurant findById(UUID id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant with id " + id + " not found!"));
    }

    public List<Restaurant> findByCity(String city) {
        return restaurantRepository.findByCityIgnoreCase(city);
    }

    public List<Restaurant> findByCityAndType(String city, String restaurantType) {
        return restaurantRepository.findByCityIgnoreCaseAndRestaurantType(
                city,
                RestaurantType.valueOf(restaurantType.toUpperCase())
        );
    }

}
