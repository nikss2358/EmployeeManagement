package management.validation;

import lombok.Data;

@Data
public class SignInUserProxy {
    @InvalidProperties
    private SignInUser signInUser;

    public SignInUserProxy() {
        signInUser = new SignInUser();
    }
}
