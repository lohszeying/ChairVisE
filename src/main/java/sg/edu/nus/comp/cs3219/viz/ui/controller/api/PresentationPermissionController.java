package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationPermission;
import sg.edu.nus.comp.cs3219.viz.service.PresentationPermissionService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/versions/{versionId}/permissions")
public class PresentationPermissionController {

    private final PresentationPermissionService presentationPermissionService;

    public PresentationPermissionController(PresentationPermissionService presentationPermissionService) {
        this.presentationPermissionService = presentationPermissionService;
    }

    @GetMapping
    public List<PresentationPermission> all(@PathVariable Long versionId) {
        return presentationPermissionService.findAllByVersion(versionId);
    }

    @PostMapping
    public ResponseEntity<?> newPermission(
            @PathVariable Long versionId,
            @RequestBody PresentationPermission presentationPermission
    ) throws URISyntaxException {

        PresentationPermission newPermission =
                presentationPermissionService.savePresentationPermission(versionId, presentationPermission);

        return ResponseEntity
                .created(new URI("/versions/" + versionId))
                .body(newPermission);
    }

    @PutMapping("/{permissionId}")
    public ResponseEntity<?> updatePermission(
            @PathVariable Long versionId,
            @PathVariable Long permissionId,
            @RequestBody PresentationPermission presentationPermission
    ) throws URISyntaxException {

        PresentationPermission updatedPresentationPermission =
                presentationPermissionService.updatePresentationPermission(versionId, permissionId, presentationPermission);

        return ResponseEntity
                .created(new URI("/versions/" + versionId + "/permissions/" + permissionId))
                .body(updatedPresentationPermission);
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<?> deletePermission(@PathVariable Long versionId, @PathVariable Long permissionId) {
        presentationPermissionService.deletePresentationPermission(versionId, permissionId);

        return ResponseEntity.noContent().build();
    }
}
