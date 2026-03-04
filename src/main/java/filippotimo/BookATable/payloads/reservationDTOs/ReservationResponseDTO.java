package filippotimo.BookATable.payloads.reservationDTOs;

import filippotimo.BookATable.entities.enums.SeatingPreference;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record ReservationResponseDTO(
        UUID id,
        
        // info utente
        UUID userId,
        String userFullName,

        // info ristorante
        UUID restaurantId,
        String restaurantCity,
        String restaurantType,

        // info prenotazione
        LocalDate date,
        LocalTime time,
        Integer seatsBooked,
        SeatingPreference seatingPreference
) {
}
