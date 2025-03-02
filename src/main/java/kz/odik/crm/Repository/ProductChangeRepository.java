package kz.odik.crm.Repository;

import kz.odik.crm.entity.ProductChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductChangeRepository extends JpaRepository<ProductChange, Long> {
}
