package kz.odik.crm.DTO;

import lombok.Data;

@Data

public class StoreInventoryDTO {
    private Long product_id;
    private String product_name;
    private Long retail_price;
    private Long quantity;
    private Long store_id;

}
