package kz.odik.crm.Repository;

import kz.odik.crm.entity.Roles;
import kz.odik.crm.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles,Long> {

}
