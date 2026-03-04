package filippotimo.BookATable.payloads.menuDTOs;

import filippotimo.BookATable.entities.enums.MenuType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateMenuDTO(
        @NotNull(message = "Menu type is required")
        MenuType menuType,

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name
) {
}
