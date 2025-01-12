package kz.odik.crm.DTO;

import kz.odik.crm.entity.AccessRights;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleResponseDTO {
    private Long id;
    private String name;
    private Set<AccessRights> access_rights_name;
}
