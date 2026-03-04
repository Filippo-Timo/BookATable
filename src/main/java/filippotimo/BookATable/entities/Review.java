package filippotimo.BookATable.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private GenericUser user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private Integer rating;
    private String comment;


    public Review() {
    }

    public Review(GenericUser user,
                  Restaurant restaurant,
                  Integer rating,
                  String comment) {
        this.user = user;
        this.restaurant = restaurant;
        this.rating = rating;
        this.comment = comment;
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    @Override
    public String toString() {
        return "Review { " +
                "id = " + id +
                ", user = " + user +
                ", restaurant = " + restaurant +
                ", rating = " + rating +
                ", comment = " + comment +
                " " +
                '}';
    }
}
