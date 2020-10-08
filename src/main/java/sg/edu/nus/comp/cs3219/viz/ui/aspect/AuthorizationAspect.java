package sg.edu.nus.comp.cs3219.viz.ui.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import sg.edu.nus.comp.cs3219.viz.service.AuthService;

@Aspect
@Component
public class AuthorizationAspect {

    private final AuthService authService;

    public AuthorizationAspect(AuthService authService) {
        this.authService = authService;
    }

    @Before("sg.edu.nus.comp.cs3219.viz.ui.aspect.PointcutDeclarations.forAuthentication()")
    public void beforeActions() {
        authService.verifyLoginAccess();
    }
}
