package management.web;

import management.objectData.User;
import management.data.UserRepository;
import management.validation.SignInUser;
import management.validation.SignInUserProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/")
@SessionAttributes("user")
public class HomeController {

    private final UserRepository repository;

    @Autowired
    HomeController(UserRepository repository) {
        this.repository = repository;
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

    @ModelAttribute(name = "user")
    public User getUser() {
        return new User();
    }

    @ModelAttribute(name = "signInUserProxy")
    public SignInUserProxy getSignInUserProxy() {
        return new SignInUserProxy();
    }

    @PostMapping("/register")
    public String processUser(@Valid @ModelAttribute("user") User user, Errors errors) {
        if (errors.hasErrors())
            return "register";
        repository.save(user);
        user.setPassword("");
        user.setUsername("");
        return "home";
    }

    @PostMapping("/signIn")
    public String processSignIn(@ModelAttribute("user") User userAttribute,
                                @Valid @ModelAttribute("signInUserProxy") SignInUserProxy signInUserProxy,
                                Errors errors) {
        if (errors.hasErrors())
            return "signIn";

        userAttribute.setUsername(signInUserProxy.getSignInUser().getName());
        userAttribute.setPassword(signInUserProxy.getSignInUser().getPass());
        return "entrance";
    }

}
