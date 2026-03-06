package filippotimo.BookATable.payloads.menuDTOs;

import filippotimo.BookATable.entities.enums.MenuType;
import jakarta.validation.constraints.NotNull;

public record UpdateMenuDTO(

        @NotNull(message = "Menu type is required")
        MenuType menuType

) {
}
