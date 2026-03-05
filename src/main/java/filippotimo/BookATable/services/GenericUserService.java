package filippotimo.BookATable.services;

import filippotimo.BookATable.entities.GenericUser;
import filippotimo.BookATable.entities.enums.Role;
import filippotimo.BookATable.exceptions.NotFoundException;
import filippotimo.BookATable.payloads.userDTOs.RegisterRestaurantOwnerDTO;
import filippotimo.BookATable.payloads.userDTOs.RegisterUserDTO;
import filippotimo.BookATable.repositories.GenericUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GenericUserService {

    @Autowired
    private GenericUserRepository userRepository;

    @Autowired
    private PasswordEncoder bcrypt;

    // ---------- REGISTRAZIONE USER ----------

    public GenericUser registerUser(RegisterUserDTO body) {

        // 1) Controllo duplicati
        if (userRepository.existsByEmail(body.email()))
            throw new RuntimeException("Email " + body.email() + " already in use!");

        // 2) Creo e salvo l'utente
        GenericUser user = new GenericUser(
                body.email(),
                bcrypt.encode(body.password()),
                body.firstName(),
                body.lastName(),
                Role.USER,
                body.birthDate(),
                body.city()
        );

        user.setAvatar("https://ui-avatars.com/api?name=" + body.firstName() + " " + body.lastName());

        return userRepository.save(user);
    }

    // ---------- REGISTRAZIONE RESTAURANT OWNER ----------

    public GenericUser registerRestaurantOwner(RegisterRestaurantOwnerDTO body) {

        // 1) Controllo duplicati
        if (userRepository.existsByEmail(body.email()))
            throw new RuntimeException("Email " + body.email() + " already in use!");

        // 2) Creo e salvo il ristoratore
        GenericUser owner = new GenericUser(
                body.email(),
                bcrypt.encode(body.password()),
                body.firstName(),
                body.lastName(),
                Role.RESTAURANT_OWNER,
                null,
                null
        );

        return userRepository.save(owner);
    }

    // ---------- FIND ----------

    public GenericUser findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found!"));
    }

    public GenericUser findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " not found!"));
    }

}
