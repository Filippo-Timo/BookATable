package filippotimo.BookATable.payloads.userDTOs;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserDTO(
        @NotBlank(message = "Email address is required")
        @Email(message = "The email address you entered is not in the correct format!")
        String email,
        @NotBlank(message = "Password is required")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{4,}$", message = "The password must contain a capital letter, a lowercase letter and a special character")
        String password,
        @NotBlank(message = "FirstName is required")
        @Size(min = 2, max = 30, message = "The firstName must contain between 2 and 30 characters")
        String firstName,
        @NotBlank(message = "LastName is required")
        @Size(min = 2, max = 30, message = "The lastName must contain between 2 and 30 characters")
        String lastName,
        @NotNull(message = "birthDate is required")
        @Past(message = "The birth date cannot be today or in the future")
        LocalDate birthDate,
        @NotBlank(message = "city is required")
        String city
) {
}
