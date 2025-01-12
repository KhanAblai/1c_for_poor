package kz.odik.crm.Repository;

import kz.odik.crm.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query("SELECT s FROM Store s JOIN s.users u WHERE u.id = :userId")
    List<Store> findAllStoresByUserId(@Param("userId") Long userId);

    Optional<Store> findByName(String storeName);
}
