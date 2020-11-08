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
    public List<PresentationPermission> findAllByVersion(Long versionId) {

        return presentationPermissionRepository.findAllByVersionId(versionId);
    }

    @Transactional
    public PresentationPermission savePresentationPermission(
            Long versionId,
            PresentationPermission presentationPermission
    ) {

        presentationPermission.setVersionId(versionId);
        return presentationPermissionRepository.save(presentationPermission);
    }

    @Transactional
    public PresentationPermission updatePresentationPermission(
            Long versionId,
            Long permissionId,
            PresentationPermission newPresentationPermission
    ) {

        return presentationPermissionRepository.findById(permissionId)
                .map(permission -> {
                    permission.setPermission(newPresentationPermission.getPermission());
                    permission.setUserEmail(newPresentationPermission.getUserEmail());
                    return presentationPermissionRepository.save(permission);
                })
                .orElseThrow(() -> new PresentationPermissionNotFoundException(versionId, permissionId));
    }

    @Transactional
    public void deletePresentationPermission(Long versionId, Long permissionId) {
        presentationPermissionRepository.findById(permissionId)
                .orElseThrow(() -> new PresentationPermissionNotFoundException(versionId, permissionId));

        presentationPermissionRepository.deleteById(permissionId);
    }
}
