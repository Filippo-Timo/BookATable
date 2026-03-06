package filippotimo.BookATable.payloads.menuDTOs;

import filippotimo.BookATable.entities.enums.MenuType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateMenuDTO(

        @NotNull(message = "Restaurant ID is required")
        UUID restaurantId,

        @NotNull(message = "Menu type is required")
        MenuType menuType

) {
}
