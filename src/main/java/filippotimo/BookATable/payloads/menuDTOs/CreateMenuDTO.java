package filippotimo.BookATable.payloads.menuDTOs;

import filippotimo.BookATable.entities.enums.MenuType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMenuDTO(

        @NotBlank(message = "Restaurant name is required")
        String restaurantName,

        @NotNull(message = "Menu type is required")
        MenuType menuType

) {
}
