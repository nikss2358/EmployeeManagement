package management.objectData;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;
import management.validation.UsernameExist;

import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private int id;
    @NotBlank(message = "Username is required")
    @UsernameExist
    private String username;
    @Size(min = 5, message = "Password should be at least 5 characters long")
    private String password;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Employee> userEmployees;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Position> positions;
    @Transient
    @Size(min = 1, message = "You need to choose at least one position")
    private List<Position> chosenOnes;
    @Transient
    private String nameToEdit;
}
