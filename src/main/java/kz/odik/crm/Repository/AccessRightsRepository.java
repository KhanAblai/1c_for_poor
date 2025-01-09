package kz.odik.crm.Repository;

import kz.odik.crm.entity.AccessRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessRightsRepository extends JpaRepository<AccessRights, Long> {
    Optional<AccessRights> findByName(String name);
}
