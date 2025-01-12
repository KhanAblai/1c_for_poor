package kz.odik.crm.controller;

import kz.odik.crm.DTO.*;
import kz.odik.crm.Repository.UsersRepository;
import kz.odik.crm.Service.UserService;
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
    public CreateUsersResponseDTO createUser(@RequestBody UserDTO dto) {
        System.out.println(dto.getUsername());
        System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
        return userService.create(dto);
    }

    @PreAuthorize("hasAuthority('UPDATE_USER') or hasAuthority('CREATE_USER')")
    @GetMapping("/all")
    public List<UserGetAllResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAuthority('UPDATE_USER')")
    @PostMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @PreAuthorize("hasAuthority('UPDATE_USER') or hasAuthority('CREATE_USER')")
    @GetMapping("/by-role/{id}")
    public List<UserGetAllResponseDTO> getUsersByRole(@PathVariable("id") Long roleId) {
        return userService.getAllUsersByRole(roleId);
    }

    @PreAuthorize("hasAuthority('UPDATE_USER')")
    @PostMapping("/update/{id}")
    public UpdateUserResponseDTO updateUser(@PathVariable Long id, @RequestBody UpdateUserDto dto) {
        return userService.updateUser(id, dto);
    }
}
