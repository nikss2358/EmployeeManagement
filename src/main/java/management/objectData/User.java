package management.objectData;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;
import management.validation.UsernameExist;

import java.util.Collection;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    @NotBlank(message = "Username is required")
    @UsernameExist
    private String username;
    @Size(min = 5, message = "Password should be at least 5 characters long")
    private String password;
    @OneToMany()
    private Collection<Employee> userEmployees;
    @OneToMany()
    private Collection<Position> userPositions;
}
