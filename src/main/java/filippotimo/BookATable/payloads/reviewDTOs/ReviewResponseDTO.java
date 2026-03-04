package filippotimo.BookATable.payloads.reviewDTOs;

import java.util.UUID;

public record ReviewResponseDTO(
        UUID id,
        Integer rating,
        String comment,

        // info dell'utente che ha scritto la recensione
        UUID userId,
        String userFullName,
        String userAvatar,

        // info del ristorante
        UUID restaurantId,
        String restaurantCity,
        String restaurantType
) {
}
