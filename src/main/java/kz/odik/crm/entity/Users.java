package kz.odik.crm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Логин не должен быть пустым")
    private String username;
    @NotBlank(message = "Пароль не должен быть пустым")
    private String password;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Roles role;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}