package management.web;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import management.data.EmployeeRepository;
import management.objectData.Employee;
import management.objectData.Position;
import management.objectData.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/")
@SessionAttributes("user")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @ModelAttribute("employee")
    private Employee getEmployee() {
        return new Employee();
    }

    @GetMapping("/entranceFromEmpl")
    public String getEntranceFromEmployeeCont() {
        return "entrance";
    }

    @GetMapping("/employeeManagementHome")
    public String getEmployeeManagementHome() {
        return "employeeManagementHome";
    }

    @GetMapping("/viewAllEmployees")
    public String getViewAllEmployees(@ModelAttribute("user") User user) {
        if (user.getEmployees().isEmpty())
            return "noEmployeesExist";
        return "viewAllEmployees";
    }

    @GetMapping("/addEmployee")
    public String getAddEmployee(@ModelAttribute("employee") Employee employee,
                                 @ModelAttribute("user") User user) {
        employee.setChosenOnes(new ArrayList<>(user.getPositionsWithoutDeletedOne()));
        return "addEmployee";
    }

    @GetMapping("/deleteEmployee")
    public String getDeleteEmployee(@ModelAttribute("user") User user) {
        if (user.getEmployees().isEmpty())
            return "noEmployeesExist";
        user.setChosenOnesEmployees(user.getEmployees());
        return "deleteEmployee";
    }

    @GetMapping("/editEmployeeEntrance")
    public String getEditEmployee(@ModelAttribute("user") User user) {
        if (user.getEmployees().isEmpty())
            return "noEmployeesExist";
        user.setChosenOnesEmployees(new ArrayList<>(user.getEmployees()));
        return "chooseEmployeeToEdit";
    }

    @PostMapping("/checkAddingEmployee")
    public String checkAddingEmployee(
            @ModelAttribute("user") User user,
            @Valid @ModelAttribute("employee") Employee employee, Errors errors) {

        if (errors.hasErrors()) {
            employee.setChosenOnes(new ArrayList<>(user.getPositionsWithoutDeletedOne()));
            return "addEmployee";
        }
        employee.setUser(user);
        employee.setPosition(employee.getChosenOnes().get(0));
        employeeRepository.save(employee);
        user.getEmployees().add(employee);
        return "employeeManagementHome";
    }

    @PostMapping("/checkDeleteEmployee")
    @Transactional
    public String checkDeleteEmployee(@Valid @ModelAttribute("user") User user, Errors errors) {
        if (errors.hasFieldErrors("chosenOnesEmployees")) {
            user.setChosenOnesEmployees(user.getEmployees());
            return "deleteEmployee";
        }
        List<Employee> employeesToDelete = user.getChosenOnesEmployees();
        for (Employee employee : employeesToDelete) {
            employeeRepository.deleteByUserAndId(user, employee.getId());
            user.getEmployees().removeIf(curr -> curr.getId() == employee.getId());
        }
        return "employeeManagementHome";
    }

    @PostMapping("/checkChooseEmployeeToEdit")
    public String checkChooseEmployeeToEdit(@ModelAttribute("editedEmployee") EditedEmployee editedEmployee,
                                            @Valid @ModelAttribute("user") User user, Errors errors) {
        if (errors.hasFieldErrors("chosenOnesEmployees")) {
            user.setChosenOnesEmployees(user.getEmployees());
            return "chooseEmployeeToEdit";
        }
        user.setEmployeeToEdit(user.getChosenOnesEmployees().get(0));
        editedEmployee.setEditedEmployeesPositions(new ArrayList<>(user.getPositionsWithoutDeletedOne()));
        return "editEmployee";
    }

    @Data
    public static class EditedEmployee {
        private String firstName;
        private String middleName;
        private String lastName;
        @Size(max = 1, message = "Нужно выбрать одну должность")
        private List<Position> editedEmployeesPositions;
        @Pattern(regexp = "^$|(^(0?[1-9]|[12][0-9]|3[01])[\\/\\-](0?[1-9]|1[012])[\\/\\-]\\d{4}$)",
                message = "Дата должна быть формата ДД/ММ/ГГГГ")
        private String date;
    }

    @ModelAttribute("editedEmployee")
    public EditedEmployee getEditEmployee() {
        return new EditedEmployee();
    }

    @PostMapping("/checkEditEmployeeHome")
    @Transactional
    public String checkEditEmployeeHome(@ModelAttribute("user") User user,
                                        @Valid @ModelAttribute("editedEmployee") EditedEmployee editedEmployee, Errors errors) {
        if (errors.hasErrors()) {
            editedEmployee.setEditedEmployeesPositions(new ArrayList<>(user.getPositionsWithoutDeletedOne()));
            return "editEmployee";
        }
        user.getEmployees().removeIf(curr -> curr.getId() == user.getEmployeeToEdit().getId());
        Employee newEmployee = getNewEmployee(user, editedEmployee);

        employeeRepository.updateEmployee(newEmployee.getFirstName(), newEmployee.getMiddleName(), newEmployee.getLastName(),
                newEmployee.getDate(), newEmployee.getPosition(), newEmployee.getId());

        user.getEmployees().add(newEmployee);
        return "employeeManagementHome";
    }

    private Employee getNewEmployee(User user, EditedEmployee editedEmployee) {
        Employee newEmployee = user.getEmployeeToEdit();
        if (!editedEmployee.getFirstName().isEmpty())
            newEmployee.setFirstName(editedEmployee.getFirstName());
        if (!editedEmployee.getMiddleName().isEmpty())
            newEmployee.setMiddleName(editedEmployee.getMiddleName());
        if (!editedEmployee.getLastName().isEmpty())
            newEmployee.setLastName(editedEmployee.getLastName());
        if (!editedEmployee.getDate().isEmpty())
            newEmployee.setDate(editedEmployee.getDate());
        if (!editedEmployee.getEditedEmployeesPositions().isEmpty())
            newEmployee.setPosition(editedEmployee.getEditedEmployeesPositions().get(0));
        return newEmployee;
    }
}
