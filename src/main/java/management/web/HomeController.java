package management.web;

import management.User;
import management.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/home")
@SessionAttributes("user")
public class HomeController {
    private final UserRepository repository;

    @Autowired
    HomeController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String showHomeForm() {
        return "home";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @GetMapping("/signIn")
    public String showSignInPage() {
        return "signIn";
    }

    @ModelAttribute(name = "user")
    public User getUser() {
        return new User();
    }

    @PostMapping("/register")
    public String processUser(@Valid @ModelAttribute("user") User user, Errors errors) {
        if (errors.hasErrors())
            return "register";
        repository.save(user);
        return "home";
    }

}
