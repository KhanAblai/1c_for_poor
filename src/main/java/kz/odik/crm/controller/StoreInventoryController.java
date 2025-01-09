package kz.odik.crm.controller;

import kz.odik.crm.DTO.StoreInventoryDTO;
import kz.odik.crm.Service.StoreInventoryService;
import kz.odik.crm.entity.Products;
import kz.odik.crm.entity.StoreInventory;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/storeinventory")
public class StoreInventoryController {
    private StoreInventoryService storeInventoryService;

    @GetMapping("/products-by-name")
    public List<Products> searchProductsByName(@RequestParam String name) {
        return storeInventoryService.findProductsByName(name);
    }

    @PreAuthorize("hasAuthority('SELL_STOREINVENTORY_PRODUCT')")
    @PostMapping("/sell-product/{id}")
    public StoreInventory sellProduct(@PathVariable Long id, @RequestBody StoreInventoryDTO dto) {
        return storeInventoryService.createProductOutflow(id, dto);
    }

    @PreAuthorize("hasAuthority('UPDATE_STOREINVENTORY_PRODUCT')")
    @PostMapping("/update-product/{id}")
    public StoreInventory updateProduct(@PathVariable Long id, @RequestBody StoreInventoryDTO dto) {
        return storeInventoryService.updateProduct(id, dto);
    }

    @PreAuthorize("hasAuthority('CREATE_STOREINVENTORY_PRODUCT')")
    @PostMapping("/create-product")
    public StoreInventory createProduct(@RequestParam Long userid, @RequestBody StoreInventoryDTO dto) {
        return storeInventoryService.createProductInflow(userid, dto);
    }
}
