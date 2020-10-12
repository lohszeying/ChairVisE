package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationPermission;
import sg.edu.nus.comp.cs3219.viz.service.PresentationPermissionService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/presentations/{presentationId}/permissions")
public class PresentationPermissionController {

    private final PresentationPermissionService presentationPermissionService;

    public PresentationPermissionController(PresentationPermissionService presentationPermissionService) {
        this.presentationPermissionService = presentationPermissionService;
    }

    @GetMapping
    public List<PresentationPermission> all(@PathVariable Long presentationId) {
        return presentationPermissionService.findAllByPresentation(presentationId);
    }

    @PostMapping
    public ResponseEntity<?> newPermission(
            @PathVariable Long presentationId,
            @RequestBody PresentationPermission presentationPermission
    ) throws URISyntaxException {

        PresentationPermission newPermission =
                presentationPermissionService.savePresentationPermission(presentationId, presentationPermission);

        return ResponseEntity
                .created(new URI("/presentations/" + presentationId))
                .body(newPermission);
    }

    @PutMapping("/{permissionId}")
    public ResponseEntity<?> updatePermission(
            @PathVariable Long presentationId,
            @PathVariable Long permissionId,
            @RequestBody PresentationPermission presentationPermission
    ) throws URISyntaxException {

        PresentationPermission updatedPresentationPermission =
                presentationPermissionService.updatePresentationPermission(presentationId, permissionId, presentationPermission);

        return ResponseEntity
                .created(new URI("/presentations/" + presentationId + "/permissions/" + permissionId))
                .body(updatedPresentationPermission);
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<?> deletePermission(@PathVariable Long presentationId, @PathVariable Long permissionId) {
        presentationPermissionService.deletePresentationPermission(presentationId, permissionId);

        return ResponseEntity.noContent().build();
    }
}
