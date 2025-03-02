package kz.odik.crm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_activity")
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "checked_in_date", nullable = false)
    private LocalDateTime checkedInDate;

    @Column(name = "checked_out_date")
    private LocalDateTime checkedOutDate;

    @Column(name = "check_out", nullable = false)
    private Boolean checkOut = false;
}
