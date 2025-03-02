package kz.odik.crm.Repository;

import kz.odik.crm.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    // Найти последнюю активность пользователя, где checkOut = false
    Optional<UserActivity> findFirstByUserIdAndCheckOutFalseOrderByCheckedInDateDesc(Long userId);

    // Найти все активности пользователя
    List<UserActivity> findByUserId(Long userId);
}