package filippotimo.BookATable.services;

import filippotimo.BookATable.entities.GenericUser;
import filippotimo.BookATable.exceptions.NotFoundException;
import filippotimo.BookATable.repositories.GenericUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GenericUserService {

    @Autowired
    private GenericUserRepository userRepository;

    // Cerca utente per ID — usato nel JWTCheckerFilter
    public GenericUser findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found!"));
    }

    // Cerca utente per email — usato nel AuthService
    public GenericUser findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " not found!"));
    }

}
