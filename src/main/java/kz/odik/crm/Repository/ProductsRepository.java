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
    @Query("SELECT p FROM Products p JOIN p.stores s WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND s.id = :storeId")
    List<Products> findByNameAndStoreId(@Param("name") String name, @Param("storeId") Long storeId);


    Optional<Products> findByName(String name);
}
