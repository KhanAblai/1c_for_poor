package kz.odik.crm.controller;

import kz.odik.crm.DTO.*;
import kz.odik.crm.Repository.UsersRepository;
import kz.odik.crm.Service.ExcelExportService;
import kz.odik.crm.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UsersRepository usersRepository;
    @Autowired
    private ExcelExportService excelExportService;

    @PreAuthorize("hasAuthority('CREATE_USER')")
    @PostMapping("/create")
    public CreateUsersResponseDTO createUser(@RequestBody UserDTO dto) {
        System.out.println(dto.getUsername());
        System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
        return userService.create(dto);
    }

    @PreAuthorize("hasAuthority('CREATE_USER')")
    @GetMapping("/export-user-activity")
    public ResponseEntity<byte[]> export() {
        byte[] bytes = excelExportService.exportUserActivityToExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "UserActivity.xlsx");
        return ResponseEntity.ok().headers(headers).body(bytes);
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

    @PostMapping("/check-in")
    public ResponseEntity<String> checkIn(Principal principal) {
        try {
            userService.checkIn(principal);
            return ResponseEntity.ok("Checked in successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/check-out")
    public ResponseEntity<String> checkOut(Principal principal) {
        try {
            userService.checkOut(principal);
            return ResponseEntity.ok("Checked out successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('UPDATE_USER')")
    @PostMapping("/update/{id}")
    public UpdateUserResponseDTO updateUser(@PathVariable Long id, @RequestBody UpdateUserDto dto) {
        return userService.updateUser(id, dto);
    }
}
