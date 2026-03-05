package filippotimo.BookATable.services;

import filippotimo.BookATable.entities.GenericUser;
import filippotimo.BookATable.exceptions.UnauthorizedException;
import filippotimo.BookATable.payloads.userDTOs.LoginDTO;
import filippotimo.BookATable.repositories.GenericUserRepository;
import filippotimo.BookATable.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final GenericUserService userService;
    private final GenericUserRepository userRepository;
    private final JWTTools jwtTools;
    private final PasswordEncoder bcrypt;

    @Autowired
    public AuthService(GenericUserService userService,
                       GenericUserRepository userRepository,
                       JWTTools jwtTools,
                       PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtTools = jwtTools;
        this.bcrypt = passwordEncoder;
    }

    // ---------- LOGIN ----------

    public String checkCredentialsAndGenerateToken(LoginDTO body) {

        // 1) Cerco l'utente per email
        GenericUser found = userService.findByEmail(body.email());

        // 2) Controllo la password
        if (bcrypt.matches(body.password(), found.getPassword())) {

            // 3) Genero e ritorno il token
            return jwtTools.generateToken(found);
        } else {
            throw new UnauthorizedException("Le credenziali inserite sono errate!");
        }
    }


}
