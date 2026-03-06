package filippotimo.BookATable.controllers;

import filippotimo.BookATable.entities.Dish;
import filippotimo.BookATable.entities.GenericUser;
import filippotimo.BookATable.exceptions.ValidationException;
import filippotimo.BookATable.payloads.dishDTOs.CreateDishDTO;
import filippotimo.BookATable.payloads.dishDTOs.UpdateDishDTO;
import filippotimo.BookATable.services.DishService;
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
public class DishController {

    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }


    // 1. ---------- GET /api/menus/{menuId}/dishes ----------

    @GetMapping("/menus/{menuId}/dishes")
    public List<Dish> findByMenu(@PathVariable UUID menuId) {
        return dishService.findByMenu(menuId);
    }


    // 2. ---------- GET /api/dishes/{id} ----------

    @GetMapping("/dishes/{id}")
    public Dish findById(@PathVariable UUID id) {
        return dishService.findById(id);
    }


    // 3. ---------- POST /api/dishes ----------

    @PostMapping("/dishes")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('RESTAURANT_OWNER')")
    public Dish create(@RequestBody @Validated CreateDishDTO body,
                       BindingResult validationResult,
                       @AuthenticationPrincipal GenericUser currentUser) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        return dishService.create(body, currentUser.getId());
    }


    // 4. ---------- PUT /api/dishes/{id} ----------

    @PutMapping("/dishes/{id}")
    @PreAuthorize("hasAuthority('RESTAURANT_OWNER')")
    public Dish update(@PathVariable UUID id,
                       @RequestBody @Validated UpdateDishDTO body,
                       BindingResult validationResult,
                       @AuthenticationPrincipal GenericUser currentUser) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        return dishService.update(id, body, currentUser.getId());
    }


    // 5. ---------- DELETE /api/dishes/{id} ----------

    @DeleteMapping("/dishes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('RESTAURANT_OWNER')")
    public void delete(@PathVariable UUID id,
                       @AuthenticationPrincipal GenericUser currentUser) {
        dishService.delete(id, currentUser.getId());
    }

}
