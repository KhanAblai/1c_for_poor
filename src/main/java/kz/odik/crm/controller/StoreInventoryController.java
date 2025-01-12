package kz.odik.crm.controller;

import kz.odik.crm.DTO.CreateStoreInventoryCreateProductDTO;
import kz.odik.crm.DTO.SellStoreInventoryProductDTO;
import kz.odik.crm.DTO.StoreInventoryDTO;
import kz.odik.crm.DTO.UpdateStoreInventoryProductDTO;
import kz.odik.crm.Service.StoreInventoryService;
import kz.odik.crm.entity.Products;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/storeinventory")
public class StoreInventoryController {
    private StoreInventoryService storeInventoryService;

    @PreAuthorize("hasAuthority('SELL_STOREINVENTORY_PRODUCTS') or hasAuthority('UPDATE_STOREINVENTORY_PRODUCTS') or hasAuthority('CREATE_STOREINVENTORY_PRODUCTS')")
    @GetMapping("/products-by-name")
    public List<Products> searchProductsByName(@RequestParam String name) {
        return storeInventoryService.findProductsByName(name);
    }

    @PreAuthorize("hasAuthority('SELL_STOREINVENTORY_PRODUCTS')")
    @PostMapping("/sell-product/{id}")
    public SellStoreInventoryProductDTO sellProduct(@PathVariable Long id, @RequestBody StoreInventoryDTO dto) {
        return storeInventoryService.createProductOutflow(id, dto);
    }

    @PreAuthorize("hasAuthority('UPDATE_STOREINVENTORY_PRODUCTS')")
    @PostMapping("/update-product/{id}")
    public UpdateStoreInventoryProductDTO updateProduct(@PathVariable Long id, @RequestBody StoreInventoryDTO dto) {
        return storeInventoryService.updateProduct(id, dto);
    }

    @PreAuthorize("hasAuthority('CREATE_STOREINVENTORY_PRODUCTS')")
    @PostMapping("/create-product/{userid}")
    public CreateStoreInventoryCreateProductDTO createProduct(@PathVariable Long userid, @RequestBody StoreInventoryDTO dto) {
        return storeInventoryService.createProductInflow(userid, dto);
    }
}
