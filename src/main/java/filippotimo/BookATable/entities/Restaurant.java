package filippotimo.BookATable.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import filippotimo.BookATable.entities.enums.RestaurantType;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "restaurants")
@JsonIgnoreProperties({
        "owner"
})
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

    private Integer maxSeats;
    private String description;
    private Integer availableSeatsIndoor;
    private Integer availableSeatsOutdoor;
    private String phone;

    public Restaurant() {
    }

    public Restaurant(GenericUser owner,
                      String city,
                      RestaurantType restaurantType,
                      String description,
                      Integer availableSeatsIndoor,
                      Integer availableSeatsOutdoor,
                      String phone) {
        this.owner = owner;
        this.city = city;
        this.restaurantType = restaurantType;
        this.maxSeats = availableSeatsIndoor + availableSeatsOutdoor;
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

    public Integer getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(Integer maxSeats) {
        this.maxSeats = maxSeats;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAvailableSeatsIndoor() {
        return availableSeatsIndoor;
    }

    public void setAvailableSeatsIndoor(Integer availableSeatsIndoor) {
        this.availableSeatsIndoor = availableSeatsIndoor;
    }

    public Integer getAvailableSeatsOutdoor() {
        return availableSeatsOutdoor;
    }

    public void setAvailableSeatsOutdoor(Integer availableSeatsOutdoor) {
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
