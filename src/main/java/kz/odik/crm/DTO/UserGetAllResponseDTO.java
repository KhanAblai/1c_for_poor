package kz.odik.crm.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGetAllResponseDTO {
    private Long id;
    private String username;
    private Long roleID;
    private List<String> stores;
}
