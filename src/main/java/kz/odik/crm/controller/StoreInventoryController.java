package kz.odik.crm.controller;

import kz.odik.crm.DTO.*;
import kz.odik.crm.Service.StoreInventoryService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/storeinventory")
public class StoreInventoryController {
    private StoreInventoryService storeInventoryService;

    @PreAuthorize("hasAuthority('SELL_STOREINVENTORY_PRODUCTS') or hasAuthority('UPDATE_STOREINVENTORY_PRODUCTS') or hasAuthority('CREATE_STOREINVENTORY_PRODUCTS')")
    @GetMapping("/products-by-name")
    public List<FindProductsByNameDTo> searchProductsByName(@RequestParam String name, @RequestParam Long storeId) {
        return storeInventoryService.findProductsByName(name, storeId);
    }

//    @PreAuthorize("hasAuthority('SELL_STOREINVENTORY_PRODUCTS') or hasAuthority('UPDATE_STOREINVENTORY_PRODUCTS') or hasAuthority('CREATE_STOREINVENTORY_PRODUCTS')")
//    @GetMapping("/products-by-name")
//    public List<FindProductsByNameDTo> searchProductsByID(@RequestParam String name, @RequestParam Long storeId
//    ) {
//        return storeInventoryService.findProductsByName(name, storeId);
//    }

    @PreAuthorize("hasAuthority('SELL_STOREINVENTORY_PRODUCTS')")
    @PostMapping("/sell-product")
    public SellStoreInventoryProductDTO sellProduct(Principal principal, @RequestBody StoreInventoryDTO dto) {
        return storeInventoryService.createProductOutflow(principal, dto);
    }

    @PreAuthorize("hasAuthority('UPDATE_STOREINVENTORY_PRODUCTS')")
    @PostMapping("/update-product")
    public UpdateStoreInventoryProductDTO updateProduct(Principal principal, @RequestBody StoreInventoryDTO dto) {
        return storeInventoryService.updateProduct(principal, dto);
    }

    @PreAuthorize("hasAuthority('CREATE_STOREINVENTORY_PRODUCTS')")
    @PostMapping("/create-product")
    public CreateStoreInventoryCreateProductDTO createProduct(Principal principal, @RequestBody StoreInventoryDTO dto) {
        return storeInventoryService.createProductInflow(principal, dto);
    }
}
