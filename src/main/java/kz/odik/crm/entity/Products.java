package kz.odik.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @ManyToMany
    @JoinTable(name = "store_inventory", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "store_id"))
    @JsonIgnoreProperties("products")
    private Set<Store> stores;
}
