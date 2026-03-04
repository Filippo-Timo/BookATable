package filippotimo.BookATable.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    private MenuType menuType;


    public Menu() {
    }

    public Menu(Restaurant restaurant,
                MenuType menuType) {
        this.restaurant = restaurant;
        this.menuType = menuType;
    }


    public UUID getId() {
        return id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }

    @Override
    public String toString() {
        return "Menu { " +
                "id = " + id +
                ", restaurant = " + restaurant +
                ", menuType = " + menuType +
                " " +
                '}';
    }
}
