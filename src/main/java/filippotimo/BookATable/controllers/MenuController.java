package filippotimo.BookATable.controllers;

import filippotimo.BookATable.entities.GenericUser;
import filippotimo.BookATable.entities.Menu;
import filippotimo.BookATable.exceptions.ValidationException;
import filippotimo.BookATable.payloads.menuDTOs.CreateMenuDTO;
import filippotimo.BookATable.payloads.menuDTOs.UpdateMenuDTO;
import filippotimo.BookATable.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }


    // 1. ---------- GET /api/restaurants/{restaurantId}/menus ----------

    @GetMapping("/restaurants/{restaurantId}/menus")
    public List<Menu> findByRestaurant(@PathVariable UUID restaurantId) {
        return menuService.findByRestaurant(restaurantId);
    }


    // 2. ---------- GET /api/menus/{id} ----------

    @GetMapping("/menus/{id}")
    public Menu findById(@PathVariable UUID id) {
        return menuService.findById(id);
    }


    // 3. ---------- POST /api/menus ----------

    @PostMapping("/menus")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('RESTAURANT_OWNER')")
    public Menu create(@RequestBody @Validated CreateMenuDTO body,
                       BindingResult validationResult,
                       @AuthenticationPrincipal GenericUser currentUser) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        // ← passa currentUser.getId() invece dell'oggetto intero
        return menuService.create(body, currentUser.getId());
    }


    // 4. ---------- PUT /api/menus/{id} ----------

    @PutMapping("/menus/{id}")
    @PreAuthorize("hasAuthority('RESTAURANT_OWNER')")
    public Menu update(@PathVariable UUID id,
                       @RequestBody @Validated UpdateMenuDTO body,
                       BindingResult validationResult,
                       @AuthenticationPrincipal GenericUser currentUser) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        return menuService.update(id, body, currentUser.getId());
    }


    // 5. ---------- DELETE /api/menus/{id} ----------
    
    @DeleteMapping("/menus/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('RESTAURANT_OWNER')")
    public void delete(@PathVariable UUID id,
                       @AuthenticationPrincipal GenericUser currentUser) {
        menuService.delete(id, currentUser.getId());
    }

}
