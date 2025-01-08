package kz.odik.crm.Service;

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

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Loading user by username: " + username);
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        System.out.println("User found: " + user.getUsername() + " with password: " + user.getPassword());

        Collection<? extends GrantedAuthority> authorities = new CopyOnWriteArrayList<>(
                mapRolesToAuthorities(user.getRole())
        );
        System.out.println("Authorities: " + authorities);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );

        System.out.println("UserDetails created: " + userDetails.getUsername() + " with authorities: " + userDetails.getAuthorities());
        return userDetails;
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Roles role) {
        System.out.println("Mapping roles to authorities for role: " + role.getName());

        Set<AccessRights> accessRightsCopy;
        synchronized (role.getAccessRights()) {
            accessRightsCopy = new HashSet<>(role.getAccessRights());
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (AccessRights accessRight : accessRightsCopy) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + accessRight.getName()));
        }

        System.out.println("Mapped authorities: " + authorities);
        return authorities;
    }


}
