package kz.odik.crm.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStoreInventoryCreateProductDTO {
    private String product_name;
    private Long retail_price;
    private Long quantity;
    private Long store_id;
}
