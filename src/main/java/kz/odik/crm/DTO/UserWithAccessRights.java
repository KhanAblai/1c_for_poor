package kz.odik.crm.DTO;

import kz.odik.crm.entity.AccessRights;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class UserWithAccessRights {
    private String username;
    private String password;
    private Set<AccessRights> accessRights;

    public UserWithAccessRights(String username, String password, Set<AccessRights> ar) {
        System.out.println(ar);
        System.out.println("____________________________________");
        this.accessRights = ar;
        this.password = password;
        this.username = username;
    }
}