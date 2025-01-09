package kz.odik.crm.Repository;

import kz.odik.crm.entity.AccessRights;
import kz.odik.crm.entity.RoleAccessRights;
import kz.odik.crm.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleAccessRightsRepository extends JpaRepository<RoleAccessRights, Long> {
    @Query("SELECT ra.accessRight FROM RoleAccessRights ra WHERE ra.role = :role")
    List<AccessRights> findAccessRightsByRole(@Param("role") Roles role);

}
