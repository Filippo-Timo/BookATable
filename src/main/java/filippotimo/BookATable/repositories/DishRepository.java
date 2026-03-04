package filippotimo.BookATable.repositories;

import filippotimo.BookATable.entities.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DishRepository extends JpaRepository<Dish, UUID> {

    List<Dish> findByMenuId(UUID menuId);

}
