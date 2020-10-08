package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.util.Config;
import sg.edu.nus.comp.cs3219.viz.service.AuthService;
import sg.edu.nus.comp.cs3219.viz.ui.controller.data.AuthInfo;

@RestController
public class AuthInfoController extends BaseRestController {

    private final AuthService authService;

    public AuthInfoController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/auth")
    public AuthInfo getAuthInfo(@RequestParam(value = "redirectUrl", required = false) String redirectUrl) {
        String redirect = redirectUrl == null ? Config.APP_URL : redirectUrl;

        UserInfo userInfo = null;
        if (authService.isLoggedIn())
            userInfo = authService.getCurrentLoginUser();

        AuthInfo authInfo = new AuthInfo();
        authInfo.setLogin(userInfo != null);
        // development server doesn't have urlPrefix while production server has
        String urlPrefix = Config.isDevServer() ? Config.APP_URL : "";
        if (authInfo.isLogin()) {
            authInfo.setLogoutUrl(urlPrefix + authService.getLogoutUrl(redirect));
            authInfo.setUserInfo(userInfo);
        } else {
            authInfo.setLoginUrl(urlPrefix + authService.getLoginUrl(redirect));
        }

        return authInfo;
    }

}
