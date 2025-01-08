package kz.odik.crm.Repository;

import kz.odik.crm.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    @Query("SELECT u FROM Users u WHERE u.role.id = :roleId")
    List<Users> findByRoleId(@Param("roleId") Long roleId);
}
