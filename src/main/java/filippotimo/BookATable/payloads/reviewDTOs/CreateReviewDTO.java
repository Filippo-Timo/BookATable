package filippotimo.BookATable.payloads.reviewDTOs;

import jakarta.validation.constraints.*;

public record CreateReviewDTO(
        @NotNull(message = "Rating is required")
        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating cannot exceed 5")
        Integer rating,

        @NotBlank(message = "Comment is required")
        @Size(min = 10, max = 500, message = "Comment must be between 10 and 500 characters")
        String comment
) {
}
