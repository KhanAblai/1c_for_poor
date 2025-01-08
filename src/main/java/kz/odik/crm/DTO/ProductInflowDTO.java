package kz.odik.crm.DTO;

import lombok.Data;

@Data
public class ProductInflowDTO {
    private Long storeInventory_id;
    private int wholesale_price;
    private int quantity;
    private Long user_id;
}
