package kz.odik.crm.entity;

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
@Table(name = "store_inventory")
public class StoreInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Products product;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id")
    private Store store;
    @NotBlank
    private int quantity;
    @NotBlank
    private int retail_price;
    @OneToMany(mappedBy = "storeInventory", cascade = CascadeType.REMOVE)
    private Set<ProductInflow> productInflows;
    @OneToMany(mappedBy = "storeInventory", cascade = CascadeType.REMOVE)
    private Set<ProductOutflow> productOutflows;
}
