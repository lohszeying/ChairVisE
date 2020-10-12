package sg.edu.nus.comp.cs3219.viz.service;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.springframework.stereotype.Service;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.Permission;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.Conference;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.common.exception.UnauthorisedException;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationPermissionRepository;

@Service
public class AuthService {

    private final PresentationPermissionRepository presentationPermissionRepository;

    public AuthService(PresentationPermissionRepository presentationPermissionRepository) {
        this.presentationPermissionRepository = presentationPermissionRepository;
    }

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

    public void verifyDeletionAccessForConference(Conference conference) {
        if (conference == null) {
            throw new UnauthorisedException();
        }

        UserInfo currentUser = getCurrentLoginUser();

        if (!currentUser.getUserEmail().equals(conference.getUserEmail())) {
            throw new UnauthorisedException();
        }
    }

    public void verifyDeletionAccessForPresentation(Presentation presentation) {
        if (presentation == null) {
            throw new UnauthorisedException();
        }

        UserInfo currentUser = getCurrentLoginUser();

        if (!currentUser.getUserEmail().equals(presentation.getUserEmail())) {
            throw new UnauthorisedException();
        }
    }

    public void verifyAccessForPresentation(Presentation presentation, Permission permission) {
        if (presentation == null) {
            throw new UnauthorisedException();
        }

        // check public access
        if (presentationPermissionRepository.existsByPresentationAndPermissionEquals(presentation, permission)) {
            return;
        }
        // can_write means can_read
        if (permission == Permission.CAN_READ &&
                presentationPermissionRepository.existsByPresentationAndPermissionEquals(presentation, Permission.CAN_WRITE)) {
            return;
        }

        UserInfo currentUser = getCurrentLoginUser();

        // creator can always access their own presentation
        if (presentation.getUserEmail().equals(currentUser.getUserEmail())) {
            return;
        }

        if (presentationPermissionRepository.existsByPresentationAndPermissionEquals(presentation, permission)) {
            return;
        }

        // can_write means can_read
        if (permission == Permission.CAN_READ &&
                presentationPermissionRepository.existsByPresentationAndPermissionEquals(presentation, Permission.CAN_WRITE)) {
            return;
        }

        throw new UnauthorisedException();
    }
}
