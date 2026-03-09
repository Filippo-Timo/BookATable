package filippotimo.BookATable.controllers;

import filippotimo.BookATable.entities.GenericUser;
import filippotimo.BookATable.exceptions.ValidationException;
import filippotimo.BookATable.payloads.userDTOs.UpdateUserDTO;
import filippotimo.BookATable.services.GenericUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class GenericUserController {


    private final GenericUserService genericUserService;

    public GenericUserController(GenericUserService genericUserService) {
        this.genericUserService = genericUserService;
    }


    // 1. ---------- GET /api/users/me ----------

    @GetMapping("/me")
    public GenericUser getMe(@AuthenticationPrincipal GenericUser currentUser) {
        return currentUser;
    }


    // 2. ---------- PUT /api/users/me ----------

    @PutMapping("/me")
    public GenericUser update(@RequestBody @Validated UpdateUserDTO body,
                              BindingResult validationResult,
                              @AuthenticationPrincipal GenericUser currentUser) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        return genericUserService.update(currentUser.getId(), body);
    }


    // 3. ---------- PATCH /api/users/me/avatar ----------
    // consumes = MediaType. ... ---> serve per permettere di caricare un file su Swagger (in questo caso un'immagine per l'avatar)

    @PatchMapping(value = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public GenericUser uploadAvatar(@RequestParam("avatar") MultipartFile file,
                                    @AuthenticationPrincipal GenericUser currentUser) {
        return genericUserService.findByIdAndUploadAvatar(currentUser.getId(), file);
    }


    // 4. ---------- DELETE /api/users/me ----------

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal GenericUser currentUser) {
        genericUserService.delete(currentUser.getId());
    }
}
