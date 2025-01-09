package kz.odik.crm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_in_flow")
public class ProductInflow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "stores_inventory_id", referencedColumnName = "id")
    private StoreInventory storeInventory;
    @NotBlank
    private int wholesale_price;
    @NotBlank
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
