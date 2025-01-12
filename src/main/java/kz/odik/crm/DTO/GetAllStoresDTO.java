package kz.odik.crm.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class GetAllStoresDTO {
    private Long id;
    private String name;
    private String place;
}
