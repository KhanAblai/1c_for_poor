package kz.odik.crm.Service;

import kz.odik.crm.DTO.*;
import kz.odik.crm.Repository.RolesRepository;
import kz.odik.crm.Repository.StoreRepository;
import kz.odik.crm.Repository.UsersRepository;
import kz.odik.crm.Repository.UsersStoresRepository;
import kz.odik.crm.entity.Roles;
import kz.odik.crm.entity.Store;
import kz.odik.crm.entity.Users;
import kz.odik.crm.entity.UsersStores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsersStoresRepository usersStoresRepository;

    public UserService(RolesRepository rolesRepository, UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.rolesRepository = rolesRepository;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CreateUsersResponseDTO create(UserDTO dto) {
        if (dto.getRoleID() == null) {
            throw new IllegalArgumentException("Role ID must not be null");
        }
        System.out.println("Нхауй");
        Roles role = rolesRepository.findById(dto.getRoleID()).orElseThrow(() -> new RuntimeException("Role not found with ID: " + dto.getRoleID()));
        Users user = new Users();
        System.out.println("Нхауй");
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);
        System.out.println("Нхауй");
        Store store = storeRepository.findById(dto.getStoreID()).orElseThrow();
        System.out.println("Н");
        UsersStores usersStores = new UsersStores();
        usersStores.setUser(user);
        usersStores.setStore(store);
        usersRepository.save(user);
        usersStoresRepository.save(usersStores);
        System.out.println(user.getUsername());
        CreateUsersResponseDTO createUsersResponseDTO = new CreateUsersResponseDTO();
        createUsersResponseDTO.setId(user.getId());
        createUsersResponseDTO.setRole(user.getRole().getName());
        createUsersResponseDTO.setUsername(user.getUsername());
        System.out.println("Нхауй");
        List<String> storeNamesList = new ArrayList<>();
        for (Store s : user.getStores()) {
            storeNamesList.add(s.getName());
        }
        // Преобразуем список в массив строк
        String[] storeNames = storeNamesList.toArray(new String[0]);
        System.out.println("ЗАхуй");
        createUsersResponseDTO.setStores(storeNames);
        return createUsersResponseDTO;
    }

    public void deleteUserById(Long id) {
        usersRepository.deleteById(id);
    }

    public List<UserGetAllResponseDTO> getAllUsers() {
        List<Users> users = usersRepository.findAll();
        List<UserGetAllResponseDTO> userGetAllResponseDTOS = new ArrayList<>();
        for (Users user : users) {
            List<Store> usersStores = usersStoresRepository.findStoresByUserId(user.getId());
            userGetAllResponseDTOS.add(new UserGetAllResponseDTO(user.getId(), user.getUsername(), user.getRole().getId(), usersStores.stream().map(Store::getName).collect(Collectors.toList())));
        }
        return userGetAllResponseDTOS;
    }

    public Optional<Users> getUserById(Long id) {
        return usersRepository.findById(id);
    }

    public List<UserGetAllResponseDTO> getAllUsersByRole(Long roleid) {
        List<Users> users = usersRepository.findByRoleId(roleid);
        List<UserGetAllResponseDTO> userGetAllResponseDTOS = new ArrayList<>();
        for (Users user : users) {
            List<Store> usersStores = usersStoresRepository.findStoresByUserId(user.getId());
            userGetAllResponseDTOS.add(new UserGetAllResponseDTO(user.getId(), user.getUsername(), user.getRole().getId(), usersStores.stream().map(Store::getName).collect(Collectors.toList())));
        }
        return userGetAllResponseDTOS;
    }

    public UpdateUserResponseDTO updateUser(Long userId, UpdateUserDto dto) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        if (dto.getName() != null) {
            user.setUsername(dto.getName());
        }
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getRoleId() != null) {
            Roles role = rolesRepository.findById(dto.getRoleId()).orElseThrow();
            user.setRole(role);
        }

        if (dto.getStoreId() != null) {
            List<Store> usersStores = usersStoresRepository.findStoresByUserId(user.getId());

            // Если список usersStores пуст, добавляем магазин и сохраняем пользователя
            if (usersStores.isEmpty()) {
                Store store = storeRepository.findById(dto.getStoreId()).orElseThrow(() -> new RuntimeException("Not found"));
                user.getStores().add(store);
                usersRepository.save(user);
            }

            // Если список не пуст, добавляем магазин в существующий список
            System.out.println("UserStoresfound" + usersStores.get(0).getName());
            Store store = storeRepository.findById(dto.getStoreId()).orElseThrow(() -> new RuntimeException("Store not found"));
            System.out.println(store.getName());
            System.out.println("Зашло");
            UsersStores userstore = new UsersStores();
            userstore.setUser(user);
            userstore.setStore(store);
            usersStoresRepository.save(userstore);
            System.out.println("Вышло");
        }
        System.out.println("ЗАхуй");
        usersRepository.save(user);
        System.out.println("ЗАхуй");
        UpdateUserResponseDTO userGetAllResponseDTO = new UpdateUserResponseDTO();
        System.out.println("ЗАхуй");
        userGetAllResponseDTO.setUsername(user.getUsername());
        System.out.println("ЗАхуй");
        userGetAllResponseDTO.setRoleName(user.getRole().getName());
        System.out.println("ЗАхуй");
        System.out.println("ЗАхуй");
        List<Store> usersStores = usersStoresRepository.findStoresByUserId(user.getId());
        List<String> allstores = new ArrayList<>();
        for (Store s : usersStores) {
            allstores.add(String.valueOf(usersStores.stream().map(Store::getName).collect(Collectors.toList())));
        }
        userGetAllResponseDTO.setStoreNames(allstores);
        return userGetAllResponseDTO;
    }
}
