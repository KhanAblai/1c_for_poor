package kz.odik.crm.Service;

import jakarta.transaction.Transactional;
import kz.odik.crm.Repository.AccessRightsRepository;
import kz.odik.crm.Repository.RoleAccessRightsRepository;
import kz.odik.crm.Repository.UsersRepository;
import kz.odik.crm.entity.AccessRights;
import kz.odik.crm.entity.Roles;
import kz.odik.crm.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AccessRightsRepository accessRightsRepository;
    @Autowired
    private RoleAccessRightsRepository roleAccessRightsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            System.out.println("Loading user by username: " + username);
            Users user = usersRepository.findByUsername(username).orElseThrow();
            Roles role = user.getRole();
            List<AccessRights> accessRights = roleAccessRightsRepository.findAccessRightsByRole(role);

            Collection<? extends GrantedAuthority> authorities = new CopyOnWriteArrayList<>(
                    accessRights.stream().map(AccessRights::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toList())
            );
  
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    authorities
            );

            System.out.println("UserDetails created: " + userDetails.getUsername() + " with authorities: " + userDetails.getAuthorities());
            return userDetails;
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    @Transactional
    public Users getUserWithRoleAndAccessRights(String username) {
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Логирование данных
        System.out.println("User: " + user.getUsername());
        System.out.println("Role: " + user.getRole().getName());
        System.out.println("AccessRights size: " + user.getRole().getAccessRights().size());
        user.getRole().getAccessRights().forEach(accessRight -> System.out.println("AccessRight: " + accessRight.getName()));

        // Принудительная инициализация коллекции accessRights
        user.getRole().getAccessRights().size(); // Инициализация коллекции

        return user;
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Roles role) {
        System.out.println("Mapping roles to authorities for role: " + role.getName());

        Set<AccessRights> accessRightsCopy;
        synchronized (role.getAccessRights()) {
            accessRightsCopy = new HashSet<>(role.getAccessRights());
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (AccessRights accessRight : accessRightsCopy) {
            System.out.println("Access right: " + accessRight.getName());
            authorities.add(new SimpleGrantedAuthority("ROLE_" + accessRight.getName()));
        }

        System.out.println("Mapped authorities: ");
        System.out.println(authorities);
        return authorities;
    }


}
