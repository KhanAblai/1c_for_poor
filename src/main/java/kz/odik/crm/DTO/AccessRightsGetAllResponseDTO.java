package kz.odik.crm.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessRightsGetAllResponseDTO {
    private Long id;
    private String name;
    private String description;
}
