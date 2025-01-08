package kz.odik.crm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NotBlank
    private String name;
    @ManyToMany
    @JoinTable(name = "role_access_rights", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "access_right_id"))
    private Set<AccessRights> accessRights = new HashSet<>();
    @ManyToMany(mappedBy = "role")
    private Set<Users> users = new HashSet<>();
}
