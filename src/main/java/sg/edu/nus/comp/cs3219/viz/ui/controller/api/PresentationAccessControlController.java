package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.AccessLevel;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationAccessControl;
import sg.edu.nus.comp.cs3219.viz.common.exception.PresentationAccessControlNotFoundException;
import sg.edu.nus.comp.cs3219.viz.common.exception.PresentationNotFoundException;
import sg.edu.nus.comp.cs3219.viz.service.GateKeeper;
import sg.edu.nus.comp.cs3219.viz.service.PresentationAccessControlService;
import sg.edu.nus.comp.cs3219.viz.service.PresentationService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class PresentationAccessControlController extends BaseRestController {

    private final PresentationAccessControlService presentationAccessControlService;

    public PresentationAccessControlController(PresentationAccessControlService presentationAccessControlService) {
        this.presentationAccessControlService = presentationAccessControlService;
    }

    @GetMapping("/presentations/{presentationId}/accessControl")
    public List<PresentationAccessControl> all(@PathVariable Long presentationId) {
        return presentationAccessControlService.findAllByPresentation(presentationId);
    }

    @PostMapping("/presentations/{presentationId}/accessControl")
    public ResponseEntity<?> addPermission(@RequestBody PresentationAccessControl presentationAccessControl, @PathVariable Long presentationId) throws URISyntaxException {
        PresentationAccessControl newAccessControl = presentationAccessControlService.saveForPresentation(presentationId, presentationAccessControl);
        return ResponseEntity
                .created(new URI("/presentations/" + presentationId + "/accessControl/" + newAccessControl.getId()))
                .body(newAccessControl);
    }

    @PutMapping("/presentations/{presentationId}/accessControl/{accessControlId}")
    public ResponseEntity<?> updatePermission(
            @RequestBody PresentationAccessControl presentationAccessControl,
            @PathVariable Long presentationId,
            @PathVariable Long accessControlId
    ) throws URISyntaxException {
        PresentationAccessControl oldPresentationAccessControl = presentationAccessControlService.findById(accessControlId)
                .orElseThrow(() -> new PresentationAccessControlNotFoundException(presentationId, accessControlId));

        PresentationAccessControl updatedPresentationAccessControl =
                presentationAccessControlService.updatePresentationAccessControl(oldPresentationAccessControl, presentationAccessControl);

        return ResponseEntity
                .created(new URI("/presentations/" + presentationId + "/accessControl/" + accessControlId))
                .body(updatedPresentationAccessControl);
    }

    @DeleteMapping("/presentations/{presentationId}/accessControl/{accessControlId}")
    public ResponseEntity<?> removePermission(@PathVariable Long presentationId, @PathVariable Long accessControlId) {
        presentationAccessControlService.deleteById(accessControlId);

        return ResponseEntity.noContent().build();
    }
}
