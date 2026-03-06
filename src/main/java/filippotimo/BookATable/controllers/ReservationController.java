package filippotimo.BookATable.controllers;

import filippotimo.BookATable.entities.GenericUser;
import filippotimo.BookATable.entities.Reservation;
import filippotimo.BookATable.exceptions.ValidationException;
import filippotimo.BookATable.payloads.reservationDTOs.CreateReservationDTO;
import filippotimo.BookATable.payloads.reservationDTOs.UpdateReservationDTO;
import filippotimo.BookATable.services.ReservationService;
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
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // 1. ---------- GET /api/reservations/my ----------

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('USER')")
    public List<Reservation> findMyReservations(
            @AuthenticationPrincipal GenericUser currentUser) {
        return reservationService.findByUser(currentUser);
    }


    // 2. ---------- GET /api/reservations/restaurant/{restaurantId} ----------

    @GetMapping("/restaurant/{restaurantId}")
    @PreAuthorize("hasAuthority('RESTAURANT_OWNER')")
    public List<Reservation> findByRestaurant(
            @PathVariable UUID restaurantId) {
        return reservationService.findByRestaurant(restaurantId);
    }


    // 3. ---------- GET /api/reservations/{id} ----------

    @GetMapping("/{id}")
    public Reservation findById(@PathVariable UUID id) {
        return reservationService.findById(id);
    }


    // 4. ---------- POST /api/reservations ----------

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public Reservation create(
            @RequestBody @Validated CreateReservationDTO body,
            BindingResult validationResult,
            @AuthenticationPrincipal GenericUser currentUser) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        return reservationService.create(body, currentUser);
    }


    // 5. ---------- PUT /api/reservations/{id} ----------

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public Reservation update(
            @PathVariable UUID id,
            @RequestBody @Validated UpdateReservationDTO body,
            BindingResult validationResult,
            @AuthenticationPrincipal GenericUser currentUser) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        return reservationService.update(id, body, currentUser);
    }


    // 6. ---------- DELETE /api/reservations/{id} ----------

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('USER')")
    public void delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal GenericUser currentUser) {
        reservationService.delete(id, currentUser);
    }

}
