package filippotimo.BookATable.payloads.dishDTOs;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateDishDTO(
        @NotNull(message = "Menu ID is required")
        UUID menuId,

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name,

        @NotBlank(message = "Ingredients is required")
        @Size(max = 500, message = "Ingredients cannot exceed 500 characters")
        String ingredients,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        @Digits(integer = 5, fraction = 2, message = "Price format: max 99999.99")
        BigDecimal price
) {
}
