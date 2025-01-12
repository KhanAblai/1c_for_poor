package kz.odik.crm.controller;

import kz.odik.crm.DTO.AuthDTO;
import kz.odik.crm.Repository.AccessRightsRepository;
import kz.odik.crm.Repository.RoleAccessRightsRepository;
import kz.odik.crm.Repository.RolesRepository;
import kz.odik.crm.Repository.UsersRepository;
import kz.odik.crm.Security.JwtTokenProvider;
import kz.odik.crm.entity.AccessRights;
import kz.odik.crm.entity.Users;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final RoleAccessRightsRepository roleAccessRightsRepository;
    private final AccessRightsRepository accessRightsRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthDTO authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            System.out.println("Authentication successful for user: " + authRequest.getUsername());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Collection<? extends GrantedAuthority> permissions = authentication.getAuthorities();

            String token = jwtTokenProvider.createToken(authRequest.getUsername(), permissions.stream().map(Object::toString).collect(Collectors.toList()));
            System.out.println("Token generated: " + token);
            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            System.out.println("Authentication failed: " + e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }


    // Метод для получения прав доступа пользователя
    private List<String> getUserPermissions(String username) {
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return user.getRole().getAccessRights().stream()
                .map(AccessRights::getName)  // Получаем имена прав доступа
                .collect(Collectors.toList());
    }
}
