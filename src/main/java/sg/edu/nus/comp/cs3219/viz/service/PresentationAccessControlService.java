package sg.edu.nus.comp.cs3219.viz.service;

import org.springframework.stereotype.Service;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationAccessControl;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationAccessControlRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PresentationAccessControlService {

    private final PresentationAccessControlRepository presentationAccessControlRepository;

    public PresentationAccessControlService(PresentationAccessControlRepository presentationAccessControlRepository) {
        this.presentationAccessControlRepository = presentationAccessControlRepository;
    }

    public List<PresentationAccessControl> findAllByPresentation(Presentation presentation) {
        return presentationAccessControlRepository.findAllByPresentation(presentation);
    }

    public PresentationAccessControl saveForPresentation(Presentation presentation, PresentationAccessControl presentationAccessControl) {
        PresentationAccessControl newPresentationAccessControl = new PresentationAccessControl();

        newPresentationAccessControl.setPresentation(presentation);
        newPresentationAccessControl.setUserIdentifier(presentationAccessControl.getUserIdentifier());
        newPresentationAccessControl.setAccessLevel(presentationAccessControl.getAccessLevel());

        return presentationAccessControlRepository.save(newPresentationAccessControl);
    }

    public PresentationAccessControl updatePresentationAccessControl(PresentationAccessControl oldPresentationAccessControl, PresentationAccessControl newPresentationAccessControl) {
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
