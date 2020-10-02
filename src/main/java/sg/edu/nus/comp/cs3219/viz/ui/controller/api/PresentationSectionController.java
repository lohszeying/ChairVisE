package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.AccessLevel;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationSection;
import sg.edu.nus.comp.cs3219.viz.common.exception.PresentationNotFoundException;
import sg.edu.nus.comp.cs3219.viz.common.exception.PresentationSectionNotFoundException;
import sg.edu.nus.comp.cs3219.viz.service.GateKeeper;
import sg.edu.nus.comp.cs3219.viz.service.PresentationService;
import sg.edu.nus.comp.cs3219.viz.service.PresentationSectionService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class PresentationSectionController extends BaseRestController {

    private final PresentationService presentationService;
    private final PresentationSectionService presentationSectionService;

    private final GateKeeper gateKeeper;

    public PresentationSectionController(PresentationService presentationService,
                                         PresentationSectionService presentationSectionService,
                                         GateKeeper gateKeeper) {
        this.presentationService = presentationService;
        this.presentationSectionService = presentationSectionService;
        this.gateKeeper = gateKeeper;
    }

    @GetMapping("/presentations/{presentationId}/sections")
    public List<PresentationSection> all(@PathVariable Long presentationId) {
        Presentation presentation = presentationService.findById(presentationId)
                .orElseThrow(() -> new PresentationNotFoundException(presentationId));
        gateKeeper.verifyAccessForPresentation(presentation, AccessLevel.CAN_READ);

        return presentationSectionService.findAllByPresentation(presentation);
    }

    @PostMapping("/presentations/{presentationId}/sections")
    public ResponseEntity<?> newPresentationSection(@PathVariable Long presentationId, @RequestBody PresentationSection presentationSection) throws URISyntaxException {
        Presentation presentation = presentationService.findById(presentationId)
                .orElseThrow(() -> new PresentationNotFoundException(presentationId));
        gateKeeper.verifyAccessForPresentation(presentation, AccessLevel.CAN_WRITE);

        PresentationSection newPresentationSection = presentationSectionService.saveForPresentation(presentation, presentationSection);

        return ResponseEntity
                .created(new URI("/presentations/" + presentationId + "/section/" + newPresentationSection.getId()))
                .body(newPresentationSection);
    }

    @PutMapping("/presentations/{presentationId}/sections/{sectionId}")
    public ResponseEntity<?> updatePresentationSection(@PathVariable Long presentationId, @PathVariable Long sectionId,
                                                       @RequestBody PresentationSection newPresentationSection) throws URISyntaxException {
        PresentationSection oldPresentationSection = presentationSectionService.findById(sectionId)
                .orElseThrow(() -> new PresentationSectionNotFoundException(presentationId, sectionId));

        Presentation presentation = oldPresentationSection.getPresentation();
        gateKeeper.verifyAccessForPresentation(presentation, AccessLevel.CAN_WRITE);

        PresentationSection updatedPresentationSection =
                presentationSectionService.updatePresentation(oldPresentationSection, newPresentationSection);

        return ResponseEntity
                .created(new URI("/presentations/" + presentationId + "/section/" + updatedPresentationSection.getId()))
                .body(updatedPresentationSection);
    }

    @DeleteMapping("/presentations/{presentationId}/sections/{sectionId}")
    public ResponseEntity<?> deletePresentationSection(@PathVariable Long presentationId, @PathVariable Long sectionId) {
        PresentationSection presentationSection = presentationSectionService.findById(sectionId)
                .orElseThrow(() -> new PresentationSectionNotFoundException(presentationId, sectionId));

        Presentation presentation = presentationSection.getPresentation();
        gateKeeper.verifyAccessForPresentation(presentation, AccessLevel.CAN_WRITE);

        presentationSectionService.deleteById(sectionId);

        return ResponseEntity.noContent().build();
    }

}
