package kz.odik.crm.controller;

import kz.odik.crm.DTO.CreateStoreResponseDTO;
import kz.odik.crm.DTO.GetAllStoresDTO;
import kz.odik.crm.DTO.StoreDTO;
import kz.odik.crm.Repository.StoreRepository;
import kz.odik.crm.Service.StoreService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/store")
public class StoreContoller {
    private StoreService storeService;
    private StoreRepository storeRepository;

    @PreAuthorize("hasAuthority('UPDATE_STORE') or hasAuthority('CREATE_STORE')")
    @GetMapping("/all")
    public List<GetAllStoresDTO> getAllUsers() {
        return storeService.getAllStores();
    }

    @GetMapping("/all-by-user/{id}")
    public List<GetAllStoresDTO> getAllStoresByUser(@PathVariable("id") Long userId) {
        return storeService.getAllStoresToUser(userId);
    }

    @PreAuthorize("hasAuthority('CREATE_STORE')")
    @PostMapping("/create")
    public CreateStoreResponseDTO createStore(@RequestBody StoreDTO dto) {
        return storeService.create(dto);
    }

    @PreAuthorize("hasAuthority('UPDATE_STORE')")
    @PostMapping("/update/{id}")
    public CreateStoreResponseDTO updateStore(@PathVariable("id") Long storeId, @RequestBody StoreDTO dto) {
        System.out.println("ZSDASDASD");
        return storeService.updateStore(storeId, dto);
    }
}
