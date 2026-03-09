package filippotimo.BookATable.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import filippotimo.BookATable.entities.GenericUser;
import filippotimo.BookATable.entities.enums.Role;
import filippotimo.BookATable.exceptions.BadRequestException;
import filippotimo.BookATable.exceptions.NotEmptyException;
import filippotimo.BookATable.exceptions.NotFoundException;
import filippotimo.BookATable.payloads.userDTOs.RegisterRestaurantOwnerDTO;
import filippotimo.BookATable.payloads.userDTOs.RegisterUserDTO;
import filippotimo.BookATable.payloads.userDTOs.UpdateUserDTO;
import filippotimo.BookATable.repositories.GenericUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class GenericUserService {

    private final Cloudinary cloudinaryUploader;
    private GenericUserRepository userRepository;
    private PasswordEncoder bcrypt;

    @Autowired
    public GenericUserService(GenericUserRepository userRepository, PasswordEncoder bcrypt, Cloudinary cloudinaryUploader) {
        this.userRepository = userRepository;
        this.bcrypt = bcrypt;
        this.cloudinaryUploader = cloudinaryUploader;
    }

    // ---------- REGISTRAZIONE USER ----------

    public GenericUser registerUser(RegisterUserDTO body) {

        // 1) Controllo duplicati
        if (userRepository.existsByEmail(body.email()))
            throw new BadRequestException("Email " + body.email() + " already in use!");

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
            throw new BadRequestException("Email " + body.email() + " already in use!");

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


    // ---------- UPDATE ----------

    public GenericUser update(UUID id, UpdateUserDTO body) {

        GenericUser user = findById(id);

        user.setFirstName(body.firstName());
        user.setLastName(body.lastName());
        user.setCity(body.city());
        user.setBirthDate(body.birthDate());

        return userRepository.save(user);
    }

// ---------- DELETE ----------

    public void delete(UUID id) {
        findById(id);
        userRepository.deleteById(id);
    }


    // ---------- CLOUDINARY ----------

    public GenericUser findByIdAndUploadAvatar(UUID userId, MultipartFile file) {

        if (file.isEmpty()) throw new NotEmptyException();

        GenericUser found = this.findById(userId);

        try {
            Map result = cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            String imageUrl = (String) result.get("secure_url");

            found.setAvatar(imageUrl);

            return userRepository.save(found);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
