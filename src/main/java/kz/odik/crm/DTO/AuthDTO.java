package kz.odik.crm.DTO;

import lombok.Data;

@Data

public class AuthDTO {
    private String username;
    private String password;

    public AuthDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
