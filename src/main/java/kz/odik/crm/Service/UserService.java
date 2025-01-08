package kz.odik.crm.Service;

import kz.odik.crm.DTO.UpdateUserDto;
import kz.odik.crm.DTO.UserDTO;
import kz.odik.crm.Repository.RolesRepository;
import kz.odik.crm.Repository.StoreRepository;
import kz.odik.crm.Repository.UsersRepository;
import kz.odik.crm.entity.Roles;
import kz.odik.crm.entity.Store;
import kz.odik.crm.entity.Users;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private StoreRepository storeRepository;
    private RolesRepository rolesRepository;
    private UsersRepository usersRepository;
    private PasswordEncoder passwordEncoder;

    public Users create(UserDTO dto) {
        Roles role = rolesRepository.findById(dto.getRoleID()).orElseThrow();

        return usersRepository.save(Users.builder()
                .username(dto.getName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(role)
                .build());

    }

    public void deleteUserById(Long id) {
        usersRepository.deleteById(id);
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Optional<Users> getUserById(Long id) {
        return usersRepository.findById(id);
    }

    public List<Users> getAllUsersByRole(Long roleid) {
        return usersRepository.findByRoleId(roleid);
    }

    public Users updateUser(Long userId, UpdateUserDto dto) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found: " + userId));
        if (dto.getName() != null) {
            user.setUsername(dto.getName());
        }
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getRoleId() != null) {
            Roles role = rolesRepository.findById(dto.getRoleId()).orElseThrow(() -> new RuntimeException("Role not found: " + dto.getRoleId()));
            user.setRole(role);
        }
        if (dto.getStoreId() != null) {
            Store store = storeRepository.findById(dto.getStoreId()).orElseThrow(() -> new RuntimeException("Store not found: " + dto.getStoreId()));
            user.setStore(store);
        }
        return usersRepository.save(user);
    }
}
