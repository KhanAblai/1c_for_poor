package kz.odik.crm.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "role_access_rights")
public class RoleAccessRights {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Roles role_id;
    @ManyToOne
    @JoinColumn(name = "access_right_id")
    private AccessRights access_id;
}
