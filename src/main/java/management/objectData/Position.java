package management.objectData;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import management.validation.PositionNameExist;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Position {
    @Id
    @GeneratedValue
    private int id;
    @NotBlank(message = "Укажите название должности")
    @PositionNameExist
    private String name;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "position", fetch = FetchType.EAGER)
    private List<Employee> employees;
    @Override
    public String toString() {
        return name;
    }
}
