package kz.odik.crm.controller;

import kz.odik.crm.DTO.AuthDTO;
import kz.odik.crm.Repository.UsersRepository;
import kz.odik.crm.Security.JwtTokenProvider;
import kz.odik.crm.entity.AccessRights;
import kz.odik.crm.entity.Users;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthDTO authRequest) {
        try {
            System.out.println("Attempting to authenticate user: " + authRequest.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            System.out.println("Authentication successful for user: " + authRequest.getUsername());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            List<String> permissions = getUserPermissions(authRequest.getUsername());

            String token = jwtTokenProvider.createToken(authRequest.getUsername(), permissions);
            System.out.println("Token generated: " + token);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (UsernameNotFoundException e) {
            System.out.println("Authentication failed: User not found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        } catch (BadCredentialsException e) {
            System.out.println("Authentication failed: Bad credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (InternalAuthenticationServiceException e) {
            System.out.println("Authentication failed due to internal error: " + e.getMessage());
            e.printStackTrace();  // This will print the full stack trace to the logs
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Internal authentication error");
        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            e.printStackTrace();  // This will print the full stack trace to the logs
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
