package kz.odik.crm.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.odik.crm.Repository.*;
import kz.odik.crm.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SeedService {

    @Autowired
    private AccessRightsRepository accessRightsRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private StoreInventoryRepository storeInventoryRepository;
    @Autowired
    private ProductOutflowRepository productOutflowRepository;
    @Autowired
    private ProductInflowRepository productInflowRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createFullData(SeedData seedData) {
        // Создание прав доступа
        seedData.getAccessRights().forEach(ar -> createOrGetAccessRight(ar.getName(), ar.getDescription()));
        System.out.println("Created " + accessRightsRepository.findAll().size() + " ARs");
        // Создание ролей
        seedData.getRoles().forEach(roleData -> {
            Set<AccessRights> accessRights = roleData.getAccessRights().stream()
                    .map(arName -> accessRightsRepository.findByName(arName)
                            .orElseThrow(() -> new RuntimeException("Access right not found: " + arName)))
                    .collect(Collectors.toSet());
            createOrGetRole(roleData.getName(), accessRights);
        });
        System.out.println("Created " + rolesRepository.findAll().size() + " roles");

        seedData.getStores().forEach(storeData -> createOrGetStore(storeData.getName(), storeData.getPlace()));

        System.out.println("Created " + storeRepository.findAll().size() + " stores");
        // Создание пользователей
        seedData.getUsers().forEach(userData -> {
            Roles role = rolesRepository.findByName(userData.getRole())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + userData.getRole()));
            Users user = createUser(userData.getUsername(), userData.getPassword(), role);

            // Привязка пользователя к магазинам
            userData.getStores().forEach(storeName -> {
                Store store = storeRepository.findByName(storeName)
                        .orElseThrow(() -> new RuntimeException("Store not found: " + storeName));
                // Логика привязки пользователя к магазину
                user.getStores().add(store);
            });
            usersRepository.save(user);
        });

    }

    // Метод для загрузки данных из JSON файла
    public SeedData loadSeedData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File("src/main/java/kz/odik/crm/Service/seed/seedData.json"), SeedData.class);
    }

    public String seed() {
        try {
            SeedData seedData = loadSeedData();
            clearDatabase();
            createFullData(seedData);
            return "Database seeded successfully";
        } catch (IOException e) {
            System.out.println(e);
            return "Failed to seed database";
        }
    }

    public void clearDatabase() {
        System.out.println("Deleting product outflows");
        productOutflowRepository.findAll().forEach(productOutflowRepository::delete);
        System.out.println("Deleting product inflows");
        productInflowRepository.findAll().forEach(productInflowRepository::delete);
        System.out.println("Deleting store inventories");
        storeInventoryRepository.findAll().forEach(storeInventoryRepository::delete);
        System.out.println("Deleting stores");
        storeRepository.findAll().forEach(storeRepository::delete);
        System.out.println("Deleting products");
        productsRepository.findAll().forEach(productsRepository::delete);
        System.out.println("Deleting users");
        usersRepository.findAll().forEach(usersRepository::delete);
        System.out.println("Deleting roles");
        rolesRepository.findAll().forEach(rolesRepository::delete);
        System.out.println("Deleting access rights");
        accessRightsRepository.findAll().forEach(accessRightsRepository::delete);
        System.out.println("Remove done");
    }

    private Store createOrGetStore(String name, String place) {
        return storeRepository.findByName(name)
                .orElseGet(() -> createStore(name, place));
    }

    private AccessRights createOrGetAccessRight(String name, String description) {
        return accessRightsRepository.findByName(name)
                .orElseGet(() -> createAccessRight(name, description));
    }

    private AccessRights createAccessRight(String name, String description) {
        AccessRights accessRight = new AccessRights();
        accessRight.setName(name);
        accessRight.setDescription(description);
        return accessRightsRepository.save(accessRight);
    }

    private Roles createOrGetRole(String name, Set<AccessRights> accessRights) {
        return rolesRepository.findByName(name)
                .orElseGet(() -> createRole(name, accessRights));
    }

    private Roles createRole(String name, Set<AccessRights> accessRights) {
        Roles role = new Roles();
        role.setName(name);
        role.setAccessRights(accessRights);
        return rolesRepository.save(role);
    }

    private Users createUser(String username, String password, Roles role) {
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return usersRepository.save(user);
    }

    private Products createProduct(String name) {
        Products product = new Products();
        product.setName(name);
        return productsRepository.save(product);
    }

    private Store createStore(String name, String place) {
        Store store = new Store();
        store.setName(name);
        store.setPlace(place);
        return storeRepository.save(store);
    }

    private StoreInventory createStoreInventory(Store store, Products product, int quantity, int retailPrice) {
        StoreInventory storeInventory = new StoreInventory();
        storeInventory.setStore(store);
        storeInventory.setProduct(product);
        storeInventory.setQuantity(quantity);
        storeInventory.setRetail_price(retailPrice);
        return storeInventoryRepository.save(storeInventory);
    }

    private ProductOutflow createProductOutflow(Users user, StoreInventory storeInventory, int quantity) {
        ProductOutflow outflow = new ProductOutflow();
        outflow.setUser(user);
        outflow.setStoreInventory(storeInventory);
        outflow.setQuantity(quantity);
        return productOutflowRepository.save(outflow);
    }

    private ProductInflow createProductInflow(Users user, StoreInventory storeInventory, int quantity, int wholesalePrice) {
        ProductInflow inflow = new ProductInflow();
        inflow.setUser(user);
        inflow.setStoreInventory(storeInventory);
        inflow.setQuantity(quantity);
        inflow.setWholesale_price(wholesalePrice);
        return productInflowRepository.save(inflow);
    }
}
