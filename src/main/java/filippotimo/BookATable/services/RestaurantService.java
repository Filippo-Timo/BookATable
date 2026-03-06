package filippotimo.BookATable.services;

import filippotimo.BookATable.entities.GenericUser;
import filippotimo.BookATable.entities.Restaurant;
import filippotimo.BookATable.entities.enums.RestaurantType;
import filippotimo.BookATable.entities.enums.Role;
import filippotimo.BookATable.exceptions.BadRequestException;
import filippotimo.BookATable.exceptions.NotFoundException;
import filippotimo.BookATable.exceptions.UnauthorizedException;
import filippotimo.BookATable.payloads.restaurantDTOs.CreateRestaurantDTO;
import filippotimo.BookATable.payloads.restaurantDTOs.UpdateRestaurantDTO;
import filippotimo.BookATable.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        // Controllo duplicati
        if (restaurantRepository.findByNameIgnoreCaseAndOwnerId(body.name(), currentUser.getId()).isPresent())
            throw new BadRequestException("You already have a restaurant with this name!");

        Restaurant restaurant = new Restaurant(
                currentUser,
                body.name(),
                body.city(),
                body.restaurantType(),
                body.description(),
                body.availableSeatsIndoor(),
                body.availableSeatsOutdoor(),
                body.phone()
        );

        return restaurantRepository.save(restaurant);
    }

    // ---------- READ ----------

    public Page<Restaurant> findAll(int page, int size, String orderBy, String sortCriteria) {
        Sort.Direction direction = sortCriteria.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, orderBy));
        return restaurantRepository.findAll(pageable);
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

    // ---------- UPDATE ----------

    public Restaurant update(UUID id, UpdateRestaurantDTO body, GenericUser currentUser) {

        Restaurant restaurant = findById(id);

        // Controllo che il ristorante appartenga all'utente loggato
        if (!restaurant.getOwner().getId().equals(currentUser.getId()))
            throw new UnauthorizedException("You are not the owner of this restaurant!");

        // Controllo duplicati — escludo il ristorante corrente
        restaurantRepository.findByNameIgnoreCaseAndOwnerId(body.name(), currentUser.getId())
                .ifPresent(r -> {
                    if (!r.getId().equals(id))
                        throw new BadRequestException("You already have a restaurant with this name!");
                });

        restaurant.setName(body.name());
        restaurant.setCity(body.city());
        restaurant.setRestaurantType(body.restaurantType());
        restaurant.setDescription(body.description());
        restaurant.setAvailableSeatsIndoor(body.availableSeatsIndoor());
        restaurant.setAvailableSeatsOutdoor(body.availableSeatsOutdoor());
        restaurant.setPhone(body.phone());

        return restaurantRepository.save(restaurant);
    }

    // ---------- DELETE ----------

    public void delete(UUID id, GenericUser currentUser) {

        Restaurant restaurant = findById(id);

        // Controllo che il ristorante appartenga all'utente loggato
        if (!restaurant.getOwner().getId().equals(currentUser.getId()))
            throw new UnauthorizedException("You are not the owner of this restaurant!");

        restaurantRepository.deleteById(id);
    }

}