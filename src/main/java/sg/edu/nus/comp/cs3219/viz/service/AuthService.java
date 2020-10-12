package sg.edu.nus.comp.cs3219.viz.service;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.springframework.stereotype.Service;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.exception.UnauthorisedException;

@Service
public class AuthService {

    private static final UserService userService = UserServiceFactory.getUserService();

    public UserInfo getCurrentLoginUser() {
        User user = userService.getCurrentUser();

        if (user == null) {
            throw new UnauthorisedException();
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUserEmail(user.getEmail());
        userInfo.setUserNickname(user.getNickname());
        return userInfo;
    }

    public String getLoginUrl(String redirectPage) {
        User user = userService.getCurrentUser();

        if (user == null) {
            return userService.createLoginURL(redirectPage);
        }
        return redirectPage;
    }

    public String getLogoutUrl(String redirectPage) {
        return userService.createLogoutURL(redirectPage);
    }

    public boolean isLoggedIn() {
        return userService.isUserLoggedIn();
    }

    public void verifyLoginAccess() {
        if (!isLoggedIn()) {
            throw new UnauthorisedException();
        }
    }

}
