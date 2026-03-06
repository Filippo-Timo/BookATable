package filippotimo.BookATable.services;

import filippotimo.BookATable.entities.Menu;
import filippotimo.BookATable.entities.Restaurant;
import filippotimo.BookATable.exceptions.BadRequestException;
import filippotimo.BookATable.exceptions.NotFoundException;
import filippotimo.BookATable.exceptions.UnauthorizedException;
import filippotimo.BookATable.payloads.menuDTOs.CreateMenuDTO;
import filippotimo.BookATable.payloads.menuDTOs.UpdateMenuDTO;
import filippotimo.BookATable.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantService restaurantService;

    @Autowired
    public MenuService(MenuRepository menuRepository, RestaurantService restaurantService) {
        this.menuRepository = menuRepository;
        this.restaurantService = restaurantService;
    }


    // ---------- CREATE ----------

    public Menu create(CreateMenuDTO body, UUID currentUserId) {

        // 1) Controllo che il ristorante esista
        Restaurant restaurant = restaurantService.findById(body.restaurantId());

        // 2) Controllo che il ristorante appartenga all'utente loggato
        if (!restaurant.getOwner().getId().equals(currentUserId))
            throw new UnauthorizedException("You are not the owner of this restaurant!");

        // 3) Controllo che non esista già un menu dello stesso tipo per questo ristorante
        boolean menuTypeExists = menuRepository.findByRestaurantId(restaurant.getId())
                .stream()
                .anyMatch(m -> m.getMenuType() == body.menuType());

        if (menuTypeExists)
            throw new BadRequestException("A menu of type " + body.menuType().name() + " already exists for this restaurant!");

        // 4) Creo e salvo il menu
        Menu menu = new Menu(restaurant, body.menuType());

        return menuRepository.save(menu);
    }


    // ---------- READ ----------

    public List<Menu> findByRestaurant(UUID restaurantId) {
        restaurantService.findById(restaurantId);
        return menuRepository.findByRestaurantId(restaurantId);
    }

    public Menu findById(UUID id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Menu with id " + id + " not found!"));
    }


    // ---------- UPDATE ----------

    public Menu update(UUID id, UpdateMenuDTO body, UUID currentUserId) {

        Menu menu = findById(id);

        // Controllo che il ristorante appartenga all'utente loggato
        if (!menu.getRestaurant().getOwner().getId().equals(currentUserId))
            throw new UnauthorizedException("You are not the owner of this restaurant!");

        // Controllo che non esista già un menu dello stesso tipo per questo ristorante
        boolean menuTypeExists = menuRepository.findByRestaurantId(menu.getRestaurant().getId())
                .stream()
                .anyMatch(m -> m.getMenuType() == body.menuType()
                        && !m.getId().equals(id)); // ← escludo il menu corrente

        if (menuTypeExists)
            throw new BadRequestException("A menu of type " + body.menuType().name() + " already exists for this restaurant!");

        menu.setMenuType(body.menuType());

        return menuRepository.save(menu);
    }


    // ---------- DELETE ----------

    public void delete(UUID id, UUID currentUserId) {

        Menu menu = findById(id);

        // Controllo che il ristorante appartenga all'utente loggato
        if (!menu.getRestaurant().getOwner().getId().equals(currentUserId))
            throw new UnauthorizedException("You are not the owner of this restaurant!");

        menuRepository.deleteById(id);
    }

}
