package sg.edu.nus.comp.cs3219.viz.ui.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import sg.edu.nus.comp.cs3219.viz.service.GateKeeper;

@Aspect
@Component
public class AuthorizationAspect {

    private final GateKeeper gateKeeper;

    public AuthorizationAspect(GateKeeper gateKeeper) {
        this.gateKeeper = gateKeeper;
    }

    @Before("sg.edu.nus.comp.cs3219.viz.ui.aspect.PointcutDeclarations.forAuthentication()")
    public void beforeActions() {
        gateKeeper.verifyLoginAccess();
    }
}
