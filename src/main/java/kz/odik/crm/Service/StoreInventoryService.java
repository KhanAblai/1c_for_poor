package kz.odik.crm.Service;

import kz.odik.crm.DTO.StoreInventoryDTO;
import kz.odik.crm.Repository.*;
import kz.odik.crm.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreInventoryService {

    @Autowired
    private ProductOutflowRepository productOutflowRepository;
    @Autowired
    private ProductInflowRepository productInflowRepository;
    @Autowired
    private ProductsRepository productRepository;
    @Autowired
    private StoreInventoryRepository storeInventoryRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private UsersRepository usersRepository;

    public List<Products> findProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }

    public StoreInventory updateProduct(Long storeInventoryId, StoreInventoryDTO dto) {
        StoreInventory storeInventory = storeInventoryRepository.findById(storeInventoryId).orElseThrow(() -> new RuntimeException("StoreInventory not found: " + storeInventoryId));
        if (dto.getProduct_name() != null) {
            Products product = productRepository.findByName(dto.getProduct_name()).orElseThrow(() -> new RuntimeException("Product not found:" + dto.getProduct_name()));

        }
        if (dto.getStore_id() != null) {
            Store store = storeRepository.findById(dto.getStore_id()).orElseThrow(() -> new RuntimeException("Store not found" + dto.getStore_id()));
            storeInventory.setStore(store);
        }
        if (dto.getQuantity() != null) {
            storeInventory.setQuantity(Math.toIntExact(dto.getQuantity()));
        }
        if (dto.getRetail_price() != null) {
            storeInventory.setRetail_price(Math.toIntExact(dto.getRetail_price()));
        }
        return storeInventoryRepository.save(storeInventory);
    }

    public StoreInventory createProductOutflow(Long userId, StoreInventoryDTO dto) {
        System.out.println("AUTHORIZATION PASSED");
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found:" + userId));

        StoreInventory storeInventory = storeInventoryRepository.findById(dto.getStore_id())
                .orElseThrow(() -> new RuntimeException("Store not found: " + dto.getStore_id()));

        int currentQuantity = storeInventory.getQuantity();
        Long quantityToReduce = dto.getQuantity();
        if (quantityToReduce > currentQuantity) {
            throw new RuntimeException("Not enough stock");
        }
        storeInventory.setQuantity(Math.toIntExact(currentQuantity - quantityToReduce));

        ProductOutflow outflow = new ProductOutflow();
        outflow.setUser(user);
        outflow.setStoreInventory(storeInventory);
        outflow.setQuantity(Math.toIntExact(quantityToReduce));


        storeInventoryRepository.save(storeInventory);
        productOutflowRepository.save(outflow);

        return storeInventory;
    }


    public StoreInventory createProductInflow(Long userId, StoreInventoryDTO dto) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        StoreInventory storeInventory = storeInventoryRepository.findById(dto.getStore_id())
                .orElseGet(() -> {
                    StoreInventory newInventory = new StoreInventory();
                    newInventory.setStore(storeRepository.findById(dto.getStore_id())
                            .orElseThrow(() -> new RuntimeException("Store not found: " + dto.getStore_id())));
                    newInventory.setProduct(productRepository.findByName(dto.getProduct_name())
                            .orElseGet(() -> {
                                Products newProduct = new Products();
                                newProduct.setName(dto.getProduct_name());
                                productRepository.save(newProduct);
                                return newProduct;
                            }));
                    newInventory.setQuantity(0);
                    newInventory.setRetail_price(Math.toIntExact(dto.getRetail_price()));
                    storeInventoryRepository.save(newInventory);
                    return newInventory;
                });

        int currentQuantity = storeInventory.getQuantity();
        Long quantityToAdd = dto.getQuantity();
        storeInventory.setQuantity(Math.toIntExact(currentQuantity + quantityToAdd));

        ProductInflow inflow = new ProductInflow();
        inflow.setUser(user);
        inflow.setStoreInventory(storeInventory);
        inflow.setQuantity(Math.toIntExact(quantityToAdd));
        inflow.setWholesale_price(Math.toIntExact(dto.getRetail_price()));

        storeInventoryRepository.save(storeInventory);
        productInflowRepository.save(inflow);

        return storeInventory;
    }


}
