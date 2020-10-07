package sg.edu.nus.comp.cs3219.viz.service;

import org.springframework.stereotype.Service;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationAccessControl;
import sg.edu.nus.comp.cs3219.viz.common.exception.PresentationNotFoundException;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationAccessControlRepository;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PresentationAccessControlService {

    private final PresentationRepository presentationRepository;
    private final PresentationAccessControlRepository presentationAccessControlRepository;

    public PresentationAccessControlService(PresentationRepository presentationRepository,
                                            PresentationAccessControlRepository presentationAccessControlRepository) {
        this.presentationRepository = presentationRepository;
        this.presentationAccessControlRepository = presentationAccessControlRepository;
    }

    public List<PresentationAccessControl> findAllByPresentation(Long presentationId) {
        Presentation presentation = presentationRepository.findById(presentationId)
                .orElseThrow(() -> new PresentationNotFoundException(presentationId));

        return presentationAccessControlRepository.findAllByPresentation(presentation);
    }

    public PresentationAccessControl saveForPresentation(Long presentationId, PresentationAccessControl presentationAccessControl) {
        PresentationAccessControl newPresentationAccessControl = new PresentationAccessControl();

        Presentation presentation = presentationRepository.findById(presentationId)
                .orElseThrow(() -> new PresentationNotFoundException(presentationId));

        newPresentationAccessControl.setPresentation(presentation);
        newPresentationAccessControl.setUserIdentifier(presentationAccessControl.getUserIdentifier());
        newPresentationAccessControl.setAccessLevel(presentationAccessControl.getAccessLevel());

        return presentationAccessControlRepository.save(newPresentationAccessControl);
    }

    public PresentationAccessControl updatePresentationAccessControl(
            PresentationAccessControl oldPresentationAccessControl,
            PresentationAccessControl newPresentationAccessControl
    ) {
        oldPresentationAccessControl.setAccessLevel(newPresentationAccessControl.getAccessLevel());

        return presentationAccessControlRepository.save(oldPresentationAccessControl);
    }

    public Optional<PresentationAccessControl> findById(Long id) {
        return presentationAccessControlRepository.findById(id);
    }

    public void deleteById(Long id) {
        presentationAccessControlRepository.deleteById(id);
    }
}
