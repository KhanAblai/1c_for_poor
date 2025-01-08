package kz.odik.crm.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users_stores")
public class UsersStores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
