package filippotimo.BookATable.entities;

import filippotimo.BookATable.entities.enums.SeatingPreference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private GenericUser user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private LocalDate date;

    private LocalTime time;

    private Integer seatsBooked;

    private SeatingPreference seatingPreference;


    public Reservation() {
    }

    public Reservation(GenericUser user,
                       Restaurant restaurant,
                       LocalDate date,
                       LocalTime time,
                       Integer seatsBooked,
                       SeatingPreference seatingPreference) {
        this.user = user;
        this.restaurant = restaurant;
        this.date = date;
        this.time = time;
        this.seatsBooked = seatsBooked;
        this.seatingPreference = seatingPreference;
    }


    public UUID getId() {
        return id;
    }

    public GenericUser getUser() {
        return user;
    }

    public void setUser(GenericUser user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Integer getSeatsBooked() {
        return seatsBooked;
    }

    public void setSeatsBooked(Integer seatsBooked) {
        this.seatsBooked = seatsBooked;
    }

    public SeatingPreference getSeatingPreference() {
        return seatingPreference;
    }

    public void setSeatingPreference(SeatingPreference seatingPreference) {
        this.seatingPreference = seatingPreference;
    }


    @Override
    public String toString() {
        return "Reservation { " +
                "id = " + id +
                ", user = " + user +
                ", restaurant = " + restaurant +
                ", date = " + date +
                ", time = " + time +
                ", seatsBooked = " + seatsBooked +
                ", seatingPreference = " + seatingPreference +
                " " +
                '}';
    }
}
