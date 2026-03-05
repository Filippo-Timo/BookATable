package filippotimo.BookATable.controllers;

import filippotimo.BookATable.entities.GenericUser;
import filippotimo.BookATable.exceptions.ValidationException;
import filippotimo.BookATable.payloads.userDTOs.LoginDTO;
import filippotimo.BookATable.payloads.userDTOs.LoginResponseDTO;
import filippotimo.BookATable.payloads.userDTOs.RegisterRestaurantOwnerDTO;
import filippotimo.BookATable.payloads.userDTOs.RegisterUserDTO;
import filippotimo.BookATable.services.AuthService;
import filippotimo.BookATable.services.GenericUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public class AuthController {

    private final AuthService authService;
    private final GenericUserService userService;

    @Autowired
    public AuthController(AuthService authService, GenericUserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    // 1. ---------- POST /api/auth/login ----------
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        return new LoginResponseDTO(authService.checkCredentialsAndGenerateToken(body));
    }

    // 2. ---------- POST /api/auth/register/user ----------
    @PostMapping("/register/user")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericUser registerUser(@RequestBody @Validated RegisterUserDTO body,
                                    BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        return userService.registerUser(body);
    }

    // 3. ---------- POST /api/auth/register/restaurant-owner ----------
    @PostMapping("/register/restaurant-owner")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericUser registerRestaurantOwner(@RequestBody @Validated RegisterRestaurantOwnerDTO body,
                                               BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        return userService.registerRestaurantOwner(body);
    }

}
