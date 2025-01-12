package kz.odik.crm.Repository;

import kz.odik.crm.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    @Query("SELECT u FROM Users u JOIN FETCH u.role WHERE u.username = :username")
    Optional<Users> findByUsername(String username);

//    @Query("SELECT new kz.odik.crm.DTO.UserWithAccessRights(u.username, u.password, (SELECT r.accessRights FROM Users u JOIN u.role r WHERE u.username = :username)) FROM Users u WHERE u.username = :username")
//    Optional<UserWithAccessRights> findByUsernameWithDetails(@Param("username") String username);

    @Query("SELECT u FROM Users u WHERE u.role.id = :roleId")
    List<Users> findByRoleId(@Param("roleId") Long roleId);
}
