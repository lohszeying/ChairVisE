package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationSection;
import sg.edu.nus.comp.cs3219.viz.service.PresentationSectionService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/presentations/{presentationId}/sections")
public class PresentationSectionController {

    private final PresentationSectionService presentationSectionService;

    public PresentationSectionController(PresentationSectionService presentationSectionService) {
        this.presentationSectionService = presentationSectionService;
    }

    @GetMapping
    public List<PresentationSection> all(@PathVariable Long presentationId) {
        return presentationSectionService.findAllByPresentation(presentationId);
    }

    @PostMapping
    public ResponseEntity<?> newPresentationSection(
            @PathVariable Long presentationId,
            @RequestBody PresentationSection presentationSection
    ) throws URISyntaxException {
        PresentationSection newPresentationSection = presentationSectionService.savePresentationSection(presentationId, presentationSection);

        return ResponseEntity
                .created(new URI("/presentations/" + presentationId + "/sections/" + newPresentationSection.getId()))
                .body(newPresentationSection);
    }

    @PutMapping("/{sectionId}")
    public ResponseEntity<?> updatePresentationSection(
            @PathVariable Long presentationId,
            @PathVariable Long sectionId,
            @RequestBody PresentationSection newPresentationSection
    ) throws URISyntaxException {
        PresentationSection updatedPresentationSection =
                presentationSectionService.updatePresentationSection(presentationId, sectionId, newPresentationSection);

        return ResponseEntity
                .created(new URI("/presentations/" + presentationId + "/sections/" + updatedPresentationSection.getId()))
                .body(updatedPresentationSection);
    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<?> deletePresentationSection(@PathVariable Long presentationId, @PathVariable Long sectionId) {
        presentationSectionService.deletePresentationSection(presentationId, sectionId);

        return ResponseEntity.noContent().build();
    }

}
