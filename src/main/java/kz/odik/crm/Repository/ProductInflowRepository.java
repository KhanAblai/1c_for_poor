package kz.odik.crm.Repository;

import kz.odik.crm.entity.ProductInflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInflowRepository extends JpaRepository<ProductInflow, Long> {
}