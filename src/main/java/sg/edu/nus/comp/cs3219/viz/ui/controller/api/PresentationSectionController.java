package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationSection;
import sg.edu.nus.comp.cs3219.viz.service.PresentationSectionService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/versions/{versionId}/sections")
public class PresentationSectionController {

    private final PresentationSectionService presentationSectionService;

    public PresentationSectionController(PresentationSectionService presentationSectionService) {
        this.presentationSectionService = presentationSectionService;
    }

    @GetMapping
    public List<PresentationSection> all(@PathVariable Long versionId) {
        return presentationSectionService.findAllByVersion(versionId);
    }

    @PostMapping
    public ResponseEntity<?> newPresentationSection(
            @PathVariable Long versionId,
            @RequestBody PresentationSection presentationSection
    ) throws URISyntaxException {
        PresentationSection newPresentationSection =
                presentationSectionService.savePresentationSection(versionId, presentationSection);

        return ResponseEntity
                .created(new URI("/versions/" + versionId + "/sections/" + newPresentationSection.getId()))
                .body(newPresentationSection);
    }

    @PutMapping("/{sectionId}")
    public ResponseEntity<?> updatePresentationSection(
            @PathVariable Long versionId,
            @PathVariable Long sectionId,
            @RequestBody PresentationSection newPresentationSection
    ) throws URISyntaxException {
        PresentationSection updatedPresentationSection =
                presentationSectionService.updatePresentationSection(versionId, sectionId, newPresentationSection);

        return ResponseEntity
                .created(new URI("/versions/" + versionId + "/sections/" + updatedPresentationSection.getId()))
                .body(updatedPresentationSection);
    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<?> deletePresentationSection(@PathVariable Long versionId, @PathVariable Long sectionId) {
        presentationSectionService.deletePresentationSection(versionId, sectionId);

        return ResponseEntity.noContent().build();
    }

}
