package sg.edu.nus.comp.cs3219.viz.service;

import org.springframework.stereotype.Service;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.common.exception.PresentationNotFoundException;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationRepository;

import java.util.List;

@Service
public class PresentationService {

    private final PresentationRepository presentationRepository;
    private final AuthService authService;

    public PresentationService(PresentationRepository presentationRepository, AuthService authService) {
        this.presentationRepository = presentationRepository;
        this.authService = authService;
    }

    public List<Presentation> findAllForUser() {
        UserInfo userInfo = authService.getCurrentLoginUser();
        return presentationRepository.findByUserEmail(userInfo.getUserEmail());
    }

    public Presentation findById(Long id) {
        return presentationRepository.findById(id)
                .orElseThrow(() -> new PresentationNotFoundException(id));
    }

    public Presentation saveForUser(Presentation newPresentation) {
        UserInfo userInfo = authService.getCurrentLoginUser();
        newPresentation.setUserEmail(userInfo.getUserEmail());
        return presentationRepository.save(newPresentation);
    }

    public Presentation updatePresentation(Long id, Presentation newPresentation) {
        return presentationRepository.findById(id)
                .map(presentation -> {
                    presentation.setName(newPresentation.getName());
                    presentation.setVersion(newPresentation.getVersion());
                    presentation.setDescription(newPresentation.getDescription());
                    return presentationRepository.save(presentation);
                })
                .orElseThrow(() -> new PresentationNotFoundException(id));
    }

    public void deleteById(Long id) {
        presentationRepository.deleteById(id);
    }
}
