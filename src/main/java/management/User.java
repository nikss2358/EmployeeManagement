package management;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

import java.util.Collection;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private int id;
    @NotBlank(message = "Username is required")
    @UsernameExist(message = "This username's already exists. Sign in or create another one")
    private String username;
    @Size(min = 5, message = "Password should be at least 5 characters long")
    private String password;
    @OneToMany()
    private Collection<Employee> userEmployees;
    @OneToMany()
    private Collection<Position> userPositions;

}
