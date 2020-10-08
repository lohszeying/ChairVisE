package sg.edu.nus.comp.cs3219.viz.service;

import org.springframework.stereotype.Service;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationPermission;
import sg.edu.nus.comp.cs3219.viz.common.exception.PresentationNotFoundException;
import sg.edu.nus.comp.cs3219.viz.common.exception.PresentationPermissionNotFoundException;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationPermissionRepository;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationRepository;

import java.util.List;

@Service
public class PresentationPermissionService {

    private final PresentationRepository presentationRepository;
    private final PresentationPermissionRepository presentationPermissionRepository;

    public PresentationPermissionService(PresentationRepository presentationRepository,
                                         PresentationPermissionRepository presentationPermissionRepository) {
        this.presentationRepository = presentationRepository;
        this.presentationPermissionRepository = presentationPermissionRepository;
    }

    public List<PresentationPermission> findAllByPresentation(Long presentationId) {
        Presentation presentation = presentationRepository.findById(presentationId)
                .orElseThrow(() -> new PresentationNotFoundException(presentationId));

        return presentationPermissionRepository.findAllByPresentation(presentation);
    }

    public PresentationPermission saveForPresentation(Long presentationId, PresentationPermission presentationPermission) {

        return presentationRepository.findById(presentationId)
                .map(presentation -> {
                    presentationPermission.setPresentation(presentation);
                    return presentationPermissionRepository.save(presentationPermission);
                })
                .orElseThrow(() -> new PresentationNotFoundException(presentationId));
    }

    public PresentationPermission updatePresentationAccessControl(
            Long presentationId,
            Long permissionId,
            PresentationPermission newPresentationPermission
    ) {

        return presentationPermissionRepository.findById(permissionId)
                .map(permission -> {
                    permission.setPresentation(newPresentationPermission.getPresentation());
                    permission.setPermission(newPresentationPermission.getPermission());
                    permission.setUserEmail(newPresentationPermission.getUserEmail());
                    return presentationPermissionRepository.save(permission);
                })
                .orElseThrow(() -> new PresentationPermissionNotFoundException(presentationId, permissionId));
    }

    public void deleteById(Long presentationId, Long permissionId) {
        if (presentationPermissionRepository.existsById(permissionId)) {
            presentationPermissionRepository.deleteById(permissionId);
        } else {
            throw new PresentationPermissionNotFoundException(presentationId, permissionId);
        }
    }
}
