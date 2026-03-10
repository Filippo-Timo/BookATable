package filippotimo.BookATable.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import filippotimo.BookATable.entities.enums.MenuType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "menus")
@JsonIgnoreProperties({
        "restaurant"
})
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    private MenuType menuType;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("menu")
    private List<Dish> dishes = new ArrayList<>();


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

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
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
