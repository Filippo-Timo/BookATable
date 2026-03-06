package filippotimo.BookATable.controllers;

import filippotimo.BookATable.entities.GenericUser;
import filippotimo.BookATable.entities.Review;
import filippotimo.BookATable.exceptions.ValidationException;
import filippotimo.BookATable.payloads.reviewDTOs.CreateReviewDTO;
import filippotimo.BookATable.payloads.reviewDTOs.UpdateReviewDTO;
import filippotimo.BookATable.services.ReviewService;
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
@RequestMapping("/api/restaurants/{restaurantId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 1. ---------- GET /api/restaurants/{restaurantId}/reviews ----------

    @GetMapping
    public List<Review> findByRestaurant(@PathVariable UUID restaurantId) {
        return reviewService.findByRestaurant(restaurantId);
    }


    // 2. ---------- GET /api/restaurants/{restaurantId}/reviews/{id} ----------

    @GetMapping("/{id}")
    public Review findById(@PathVariable UUID restaurantId,
                           @PathVariable UUID id) {
        return reviewService.findById(id);
    }


    // 3. ---------- POST /api/restaurants/{restaurantId}/reviews ----------

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public Review create(@PathVariable UUID restaurantId,
                         @RequestBody @Validated CreateReviewDTO body,
                         BindingResult validationResult,
                         @AuthenticationPrincipal GenericUser currentUser) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        return reviewService.create(restaurantId, body, currentUser);
    }


    // 4. ---------- PUT /api/restaurants/{restaurantId}/reviews/{id} ----------

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public Review update(@PathVariable UUID restaurantId,
                         @PathVariable UUID id,
                         @RequestBody @Validated UpdateReviewDTO body,
                         BindingResult validationResult,
                         @AuthenticationPrincipal GenericUser currentUser) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        return reviewService.update(id, body, currentUser);
    }


    // 5. ---------- DELETE /api/restaurants/{restaurantId}/reviews/{id} ----------

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('USER')")
    public void delete(@PathVariable UUID restaurantId,
                       @PathVariable UUID id,
                       @AuthenticationPrincipal GenericUser currentUser) {
        reviewService.delete(id, currentUser);
    }

}
