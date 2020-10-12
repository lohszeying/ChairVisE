package sg.edu.nus.comp.cs3219.viz.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public List<Presentation> findAllByVersionId(Long versionId) {
        return presentationRepository.findAllByVersionId(versionId);
    }

    @Transactional
    public Presentation findById(Long id) {
        return presentationRepository.findById(id)
                .orElseThrow(() -> new PresentationNotFoundException(id));
    }

    @Transactional
    public Presentation saveForUser(Long versionId, Presentation newPresentation) {
        UserInfo userInfo = authService.getCurrentLoginUser();
        newPresentation.setUserEmail(userInfo.getUserEmail());
        newPresentation.setVersionId(versionId);
        return presentationRepository.save(newPresentation);
    }

    @Transactional
    public Presentation updatePresentation(Long presentationId, Presentation newPresentation) {
        return presentationRepository.findById(presentationId)
                .map(presentation -> {
                    presentation.setUserEmail(newPresentation.getUserEmail());
                    presentation.setVersionId(newPresentation.getVersionId());
                    return presentationRepository.save(presentation);
                })
                .orElseThrow(() -> new PresentationNotFoundException(presentationId));
    }

    @Transactional
    public void deleteById(Long presentationId) {
        presentationRepository.findById(presentationId)
                .orElseThrow(() -> new PresentationNotFoundException(presentationId));

        presentationRepository.deleteById(presentationId);
    }
}
