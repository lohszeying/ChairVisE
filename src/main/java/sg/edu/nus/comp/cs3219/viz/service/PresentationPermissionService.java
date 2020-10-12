package sg.edu.nus.comp.cs3219.viz.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationPermission;
import sg.edu.nus.comp.cs3219.viz.common.exception.PresentationPermissionNotFoundException;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationPermissionRepository;

import java.util.List;

@Service
public class PresentationPermissionService {

    private final PresentationPermissionRepository presentationPermissionRepository;

    public PresentationPermissionService(PresentationPermissionRepository presentationPermissionRepository) {
        this.presentationPermissionRepository = presentationPermissionRepository;
    }

    @Transactional
    public List<PresentationPermission> findAllByPresentation(Long presentationId) {

        return presentationPermissionRepository.findAllByPresentationId(presentationId);
    }

    @Transactional
    public PresentationPermission savePresentationPermission(
            Long presentationId,
            PresentationPermission presentationPermission
    ) {

        presentationPermission.setPresentationId(presentationId);
        return presentationPermissionRepository.save(presentationPermission);
    }

    @Transactional
    public PresentationPermission updatePresentationPermission(
            Long presentationId,
            Long permissionId,
            PresentationPermission newPresentationPermission
    ) {

        return presentationPermissionRepository.findById(permissionId)
                .map(permission -> {
                    permission.setPermission(newPresentationPermission.getPermission());
                    permission.setUserEmail(newPresentationPermission.getUserEmail());
                    return presentationPermissionRepository.save(permission);
                })
                .orElseThrow(() -> new PresentationPermissionNotFoundException(presentationId, permissionId));
    }

    @Transactional
    public void deletePresentationPermission(Long presentationId, Long permissionId) {
        presentationPermissionRepository.findById(permissionId)
                .orElseThrow(() -> new PresentationPermissionNotFoundException(presentationId, permissionId));

        presentationPermissionRepository.deleteById(permissionId);
    }
}
