package kz.odik.crm.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUsersResponseDTO {
    private Long id;
    private String username;
    private String role;
    private String[] stores;
}
