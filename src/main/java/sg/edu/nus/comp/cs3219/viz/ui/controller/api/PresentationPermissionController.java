package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationPermission;
import sg.edu.nus.comp.cs3219.viz.service.PresentationPermissionService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/presentations/{presentationId}/permission")
public class PresentationPermissionController extends BaseRestController {

    private final PresentationPermissionService presentationPermissionService;

    public PresentationPermissionController(PresentationPermissionService presentationPermissionService) {
        this.presentationPermissionService = presentationPermissionService;
    }

    @GetMapping
    public List<PresentationPermission> all(@PathVariable Long presentationId) {
        return presentationPermissionService.findAllByPresentation(presentationId);
    }

    @PostMapping
    public ResponseEntity<?> addPermission(
            @RequestBody PresentationPermission presentationPermission,
            @PathVariable Long presentationId
    ) throws URISyntaxException {

        PresentationPermission newPermission =
                presentationPermissionService.saveForPresentation(presentationId, presentationPermission);

        return ResponseEntity
                .created(new URI("/presentations/" + presentationId))
                .body(newPermission);
    }

    @PutMapping("/{permissionId}")
    public ResponseEntity<?> updatePermission(
            @RequestBody PresentationPermission presentationPermission,
            @PathVariable Long presentationId,
            @PathVariable Long permissionId
    ) throws URISyntaxException {

        PresentationPermission updatedPresentationPermission =
                presentationPermissionService.updatePresentationAccessControl(presentationId, permissionId, presentationPermission);

        return ResponseEntity
                .created(new URI("/presentations/" + presentationId + "/permission/" + permissionId))
                .body(updatedPresentationPermission);
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<?> removePermission(@PathVariable Long presentationId, @PathVariable Long permissionId) {
        presentationPermissionService.deleteById(presentationId, permissionId);

        return ResponseEntity.noContent().build();
    }
}
