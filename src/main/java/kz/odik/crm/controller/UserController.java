package kz.odik.crm.controller;

import kz.odik.crm.DTO.UpdateUserDto;
import kz.odik.crm.DTO.UserDTO;
import kz.odik.crm.Repository.UsersRepository;
import kz.odik.crm.Service.UserService;
import kz.odik.crm.entity.Users;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UsersRepository usersRepository;

    @PreAuthorize("hasAuthority('CREATE_USER')")
    @PostMapping("/create")
    public Users createUser(@RequestBody UserDTO dto) {
        return userService.create(dto);
    }

    @GetMapping("/all")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/by-role")
    public List<Users> getUsersByRole(@RequestParam Long roleId) {
        return userService.getAllUsersByRole(roleId);
    }

    @PreAuthorize("hasAuthority('UPDATE_USER')")
    @PostMapping("/update/{id}")
    public Users updateUser(@PathVariable Long id, @RequestBody UpdateUserDto dto) {
        return userService.updateUser(id, dto);
    }
}
