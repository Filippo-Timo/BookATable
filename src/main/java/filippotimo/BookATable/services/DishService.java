package filippotimo.BookATable.services;

import filippotimo.BookATable.entities.Dish;
import filippotimo.BookATable.entities.Menu;
import filippotimo.BookATable.exceptions.BadRequestException;
import filippotimo.BookATable.exceptions.NotFoundException;
import filippotimo.BookATable.exceptions.UnauthorizedException;
import filippotimo.BookATable.payloads.dishDTOs.CreateDishDTO;
import filippotimo.BookATable.payloads.dishDTOs.UpdateDishDTO;
import filippotimo.BookATable.repositories.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DishService {

    private final DishRepository dishRepository;
    private final MenuService menuService;

    @Autowired
    public DishService(DishRepository dishRepository, MenuService menuService) {
        this.dishRepository = dishRepository;
        this.menuService = menuService;
    }

    // ---------- CREATE ----------

    public Dish create(CreateDishDTO body, UUID currentUserId) {

        // 1) Controllo che il menu esista
        Menu menu = menuService.findById(body.menuId());

        // 2) Controllo che il ristorante appartenga all'utente loggato
        if (!menu.getRestaurant().getOwner().getId().equals(currentUserId))
            throw new UnauthorizedException("You are not the owner of this restaurant!");

        // 3) Controllo che non esista già un piatto con lo stesso nome in questo menu
        boolean dishExists = dishRepository.findByMenuId(menu.getId())
                .stream()
                .anyMatch(d -> d.getName().equalsIgnoreCase(body.name()));

        if (dishExists)
            throw new BadRequestException("A dish with name '" + body.name() + "' already exists in this menu!");

        // 4) Creo e salvo il piatto
        Dish dish = new Dish(
                menu,
                body.name(),
                body.ingredients(),
                body.price()
        );

        return dishRepository.save(dish);
    }

    // ---------- READ ----------

    public List<Dish> findByMenu(UUID menuId) {
        menuService.findById(menuId);
        return dishRepository.findByMenuId(menuId);
    }

    public Dish findById(UUID id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dish with id " + id + " not found!"));
    }

    // ---------- UPDATE ----------

    public Dish update(UUID id, UpdateDishDTO body, UUID currentUserId) {

        Dish dish = findById(id);

        // Controllo che il ristorante appartenga all'utente loggato
        if (!dish.getMenu().getRestaurant().getOwner().getId().equals(currentUserId))
            throw new UnauthorizedException("You are not the owner of this restaurant!");

        // Controllo duplicati — escludo il piatto corrente
        boolean dishExists = dishRepository.findByMenuId(dish.getMenu().getId())
                .stream()
                .anyMatch(d -> d.getName().equalsIgnoreCase(body.name())
                        && !d.getId().equals(id));

        if (dishExists)
            throw new BadRequestException("A dish with name '" + body.name() + "' already exists in this menu!");

        dish.setName(body.name());
        dish.setIngredients(body.ingredients());
        dish.setPrice(body.price());

        return dishRepository.save(dish);
    }

    // ---------- DELETE ----------

    public void delete(UUID id, UUID currentUserId) {

        Dish dish = findById(id);

        // Controllo che il ristorante appartenga all'utente loggato
        if (!dish.getMenu().getRestaurant().getOwner().getId().equals(currentUserId))
            throw new UnauthorizedException("You are not the owner of this restaurant!");

        dishRepository.deleteById(id);
    }

}
