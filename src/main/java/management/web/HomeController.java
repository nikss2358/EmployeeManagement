package management.web;

import management.data.PositionRepository;
import management.objectData.Position;
import management.objectData.User;
import management.data.UserRepository;
import management.validation.SignInUserProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/")
@SessionAttributes("user")
public class HomeController {

    private final UserRepository userRepository;

    @Autowired
    HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute("model")
    public Model getModel(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("signInUserProxy", new SignInUserProxy());
        return model;
    }

    @GetMapping("/home")
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

    @PostMapping("/register")
    public String processUser(@Valid @ModelAttribute("user") User user, Errors errors) {
        if (errors.hasErrors())
            return "register";
        userRepository.save(user);
        user.setUsername("");
        user.setPassword("");
        return "home";
    }

    @PostMapping("/signIn")
    public String processSignIn(
            @ModelAttribute Model model,
            @Valid @ModelAttribute("signInUserProxy") SignInUserProxy signInUserProxy,
            Errors errors) {
        if (errors.hasErrors())
            return "signIn";
        User user = userRepository.findByUsername(signInUserProxy.getSignInUser().getName());
        model.addAttribute("user", user);
        return "entrance";
    }
}
