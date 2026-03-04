package filippotimo.BookATable.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "dishes")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private String name;

    private String ingredients;

    private Double price;


    public Dish() {
    }

    public Dish(Menu menu,
                String name,
                String ingredients,
                Double price) {
        this.menu = menu;
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
    }


    public UUID getId() {
        return id;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


    @Override
    public String toString() {
        return "Dish { " +
                "id = " + id +
                ", menu = " + menu +
                ", name = " + name + '\'' +
                ", ingredients = " + ingredients + '\'' +
                ", price = " + price +
                " " +
                '}';
    }
}
