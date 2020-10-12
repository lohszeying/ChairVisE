package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.service.PresentationService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/versions/{versionId}/presentations")
public class PresentationController {

    private final PresentationService presentationService;

    public PresentationController(PresentationService presentationService) {
        this.presentationService = presentationService;
    }

    @GetMapping
    public List<Presentation> all(@PathVariable Long versionId) {
        return presentationService.findAllByVersionId(versionId);
    }

    @PostMapping
    public ResponseEntity<?> newPresentation(
            @PathVariable Long versionId,
            @RequestBody Presentation presentation
    ) throws URISyntaxException {
        Presentation newPresentation = presentationService.saveForUser(versionId, presentation);

        return ResponseEntity
                .created(new URI("/presentations/" + newPresentation.getId()))
                .body(newPresentation);
    }

    @GetMapping("/{presentationId}")
    public Presentation one(@PathVariable Long presentationId) {
        return presentationService.findById(presentationId);
    }

    @PutMapping("/{presentationId}")
    public ResponseEntity<?> updatePresentation(
            @PathVariable Long presentationId,
            @RequestBody Presentation newPresentation
    ) throws URISyntaxException {

        Presentation updatedPresentation = presentationService.updatePresentation(presentationId, newPresentation);

        return ResponseEntity
                .created(new URI("/presentations/" + presentationId))
                .body(updatedPresentation);
    }

    @DeleteMapping("/{presentationId}")
    public ResponseEntity<?> deletePresentation(@PathVariable Long presentationId) {
        presentationService.deleteById(presentationId);

        return ResponseEntity.noContent().build();
    }
}
