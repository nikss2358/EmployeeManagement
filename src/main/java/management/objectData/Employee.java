package management.objectData;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue
    private int id;
    @NotBlank(message = "Укажите имя")
    private String firstName;
    @NotBlank(message = "Укажите отчество")
    private String middleName;
    @NotBlank(message = "Укажите фамилию")
    private String lastName;
    @ManyToOne()
    private User user;
    @ManyToOne()
    private Position position;
    @Pattern(regexp = "^(0?[1-9]|[12][0-9]|3[01])[\\/\\-](0?[1-9]|1[012])[\\/\\-]\\d{4}$", message = "Дата должна быть формата ДД/ММ/ГГГГ")
    private String date;
    @Transient
    @Size(min = 1, max = 1, message = "Нужно выбрать одну должность")
    private List<Position> chosenOnes;
    @Override
    public String toString() {
        return lastName + "  " + firstName + "  " + middleName;
    }
}
