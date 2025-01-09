package kz.odik.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

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
    @ManyToMany
    @JoinTable(name = "users_stores",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "store_id"))
    @JsonIgnoreProperties("users")
    private Set<Store> stores = new HashSet<>();
}