package kz.odik.crm.Repository;

import kz.odik.crm.entity.Store;
import kz.odik.crm.entity.UsersStores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsersStoresRepository extends JpaRepository<UsersStores, Long> {

    @Modifying
    @Query("DELETE FROM UsersStores us WHERE us.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Query("SELECT us.store FROM UsersStores us WHERE us.user.id = :userId")
    List<Store> findStoresByUserId(@Param("userId") Long userId);
}
