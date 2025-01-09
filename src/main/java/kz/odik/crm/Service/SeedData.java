package kz.odik.crm.Service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeedData {
    @JsonProperty("accessRights")
    private List<AccessRightData> accessRights;
    @JsonProperty("roles")
    private List<RoleData> roles;
    @JsonProperty("stores")
    private List<StoreData> stores;
    @JsonProperty("users")
    private List<UserData> users;
    @JsonProperty("products")
    private List<ProductData> products;

    // Конструкторы, геттеры и сеттеры
    @Data
    public static class RoleData {
        @JsonProperty("name")
        private String name;
        @JsonProperty("accessRights")
        private Set<String> accessRights; // Изменено для хранения имен прав доступа
        // Конструкторы, геттеры и сеттеры
    }

    @Data
    public static class AccessRightData {
        @JsonProperty("name")
        private String name;
        @JsonProperty("description")
        private String description;
        // Конструкторы, геттеры и сеттеры
    }

    @Data
    public static class StoreData {
        @JsonProperty("name")
        private String name;
        @JsonProperty("place")
        private String place;
        // Конструкторы, геттеры и сеттеры
    }

    @Data
    public static class UserData {
        @JsonProperty("username")
        private String username;
        @JsonProperty("password")
        private String password;
        @JsonProperty("role")
        private String role;
        @JsonProperty("stores")
        private List<String> stores;
        // Конструкторы, геттеры и сеттеры
    }

    @Data
    public static class ProductData {
        @JsonProperty("name")
        private String name;
    }

}
