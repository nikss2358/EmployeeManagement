package management.web;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import management.data.PositionRepository;
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

    private final PositionRepository repository;

    @Autowired
    PositionController(PositionRepository repository) {
        this.repository = repository;
    }

    @ModelAttribute("position")
    public Position getPosition() {
        return new Position();
    }

    @GetMapping("/positionHome")
    public String getPositionHome() {
        return "positionHome";
    }

    @GetMapping("/positionManagementHome")
    public String getPositionManagementHome() {
        return "positionManagementHome";
    }

    @GetMapping("/viewAllPositions")
    public String getViewAll(@ModelAttribute("user") User user) {
        if (user.getPositions().isEmpty())
            return "noPositionsExist";
        return "viewAllPositions";
    }

    @GetMapping("/addPosition")
    public String getAdd() {
        return "addPosition";
    }

    @GetMapping("/editPositionHome")
    public String getEdit(@ModelAttribute("user") User user) {
        user.setChosenOnes(new ArrayList<>(user.getPositions()));
        return "chooseEditPosition";
    }

    @GetMapping("/deletePositionHome")
    public String getDelete(@ModelAttribute("user") User user) {
        user.setChosenOnes(new ArrayList<>(user.getPositions()));
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
        repository.save(position);
        user.getPositions().add(position);
        return "addPosition";
    }

    @PostMapping("/checkBeforeEditingPosition")
    public String checkBeforeEdit(@Valid @ModelAttribute("user") User user, Errors errors) {
        if (errors.hasFieldErrors("chosenOnes")) {
            user.setChosenOnes(new ArrayList<>(user.getPositions()));
            return "chooseEditPosition";
        }
        return "editPosition";
    }

    @PostMapping("/editPosition")
    @Transactional
    public String editPosition(@ModelAttribute("user") User user) {
        String rename = user.getNameToEdit();
        List<Position> positionsToEdit = user.getChosenOnes();
            for (Position position : positionsToEdit) {

                for (Position curr : user.getPositions())
                    if (curr.getName().equals(position.getName()))
                        curr.setName(rename);

                repository.updatePosition(rename, position.getId());
            }
        return "positionManagementHome";
    }

    @PostMapping("/deletePosition")
    @Transactional
    public String deletePosition(@Valid @ModelAttribute("user") User user, Errors errors) {
        if (errors.hasFieldErrors("chosenOnes")) {
            user.setChosenOnes(new ArrayList<>(user.getPositions()));
            return "deletePosition";
        }
        List<Position> positionsToDelete = user.getChosenOnes();
        for (Position position : positionsToDelete) {
            repository.deleteByName(position.getName());
        }
        for (Position position : positionsToDelete)
            user.getPositions().removeIf(pos -> pos.getName().equals(position.getName()));

        return "positionManagementHome";
    }
}