package kz.odik.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String place;
    @ManyToMany(mappedBy = "stores")
    @JsonIgnoreProperties("stores")
    private Set<Products> products;
    
    @ManyToMany(mappedBy = "stores")
    @JsonIgnoreProperties("stores")
    private Set<Users> users;

}
