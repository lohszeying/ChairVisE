package sg.edu.nus.comp.cs3219.viz.service;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.springframework.stereotype.Service;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.AccessLevel;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.Conference;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.common.exception.UnauthorisedException;
import sg.edu.nus.comp.cs3219.viz.common.util.Const;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationAccessControlRepository;

import java.util.Optional;

@Service
public class GateKeeper {

    private final PresentationAccessControlRepository presentationAccessControlRepository;

    public GateKeeper(PresentationAccessControlRepository presentationAccessControlRepository) {
        this.presentationAccessControlRepository = presentationAccessControlRepository;
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
        User user = userService.getCurrentUser();
        return user != null;
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

        if (!currentUser.getUserEmail().equals(conference.getCreatorIdentifier())) {
            throw new UnauthorisedException();
        }
    }

    public void verifyDeletionAccessForPresentation(Presentation presentation) {
        if (presentation == null) {
            throw new UnauthorisedException();
        }

        UserInfo currentUser = getCurrentLoginUser();

        if (!currentUser.getUserEmail().equals(presentation.getCreatorIdentifier())) {
            throw new UnauthorisedException();
        }
    }

    public void verifyAccessForPresentation(Presentation presentation, AccessLevel accessLevel) {
        if (presentation == null) {
            throw new UnauthorisedException();
        }

        // check public access
        if (presentationAccessControlRepository.existsByPresentationAndUserIdentifierEqualsAndAccessLevelEquals(presentation, Const.SpecialIdentifier.PUBLIC, accessLevel)) {
            return;
        }
        // can_write means can_read
        if (accessLevel == AccessLevel.CAN_READ &&
                presentationAccessControlRepository.existsByPresentationAndUserIdentifierEqualsAndAccessLevelEquals(presentation, Const.SpecialIdentifier.PUBLIC, AccessLevel.CAN_WRITE)) {
            return;
        }

        UserInfo currentUser = getCurrentLoginUser();

        // creator can always access their own presentation
        if (presentation.getCreatorIdentifier().equals(currentUser.getUserEmail())) {
            return;
        }

        if (presentationAccessControlRepository.existsByPresentationAndUserIdentifierEqualsAndAccessLevelEquals(presentation, currentUser.getUserEmail(), accessLevel)) {
            return;
        }

        // can_write means can_read
        if (accessLevel == AccessLevel.CAN_READ &&
                presentationAccessControlRepository.existsByPresentationAndUserIdentifierEqualsAndAccessLevelEquals(presentation, currentUser.getUserEmail(), AccessLevel.CAN_WRITE)) {
            return;
        }

        throw new UnauthorisedException();
    }
}
