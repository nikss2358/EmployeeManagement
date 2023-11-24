package management;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import management.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UsernameConstraintValidator implements ConstraintValidator<UsernameExist, String> {
    private final UserRepository repository;

    @Autowired
    UsernameConstraintValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        User user = repository.findByUsername(name);
        return user == null;
    }
}
