package management.objectData;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;
import management.validation.UsernameExist;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private int id;
    @NotBlank(message = "Укажите имя пользователя")
    @UsernameExist
    private String username;
    @Size(min = 5, message = "Минимум 5 знаков")
    private String password;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Employee> employees;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Position> positions;
    @Transient
    @Size(min = 1, message = "Выберите минимум 1 должность")
    private List<Position> chosenOnesPositions;
    @Transient
    @Size(min = 1, message = "Выберите минимум 1 сотрудника")
    private List<Employee> chosenOnesEmployees;
    @Transient
    private String nameToEdit;
    @Transient
    private Employee employeeToEdit;
    @Transient
    private List<Position> positionsWithoutDeletedOne;


    public List<Position> getPositionsWithoutDeletedOne() {
        List<Position> withoutDeletedOne = new ArrayList<>();
        for (Position position : positions) {
            if (!position.getName().equals("Удалена"))
                withoutDeletedOne.add(position);
        }
        return withoutDeletedOne;
    }
}
