package management.web;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.Getter;
import management.data.EmployeeRepository;
import management.data.PositionRepository;
import management.objectData.Employee;
import management.objectData.Position;
import management.objectData.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
@SessionAttributes("user")
public class PositionController {

    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;
    @Getter
    private static User userForPositionValidation;

    @Autowired
    PositionController(PositionRepository positionRepository, EmployeeRepository employeeRepository) {

        this.positionRepository = positionRepository;
        this.employeeRepository = employeeRepository;
    }

    @ModelAttribute("position")
    public Position getPosition() {
        return new Position();
    }

    @GetMapping("/positionManagementHome")
    public String getPositionManagementHome() {
        return "positionManagementHome";
    }

    @GetMapping("/viewAllPositions")
    public String getViewAllPositions(@ModelAttribute("user") User user) {
        if (user.getPositions().isEmpty())
            return "noPositionsExist";
        for (Position position : user.getPositions())
            if (!position.getName().equals("Удалена"))
                return "viewAllPositions";
        return "noPositionsExist";
    }

    @GetMapping("/entranceFromPos")
    public String getEntrance() {
        return "entrance";
    }

    @GetMapping("/addPosition")
    public String getAdd(@ModelAttribute("user") User user) {
        userForPositionValidation = user;
        return "addPosition";
    }

    @GetMapping("/editPositionHome")
    public String getEdit(@ModelAttribute("user") User user) {
        if (user.getPositions().isEmpty())
            return "noPositionsExist";
        user.setChosenOnesPositions(new ArrayList<>(user.getPositionsWithoutDeletedOne()));
        return "choosePositionToEdit";
    }

    @GetMapping("/deletePositionHome")
    public String getDelete(@ModelAttribute("user") User user) {
        if (user.getPositions().isEmpty())
            return "noPositionsExist";
        user.setChosenOnesPositions(new ArrayList<>(user.getPositionsWithoutDeletedOne()));
        return "deletePosition";
    }

    @PostMapping("/checkAddingPosition")
    public String checkAdding(
            @ModelAttribute("user") User user,
            @Valid @ModelAttribute("position") Position position,
            Errors errors) {

        if (errors.hasErrors())
            return "addPosition";

        position.setUser(user);
        positionRepository.save(position);
        user.getPositions().add(position);
        return "addPosition";
    }

    @PostMapping("/checkBeforeEditingPosition")
    public String checkBeforeEdit(@Valid @ModelAttribute("user") User user, Errors errors) {
        if (errors.hasFieldErrors("chosenOnesPositions")) {
            user.setChosenOnesPositions(new ArrayList<>(user.getPositionsWithoutDeletedOne()));
            return "choosePositionToEdit";
        }
        return "editPosition";
    }

    @PostMapping("/editPosition")
    @Transactional
    public String editPosition(@ModelAttribute("user") User user) {
        List<Position> positionsToEdit = user.getChosenOnesPositions();
        Position newPos = positionRepository.findByUserAndName(user, user.getNameToEdit());
        if (newPos == null) {
            newPos = new Position();
            newPos.setUser(user);
            newPos.setName(user.getNameToEdit());
            positionRepository.save(newPos);
            user.getPositions().add(newPos);
        }
        user.setNameToEdit("");
        editOrDeletePosition(user, newPos, positionsToEdit);
        return "positionManagementHome";
    }

    @PostMapping("/deletePosition")
    @Transactional
    public String deletePosition(@Valid @ModelAttribute("user") User user, Errors errors) {
        if (errors.hasFieldErrors("chosenOnesPositions")) {
            user.setChosenOnesPositions(new ArrayList<>(user.getPositionsWithoutDeletedOne()));
            return "deletePosition";
        }
        List<Position> positionsToDelete = user.getChosenOnesPositions();
        Position deleted = positionRepository.findByUserAndName(user, "Удалена");
        if (deleted == null) {
            deleted = new Position();
            deleted.setUser(user);
            deleted.setName("Удалена");
            positionRepository.save(deleted);
        }
        editOrDeletePosition(user, deleted, positionsToDelete);
        return "positionManagementHome";
    }

    private void editOrDeletePosition(User user, Position pos,
                                      List<Position> chosenOnes) {
        for (Position position : chosenOnes) {
            List<Employee> employees = employeeRepository.findAllByUserAndPosition(user, position);
            positionRepository.deleteByUserAndName(user, position.getName());
            user.getEmployees().removeIf(curr -> curr.getPosition().getName().equals(position.getName()));
            user.getPositions().removeIf(curr -> curr.getName().equals(position.getName()));

            for (Employee employee : employees) {
                employee.setPosition(pos);
                user.getEmployees().add(employee);
            }
        }
    }
}