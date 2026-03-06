package filippotimo.BookATable.services;

import filippotimo.BookATable.entities.GenericUser;
import filippotimo.BookATable.entities.Reservation;
import filippotimo.BookATable.entities.Restaurant;
import filippotimo.BookATable.entities.enums.SeatingPreference;
import filippotimo.BookATable.exceptions.NotFoundException;
import filippotimo.BookATable.exceptions.UnauthorizedException;
import filippotimo.BookATable.payloads.reservationDTOs.CreateReservationDTO;
import filippotimo.BookATable.payloads.reservationDTOs.UpdateReservationDTO;
import filippotimo.BookATable.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantService restaurantService;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, RestaurantService restaurantService) {
        this.reservationRepository = reservationRepository;
        this.restaurantService = restaurantService;
    }

    // ---------- CREATE ----------

    public Reservation create(CreateReservationDTO body, GenericUser currentUser) {

        // 1) Controllo che la data della prenotazione non sia al passato
        if (body.date().isBefore(LocalDate.now()))
            throw new RuntimeException("Date must be today or in the future!");

        // 2) Controllo che il ristorante esista
        Restaurant restaurant = restaurantService.findById(body.restaurantId());

        // Controllo che l'utente non abbia già una prenotazione nello stesso ristorante, stessa data e stessa ora
        boolean alreadyBooked = reservationRepository
                .findByRestaurantIdAndDate(restaurant.getId(), body.date())
                .stream()
                .anyMatch(r -> r.getUser().getId().equals(currentUser.getId()));

        if (alreadyBooked)
            throw new RuntimeException("You already have a reservation at this restaurant on this date!");

        // 3) Calcolo i posti già prenotati per quel giorno e quella preferenza
        List<Reservation> reservationsToday = reservationRepository
                .findByRestaurantIdAndDate(restaurant.getId(), body.date());

        int seatsBookedToday = reservationsToday.stream()
                .filter(reservation -> reservation.getSeatingPreference() == body.seatingPreference())
                .mapToInt(Reservation::getSeatsBooked)
                .sum();

        // 4) Controllo disponibilità posti
        int availableSeats = body.seatingPreference() == SeatingPreference.INDOOR ?
                restaurant.getAvailableSeatsIndoor() : restaurant.getAvailableSeatsOutdoor();

        if (seatsBookedToday + body.seatsBooked() > availableSeats)
            throw new RuntimeException("Not enough "
                    + body.seatingPreference().name().toLowerCase()
                    + " seats available for this date!");

        // 5) Creo e salvo la prenotazione
        Reservation reservation = new Reservation(
                currentUser,
                restaurant,
                body.date(),
                body.time(),
                body.seatsBooked(),
                body.seatingPreference()
        );

        return reservationRepository.save(reservation);
    }

    // ---------- READ ----------

    public List<Reservation> findByUser(GenericUser currentUser) {
        return reservationRepository.findByUserId(currentUser.getId());
    }

    public List<Reservation> findByRestaurant(UUID restaurantId) {
        restaurantService.findById(restaurantId);
        return reservationRepository.findByRestaurantId(restaurantId);
    }

    public Reservation findById(UUID id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation with id " + id + " not found!"));
    }

    // ---------- UPDATE ----------

    public Reservation update(UUID id, UpdateReservationDTO body, GenericUser currentUser) {

        Reservation reservation = findById(id);

        // Controllo che la prenotazione appartenga all'utente loggato
        if (!reservation.getUser().getId().equals(currentUser.getId()))
            throw new UnauthorizedException("You are not the owner of this reservation!");

        // Controllo che la data inserita non sia al passato
        if (body.date().isBefore(LocalDate.now()))
            throw new RuntimeException("Date must be today or in the future!");

        // Ricalcolo disponibilità escludendo la prenotazione corrente
        Restaurant restaurant = reservation.getRestaurant();

        // Controllo duplicati — escludo la prenotazione corrente
        boolean alreadyBooked = reservationRepository
                .findByRestaurantIdAndDate(restaurant.getId(), body.date())
                .stream()
                .anyMatch(r -> r.getUser().getId().equals(currentUser.getId())
                        && !r.getId().equals(id));  // ← escludo la prenotazione corrente

        if (alreadyBooked)
            throw new RuntimeException("You already have a reservation at this restaurant on this date!");

        List<Reservation> reservationsToday = reservationRepository
                .findByRestaurantIdAndDate(restaurant.getId(), body.date());

        int seatsBookedToday = reservationsToday.stream()
                .filter(r -> r.getSeatingPreference() == body.seatingPreference())
                .filter(r -> !r.getId().equals(id)) // ← escludo la prenotazione corrente
                .mapToInt(Reservation::getSeatsBooked)
                .sum();

        int availableSeats = body.seatingPreference() == SeatingPreference.INDOOR
                ? restaurant.getAvailableSeatsIndoor()
                : restaurant.getAvailableSeatsOutdoor();

        if (seatsBookedToday + body.seatsBooked() > availableSeats)
            throw new RuntimeException("Not enough "
                    + body.seatingPreference().name().toLowerCase()
                    + " seats available for this date!");

        reservation.setDate(body.date());
        reservation.setTime(body.time());
        reservation.setSeatsBooked(body.seatsBooked());
        reservation.setSeatingPreference(body.seatingPreference());

        return reservationRepository.save(reservation);
    }

    // ---------- DELETE ----------

    public void delete(UUID id, GenericUser currentUser) {

        Reservation reservation = findById(id);

        // Controllo che la prenotazione appartenga all'utente loggato
        if (!reservation.getUser().getId().equals(currentUser.getId()))
            throw new UnauthorizedException("You are not the owner of this reservation!");

        reservationRepository.deleteById(id);
    }

}
