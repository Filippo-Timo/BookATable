package filippotimo.BookATable.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import filippotimo.BookATable.entities.enums.RestaurantType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
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

    private String name;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private GenericUser owner;

    private String city;

    private String address;

    @Enumerated(EnumType.STRING)
    private RestaurantType restaurantType;

    private Integer maxSeats;
    private String description;
    private Integer availableSeatsIndoor;
    private Integer availableSeatsOutdoor;
    private String phone;

    // Queste tre relazioni qui sotto servono per fare in modo che eliminando un ristorante
    // vengano eliminati anche i menù, le recensioni e le prenotazioni collegati a quel ristorante

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();


    public Restaurant() {
    }

    public Restaurant(GenericUser owner,
                      String name,
                      String imageUrl,
                      String city,
                      String address,
                      RestaurantType restaurantType,
                      String description,
                      Integer availableSeatsIndoor,
                      Integer availableSeatsOutdoor,
                      String phone) {
        this.owner = owner;
        this.name = name;
        this.imageUrl = imageUrl;
        this.city = city;
        this.address = address;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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


//    @Override
//    public String toString() {
//        return "Restaurant { " +
//                "id = " + id +
//                ", owner = " + owner +
//                ", city = " + city + '\'' +
//                ", restaurantType = " + restaurantType +
//                ", maxSeats = " + maxSeats +
//                ", description = " + description + '\'' +
//                ", availableSeatsIndoor = " + availableSeatsIndoor +
//                ", availableSeatsOutdoor = " + availableSeatsOutdoor +
//                ", phone = " + phone +
//                " " +
//                '}';
//    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id = " + id +
                ", name = " + name + '\'' +
                ", imageUrl = " + imageUrl + '\'' +
                ", owner = " + owner +
                ", city = " + city + '\'' +
                ", address = " + address + '\'' +
                ", restaurantType = " + restaurantType +
                ", maxSeats = " + maxSeats +
                ", description = " + description + '\'' +
                ", availableSeatsIndoor = " + availableSeatsIndoor +
                ", availableSeatsOutdoor = " + availableSeatsOutdoor +
                ", phone = " + phone + '\'' +
                ", menus = " + menus +
                ", reviews = " + reviews +
                ", reservations = " + reservations +
                '}';
    }
}
