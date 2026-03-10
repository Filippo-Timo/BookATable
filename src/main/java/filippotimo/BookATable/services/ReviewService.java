package filippotimo.BookATable.services;

import filippotimo.BookATable.entities.GenericUser;
import filippotimo.BookATable.entities.Restaurant;
import filippotimo.BookATable.entities.Review;
import filippotimo.BookATable.exceptions.BadRequestException;
import filippotimo.BookATable.exceptions.NotFoundException;
import filippotimo.BookATable.exceptions.UnauthorizedException;
import filippotimo.BookATable.payloads.reviewDTOs.CreateReviewDTO;
import filippotimo.BookATable.payloads.reviewDTOs.UpdateReviewDTO;
import filippotimo.BookATable.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantService restaurantService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, RestaurantService restaurantService) {
        this.reviewRepository = reviewRepository;
        this.restaurantService = restaurantService;
    }

    // ---------- CREATE ----------

    public Review create(UUID restaurantId, CreateReviewDTO body, GenericUser currentUser) {

        // 1) Controllo che il ristorante esista
        Restaurant restaurant = restaurantService.findById(restaurantId);

        // 2) Controllo che l'utente non abbia già recensito questo ristorante
        if (reviewRepository.existsByUserIdAndRestaurantId(currentUser.getId(), restaurantId))
            throw new BadRequestException("You have already reviewed this restaurant!");

        // 3) Creo e salvo la recensione
        Review review = new Review(
                currentUser,
                restaurant,
                body.rating(),
                body.comment()
        );

        return reviewRepository.save(review);
    }

    // ---------- READ ----------

    public List<Review> findByRestaurant(UUID restaurantId) {

        // Controllo che il ristorante esista
        restaurantService.findById(restaurantId);
        return reviewRepository.findByRestaurantId(restaurantId);
    }

    public Review findById(UUID id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review with id " + id + " not found!"));
    }

    // ---------- UPDATE ----------

    public Review update(UUID id, UpdateReviewDTO body, GenericUser currentUser) {

        Review review = findById(id);

        // Controllo che la recensione appartenga all'utente loggato
        if (!review.getUser().getId().equals(currentUser.getId()))
            throw new UnauthorizedException("You are not the author of this review!");

        review.setRating(body.rating());
        review.setComment(body.comment());

        return reviewRepository.save(review);
    }

    // ---------- DELETE ----------

    public void delete(UUID id, GenericUser currentUser) {

        Review review = findById(id);

        // Controllo che la recensione appartenga all'utente loggato
        if (!review.getUser().getId().equals(currentUser.getId()))
            throw new UnauthorizedException("You are not the author of this review!");

        reviewRepository.deleteById(id);
    }

}
