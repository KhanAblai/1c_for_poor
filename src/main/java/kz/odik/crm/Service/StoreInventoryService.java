package kz.odik.crm.Service;

import kz.odik.crm.DTO.*;
import kz.odik.crm.Repository.*;
import kz.odik.crm.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoreInventoryService {
    @Autowired
    private ProductChangeRepository productChangeRepository;
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

    public List<FindProductsByNameDTo> findProductsByName(String name, Long storeId) {
        List<Products> products = productRepository.findByNameAndStoreId(name, storeId);
        System.out.println(name);
        System.out.println(storeId);
        System.out.println("!@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        List<FindProductsByNameDTo> findProductsByNameDTos = new ArrayList<>();
        for (Products products1 : products) {
            findProductsByNameDTos.add(new FindProductsByNameDTo(products1.getId(), products1.getName()));
        }
        return findProductsByNameDTos;
    }

    public UpdateStoreInventoryProductDTO updateProduct(Principal principal, StoreInventoryDTO dto) {
        Users user = usersRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found: " + principal.getName()));
        StoreInventory storeInventory = storeInventoryRepository.findByStoreIdAndProductId(dto.getStore_id(), dto.getProduct_id()).orElseThrow(() -> new RuntimeException("StoreInventory not found: " + dto.getStore_id()));
        System.out.println(dto);
        if (dto.getProduct_name() != null) {
            Products product = productRepository.findById(dto.getProduct_id()).orElseThrow(() -> new RuntimeException("Product not found:" + dto.getProduct_name()));
            product.setName(dto.getProduct_name());
            storeInventory.setProduct(product);
            productRepository.save(product);
        }

        if (dto.getQuantity() != null) {
            storeInventory.setQuantity(Math.toIntExact(dto.getQuantity()));
        }
        if (dto.getRetail_price() != null) {
            storeInventory.setRetail_price(Math.toIntExact(dto.getRetail_price()));
        }
        ProductChange outflow = new ProductChange();
        outflow.setUser(user);
        outflow.setStoreInventory(storeInventory);
        outflow.setQuantity(Math.toIntExact(dto.getQuantity()));
        storeInventoryRepository.save(storeInventory);
        productChangeRepository.save(outflow);
        UpdateStoreInventoryProductDTO createStoreInventoryCreateProductDTO = new UpdateStoreInventoryProductDTO();
        createStoreInventoryCreateProductDTO.setProduct_name(storeInventory.getProduct().getName());
        createStoreInventoryCreateProductDTO.setStore_id(storeInventory.getStore().getId());
        createStoreInventoryCreateProductDTO.setRetail_price(Long.valueOf(storeInventory.getRetail_price()));
        createStoreInventoryCreateProductDTO.setQuantity(Long.valueOf(storeInventory.getQuantity()));
        return createStoreInventoryCreateProductDTO;
    }

    public SellStoreInventoryProductDTO createProductOutflow(Principal principal, StoreInventoryDTO dto) {
        System.out.println("AUTHORIZATION PASSED");
        Users user = usersRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found: " + principal.getName()));
        System.out.println("User found: " + user.getUsername());

        Products product = productRepository.findById(dto.getProduct_id())
                .orElseThrow(() -> new RuntimeException("Product not found: " + dto.getProduct_name()));

        StoreInventory storeInventory = storeInventoryRepository.findByStoreIdAndProductId(dto.getStore_id(), product.getId())
                .orElseThrow(() -> new RuntimeException("Store not found with store_id: " + dto.getStore_id() + " and product_id: " + product.getId()));

        System.out.println("StoreInventory found: " + storeInventory.getProduct().getName());

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

        SellStoreInventoryProductDTO createStoreInventoryCreateProductDTO = new SellStoreInventoryProductDTO();
        createStoreInventoryCreateProductDTO.setProduct_name(storeInventory.getProduct().getName());
        createStoreInventoryCreateProductDTO.setStore_id(storeInventory.getStore().getId());
        createStoreInventoryCreateProductDTO.setRetail_price(Long.valueOf(storeInventory.getRetail_price()));
        createStoreInventoryCreateProductDTO.setQuantity(Long.valueOf(storeInventory.getQuantity()));

        return createStoreInventoryCreateProductDTO;
    }


    public CreateStoreInventoryCreateProductDTO createProductInflow(Principal principal, StoreInventoryDTO dto) {
        System.out.println("222");
        System.out.println(dto.getStore_id());
        System.out.println(dto.getProduct_name());
        System.out.println(dto.getRetail_price());
        System.out.println(dto.getQuantity());
        Users user = usersRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found: " + principal.getName()));
        System.out.println(user.getUsername());
        Products products = new Products();
        products.setName(dto.getProduct_name());
        System.out.println(products.getId());
        System.out.println(products.getName());
        productRepository.save(products);
        StoreInventory storeInventory = new StoreInventory();
        storeInventory.setProduct(products);
        Store store = storeRepository.findById(dto.getStore_id()).orElseThrow();
        storeInventory.setStore(store);
        storeInventory.setRetail_price(Math.toIntExact(dto.getRetail_price()));
        storeInventory.setQuantity(Math.toIntExact(dto.getQuantity()));
        ProductInflow inflow = new ProductInflow();
        inflow.setUser(user);
        inflow.setStoreInventory(storeInventory);
        inflow.setQuantity(Math.toIntExact(dto.getQuantity()));
        inflow.setWholesale_price(Math.toIntExact(dto.getRetail_price()));
        System.out.println(storeInventory.getId());
        System.out.println(storeInventory.getProduct().getName());
        System.out.println(storeInventory.getStore().getName());
        storeInventoryRepository.save(storeInventory);
        productInflowRepository.save(inflow);
        CreateStoreInventoryCreateProductDTO createStoreInventoryCreateProductDTO = new CreateStoreInventoryCreateProductDTO();
        createStoreInventoryCreateProductDTO.setProduct_name(storeInventory.getProduct().getName());
        createStoreInventoryCreateProductDTO.setStore_id(storeInventory.getStore().getId());
        createStoreInventoryCreateProductDTO.setRetail_price(Long.valueOf(storeInventory.getRetail_price()));
        createStoreInventoryCreateProductDTO.setQuantity(Long.valueOf(storeInventory.getQuantity()));
        return createStoreInventoryCreateProductDTO;
    }


}
