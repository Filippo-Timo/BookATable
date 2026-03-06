package filippotimo.BookATable.payloads.reservationDTOs;

import filippotimo.BookATable.entities.enums.SeatingPreference;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateReservationDTO(
        @NotNull(message = "Date is required")
        LocalDate date,

        @NotNull(message = "Time is required")
        LocalTime time,

        @NotNull(message = "Seats booked is required")
        @Min(value = 1, message = "Seats booked must be at least 1")
        @Max(value = 20, message = "Seats booked cannot exceed 20")
        Integer seatsBooked,

        @NotNull(message = "Seating preference is required")
        SeatingPreference seatingPreference
) {
}
