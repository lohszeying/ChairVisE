package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationSection;
import sg.edu.nus.comp.cs3219.viz.service.PresentationSectionService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class PresentationSectionController extends BaseRestController {

    private final PresentationSectionService presentationSectionService;

    public PresentationSectionController(PresentationSectionService presentationSectionService) {
        this.presentationSectionService = presentationSectionService;
    }

    @GetMapping("/presentations/{presentationId}/sections")
    public List<PresentationSection> all(@PathVariable Long presentationId) {
        return presentationSectionService.findAllByPresentation(presentationId);
    }

    @PostMapping("/presentations/{presentationId}/sections")
    public ResponseEntity<?> newPresentationSection(
            @PathVariable Long presentationId,
            @RequestBody PresentationSection presentationSection
    ) throws URISyntaxException {
        PresentationSection newPresentationSection = presentationSectionService.saveForPresentation(presentationId, presentationSection);

        return ResponseEntity
                .created(new URI("/presentations/" + presentationId + "/section/" + newPresentationSection.getId()))
                .body(newPresentationSection);
    }

    @PutMapping("/presentations/{presentationId}/sections/{sectionId}")
    public ResponseEntity<?> updatePresentationSection(
            @PathVariable Long presentationId,
            @PathVariable Long sectionId,
            @RequestBody PresentationSection newPresentationSection
    ) throws URISyntaxException {
        PresentationSection updatedPresentationSection =
                presentationSectionService.updatePresentation(presentationId, sectionId, newPresentationSection);

        return ResponseEntity
                .created(new URI("/presentations/" + presentationId + "/section/" + updatedPresentationSection.getId()))
                .body(updatedPresentationSection);
    }

    @DeleteMapping("/presentations/{presentationId}/sections/{sectionId}")
    public ResponseEntity<?> deletePresentationSection(@PathVariable Long presentationId, @PathVariable Long sectionId) {
        presentationSectionService.deleteById(presentationId, sectionId);

        return ResponseEntity.noContent().build();
    }

}
