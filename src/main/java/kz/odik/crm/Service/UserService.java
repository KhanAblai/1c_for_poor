package kz.odik.crm.Service;

import jakarta.transaction.Transactional;
import kz.odik.crm.DTO.*;
import kz.odik.crm.Repository.*;
import kz.odik.crm.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
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
    @Autowired
    private UserActivityRepository userActivityRepository;

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

        // Сохраняем пользователя перед тем, как создавать UsersStores
        usersRepository.save(user);

        Long[] stores = dto.getStores();
        System.out.println(dto);
        List<UsersStores> newUsersStores = new ArrayList<>();

        for (Long store_id : stores) {
            Store store = new Store();
            store.setId(store_id);
            UsersStores usersStores = new UsersStores();
            usersStores.setStore(store);
            usersStores.setUser(user);
            newUsersStores.add(usersStores);
        }

        // Сохраняем UsersStores после сохранения пользователя
        usersStoresRepository.saveAll(newUsersStores);

        System.out.println(newUsersStores);
        System.out.println("Н");
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


    @Transactional
    public void checkIn(Principal principal) {
        // Проверяем, есть ли активная запись (где checkOut = false)
        Users user = usersRepository.findByUsername(principal.getName()).orElseThrow();
        Optional<UserActivity> activeActivity = userActivityRepository
                .findFirstByUserIdAndCheckOutFalseOrderByCheckedInDateDesc(user.getId());


        if (activeActivity.isPresent()) {
            throw new IllegalStateException("User is already checked in.");
        }

        // Создаем новую запись
        UserActivity userActivity = new UserActivity();
        userActivity.setUserId(user.getId());
        userActivity.setCheckedInDate(LocalDateTime.now());
        userActivity.setCheckOut(false); // Пользователь закреплен
        userActivityRepository.save(userActivity);
    }

    @Transactional
    public void checkOut(Principal principal) {
        Users user = usersRepository.findByUsername(principal.getName()).orElseThrow();
        // Находим последнюю активность пользователя, где checkOut = false
        Optional<UserActivity> activeActivity = userActivityRepository
                .findFirstByUserIdAndCheckOutFalseOrderByCheckedInDateDesc(user.getId());

        if (activeActivity.isPresent()) {
            UserActivity userActivity = activeActivity.get();
            userActivity.setCheckedOutDate(LocalDateTime.now());
            userActivity.setCheckOut(true); // Пользователь откреплен
            userActivityRepository.save(userActivity);
        } else {
            throw new IllegalStateException("No active check-in found for the user.");
        }
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

    @Transactional
    public UpdateUserResponseDTO updateUser(Long userId, UpdateUserDto dto) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        System.out.println(user.getUsername());
        System.out.println(dto);
        if (dto.getName() != null) {
            user.setUsername(dto.getName());
        }
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getRoleId() != null) {
            Roles role = rolesRepository.findById(dto.getRoleId()).orElseThrow();
            user.setRole(role);
            System.out.println("НАЖЛАСЬ РОЛЬ " + role.getId());
        }
        if (dto.getStores() != null) {
            Long[] stores = dto.getStores();
            usersStoresRepository.deleteByUserId(userId);
            List<UsersStores> newUsersStores = new ArrayList<>();
            for (Long store_id : stores) {
                Store store = new Store();
                store.setId(store_id);
                UsersStores usersStores = new UsersStores();
                usersStores.setStore(store);
                usersStores.setUser(user);
                newUsersStores.add(usersStores);
            }
            usersStoresRepository.saveAll(newUsersStores);
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
