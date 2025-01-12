package kz.odik.crm.Repository;

import kz.odik.crm.entity.AccessRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccessRightsRepository extends JpaRepository<AccessRights, Long> {
    @Query("SELECT new kz.odik.crm.entity.AccessRights(r.id, r.name, r.description) FROM AccessRights r WHERE r.name IN :names")
    List<AccessRights> findByNames(@Param("names") String[] names);

    Optional<AccessRights> findByName(String name);
}
