package filippotimo.BookATable.entities;

import filippotimo.BookATable.entities.enums.RestaurantType;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private GenericUser owner;

    private String city;

    @Enumerated(EnumType.STRING)
    private RestaurantType restaurantType;

    private Long maxSeats;
    private String description;
    private Long availableSeatsIndoor;
    private Long availableSeatsOutdoor;
    private String phone;

    public Restaurant() {
    }

    public Restaurant(GenericUser owner,
                      String city,
                      RestaurantType restaurantType,
                      Long maxSeats,
                      String description,
                      Long availableSeatsIndoor,
                      Long availableSeatsOutdoor,
                      String phone) {
        this.owner = owner;
        this.city = city;
        this.restaurantType = restaurantType;
        this.maxSeats = maxSeats;
        this.description = description;
        this.availableSeatsIndoor = availableSeatsIndoor;
        this.availableSeatsOutdoor = availableSeatsOutdoor;
        this.phone = phone;
    }

    public UUID getId() {
        return id;
    }

    public GenericUser getOwner() {
        return owner;
    }

    public void setOwner(GenericUser owner) {
        this.owner = owner;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public RestaurantType getRestaurantType() {
        return restaurantType;
    }

    public void setRestaurantType(RestaurantType restaurantType) {
        this.restaurantType = restaurantType;
    }

    public Long getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(Long maxSeats) {
        this.maxSeats = maxSeats;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAvailableSeatsIndoor() {
        return availableSeatsIndoor;
    }

    public void setAvailableSeatsIndoor(Long availableSeatsIndoor) {
        this.availableSeatsIndoor = availableSeatsIndoor;
    }

    public Long getAvailableSeatsOutdoor() {
        return availableSeatsOutdoor;
    }

    public void setAvailableSeatsOutdoor(Long availableSeatsOutdoor) {
        this.availableSeatsOutdoor = availableSeatsOutdoor;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Override
    public String toString() {
        return "Restaurant { " +
                "id = " + id +
                ", owner = " + owner +
                ", city = " + city + '\'' +
                ", restaurantType = " + restaurantType +
                ", maxSeats = " + maxSeats +
                ", description = " + description + '\'' +
                ", availableSeatsIndoor = " + availableSeatsIndoor +
                ", availableSeatsOutdoor = " + availableSeatsOutdoor +
                ", phone = " + phone +
                " " +
                '}';
    }
}
