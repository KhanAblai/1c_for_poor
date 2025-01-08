package kz.odik.crm.DTO;

import kz.odik.crm.entity.AccessRights;
import lombok.Data;

@Data
public class RolesDTO {
    private String name;
    private int[] access_rights_ids;
}
