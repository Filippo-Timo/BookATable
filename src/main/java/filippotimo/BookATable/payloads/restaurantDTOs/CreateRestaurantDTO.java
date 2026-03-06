package filippotimo.BookATable.payloads.restaurantDTOs;

import filippotimo.BookATable.entities.enums.RestaurantType;
import jakarta.validation.constraints.*;

public record CreateRestaurantDTO(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,
        @NotBlank(message = "City is required")
        @Size(min = 2, max = 30, message = "City must contain between 2 and 30 characters")
        String city,
        @NotNull(message = "Restaurant type is required")
        RestaurantType restaurantType,
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,
        @NotNull(message = "Available seats indoor is required")
        @Min(value = 0, message = "Available seats indoor cannot be negative")
        Integer availableSeatsIndoor,
        @NotNull(message = "Available seats outdoor is required")
        @Min(value = 0, message = "Available seats outdoor cannot be negative")
        Integer availableSeatsOutdoor,
        @NotBlank(message = "Phone is required")
        @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Invalid phone number")
        String phone
) {
}
