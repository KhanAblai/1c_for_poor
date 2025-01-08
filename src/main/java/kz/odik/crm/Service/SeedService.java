package kz.odik.crm.Service;

import kz.odik.crm.Repository.*;
import kz.odik.crm.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SeedService {
    @Autowired
    private AccessRightsRepository accessRightsRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private StoreInventoryRepository storeInventoryRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private ProductOutflowRepository productOutflowRepository;
    @Autowired
    private ProductInflowRepository productInflowRepository;
    @Autowired

    private PasswordEncoder passwordEncoder;

    // Основной метод для инициализации данных
    public String seed() {
        System.out.println("1");
        // Создаем права доступа
        AccessRights updateUsersAccess = createAccessRight("UPDATE_STOREINVETORY_PRODUCTS", "Управление сотрудниками");
        AccessRights createUsersAccess = createAccessRight("SELL_STOREINVENTORY_PRODUCTS", "Просмотр пользователей");

        // Создаем роль с набором прав доступа
        Roles adminRole = createRole("Менеджер", Set.of(updateUsersAccess, createUsersAccess));

        // Создаем пользователя с ролью
        Users adminUser = createUser("Adlet", "666", adminRole);

        // Создаем товар
        Products product = createProduct("Кириешки");

        // Создаем магазин
        Store store = createStore("УДамира", "АлиханУ");

        // Создаем инвентарь магазина
        StoreInventory storeInventory = createStoreInventory(store, product, 228, 1337);

        // Создаем движения товара
        createProductOutflow(adminUser, storeInventory, 666);
        createProductInflow(adminUser, storeInventory, 7234312, 2025);
        System.out.println("2");
        return "Database seeded successfully";
    }

    // Метод для создания прав доступа
    private AccessRights createAccessRight(String name, String description) {
        AccessRights accessRight = new AccessRights();
        accessRight.setName(name);
        accessRight.setDescription(description);
        return accessRightsRepository.save(accessRight);
    }

    // Метод для создания роли с правами доступа
    private Roles createRole(String name, Set<AccessRights> accessRights) {
        Roles role = new Roles();
        role.setName(name);
        role.setAccessRights(accessRights);
        return rolesRepository.save(role);
    }

    // Метод для создания пользователя с ролью
    private Users createUser(String username, String password, Roles role) {
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(password); // В реальной ситуации используйте шифрование пароля
        user.setRole(role);
        return usersRepository.save(user);
    }

    // Метод для создания товара
    private Products createProduct(String name) {
        Products product = new Products();
        product.setName(name);
        return productsRepository.save(product);
    }

    // Метод для создания магазина
    private Store createStore(String name, String place) {
        Store store = new Store();
        store.setName(name);
        store.setPlace(place);
        return storeRepository.save(store);
    }

    // Метод для создания инвентаря магазина
    private StoreInventory createStoreInventory(Store store, Products product, int quantity, int retailPrice) {
        StoreInventory storeInventory = new StoreInventory();
        storeInventory.setStore(store);
        storeInventory.setProduct(product);
        storeInventory.setQuantity(quantity);
        storeInventory.setRetail_price(retailPrice);
        return storeInventoryRepository.save(storeInventory);
    }

    // Метод для создания исходящего движения товара
    private ProductOutflow createProductOutflow(Users user, StoreInventory storeInventory, int quantity) {
        ProductOutflow outflow = new ProductOutflow();
        outflow.setUser(user);
        outflow.setStoreInventory(storeInventory);
        outflow.setQuantity(quantity);
        return productOutflowRepository.save(outflow);
    }

    // Метод для создания входящего движения товара
    private ProductInflow createProductInflow(Users user, StoreInventory storeInventory, int quantity, int wholesalePrice) {
        ProductInflow inflow = new ProductInflow();
        inflow.setUser(user);
        inflow.setStoreInventory(storeInventory);
        inflow.setQuantity(quantity);
        inflow.setWholesale_price(wholesalePrice);
        return productInflowRepository.save(inflow);
    }
}
