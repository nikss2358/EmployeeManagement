package management.objectData;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;

@Entity
@Data
public class Position {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    @OneToMany(mappedBy = "position", fetch = FetchType.EAGER)
    private Collection<Employee> employeesOnThisPosition;
}
