package kz.odik.crm.Repository;

import kz.odik.crm.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    @Query("SELECT p FROM Products p WHERE p.name LIKE %:name%")
    List<Products> findByNameContaining(@Param("name") String name);

    Optional<Products> findByName(String name);
}
