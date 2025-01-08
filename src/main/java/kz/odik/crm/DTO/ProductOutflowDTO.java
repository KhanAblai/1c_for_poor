package kz.odik.crm.DTO;

import kz.odik.crm.entity.StoreInventory;
import kz.odik.crm.entity.Users;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
@Data
public class ProductOutflowDTO {

    private StoreInventory storeInventory;

    private int quantity;

    private Users user;
}
