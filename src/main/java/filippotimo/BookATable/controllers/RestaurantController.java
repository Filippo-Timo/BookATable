package filippotimo.BookATable.controllers;

import filippotimo.BookATable.entities.GenericUser;
import filippotimo.BookATable.entities.Restaurant;
import filippotimo.BookATable.exceptions.ValidationException;
import filippotimo.BookATable.payloads.restaurantDTOs.CreateRestaurantDTO;
import filippotimo.BookATable.payloads.restaurantDTOs.UpdateRestaurantDTO;
import filippotimo.BookATable.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    // 1. ---------- GET /api/restaurants ----------

    @GetMapping
    public Page<Restaurant> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "city") String orderBy,
            @RequestParam(defaultValue = "asc") String sortCriteria) {
        return restaurantService.findAll(page, size, orderBy, sortCriteria);
    }

    // 2. ---------- GET /api/restaurants/my ----------

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('RESTAURANT_OWNER')")
    public List<Restaurant> findMyRestaurants(@AuthenticationPrincipal GenericUser currentUser) {
        return restaurantService.findByOwner(currentUser);
    }

    // 3. ---------- GET /api/restaurants/{id} ----------

    @GetMapping("/{id}")
    public Restaurant findById(@PathVariable UUID id) {
        return restaurantService.findById(id);
    }

    // 4. ---------- GET /api/restaurants/city/{city} ----------

    @GetMapping("/city/{city}")
    public List<Restaurant> findByCity(@PathVariable String city) {
        return restaurantService.findByCity(city);
    }

    // 5. ---------- GET /api/restaurants/search?city=Roma&type=PIZZERIA ----------

    @GetMapping("/search")
    public List<Restaurant> findByCityAndType(@RequestParam String city,
                                              @RequestParam String restaurantType) {
        return restaurantService.findByCityAndType(city, restaurantType);
    }

    // 6. ---------- POST /api/restaurants ----------

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('RESTAURANT_OWNER')")
    public Restaurant create(@RequestBody @Validated CreateRestaurantDTO body,
                             BindingResult validationResult,
                             @AuthenticationPrincipal GenericUser currentUser) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        return restaurantService.create(body, currentUser);
    }

    // 7. ---------- PUT /api/restaurants/{id} ----------

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('RESTAURANT_OWNER')")
    public Restaurant update(@PathVariable UUID id,
                             @RequestBody @Validated UpdateRestaurantDTO body,
                             BindingResult validationResult,
                             @AuthenticationPrincipal GenericUser currentUser) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        return restaurantService.update(id, body, currentUser);
    }

    // 8. ---------- PATCH /api/restaurants/{id}/image ----------

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('RESTAURANT_OWNER')")
    public Restaurant uploadImage(@PathVariable UUID id,
                                  @RequestParam("image") MultipartFile image,
                                  @AuthenticationPrincipal GenericUser currentUser) throws IOException {
        return restaurantService.uploadImage(id, image, currentUser);
    }

    // 9. ---------- DELETE /api/restaurants/{id} ----------

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('RESTAURANT_OWNER')")
    public void delete(@PathVariable UUID id,
                       @AuthenticationPrincipal GenericUser currentUser) {
        restaurantService.delete(id, currentUser);
    }

}
